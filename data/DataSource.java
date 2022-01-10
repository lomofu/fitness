package data;

import bean.*;
import constant.CustomerSateEnum;
import constant.DataManipulateEnum;
import constant.DefaultDataConstant;
import constant.UIConstant;
import dto.CustomerDto;
import dto.RoleDto;
import dto.VisitorDto;
import ui.ClubFrameView;
import utils.CSVUtil;
import utils.DateUtil;
import utils.IDUtil;
import utils.Logger;

import javax.swing.Timer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * 1. This class is a resposity to store the all system data we need
 * 2. Aslo, the datasoure is the observed, when there is a data change event. It will broadcast to the subscribers.
 * 3. It will automatically refresh the data each 1 hour to check the system has some expired members today or not.
 * and updatet the expiry state.
 * 4. DataSource gives easy method to manger the data.
 * 5. It will automatically back up the data each 1 hour
 */
@SuppressWarnings("unchecked")
public class DataSource implements ActionListener {
    // this blocking queue store each subscriber information which will be used when broadcast
    private static final BlockingQueue<DataSourceChannelInfo> queue = new LinkedBlockingQueue<>();
    // store the promotions
    private static List<Promotion> promotionList = new ArrayList<>();
    // store the courses
    private static List<Course> courseList = new ArrayList<>();
    // store the roles
    private static List<RoleDto> roleList = new ArrayList<>();
    // store the membership info
    private static List<CustomerDto> customerList = new ArrayList<>();
    // store the consumption records
    private static List<Consumption> consumptionList = new ArrayList<>();
    // store the visitors counts of every day
    private static List<VisitorDto> visitorDtoList = new ArrayList<>();

    public DataSource() {
        refreshJob();
    }

    // init the data source when load the ui before
    public static void init() {
        Logger.banner();
        try {
            readPromotionList();
            readCourseList();
            readRoleList();
            readCustomerList();
            readConsumptionList();
            readVisitorList();
        } catch(IOException e) {
            Logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, """
                    We are sorry there exist some error when loading the file
                                        
                    Suggestion:
                    1. Please use the files in the backup folder to cover the old file.
                    2. Or TRY TO DELETE, BUT THE DATA WILL BE LOST.
                    """, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void readPromotionList() throws IOException {
        promotionList = CSVUtil.read(DefaultDataConstant.PROMOTION_CSV_PATH, Promotion.class);
    }

    private static void readCourseList() throws IOException {
        courseList = CSVUtil.read(DefaultDataConstant.COURSE_CSV_PATH, Course.class, DefaultDataConstant.DEFAULT_COURSES);
    }

    private static void readConsumptionList() throws IOException {
        consumptionList = CSVUtil.read(DefaultDataConstant.CONSUMPTION_CSV_PATH, Consumption.class);
    }

    private static void readVisitorList() throws IOException {
        visitorDtoList = CSVUtil.read(DefaultDataConstant.VISITOR_CSV_PATH, Visitor.class)
                .stream()
                // use the map to converse to a visitor dto
                .map(e -> {
                    VisitorDto visitorDto = new VisitorDto();
                    visitorDto.setDate(DateUtil.str2Date(e.getDate()));
                    visitorDto.setCount(Integer.parseInt(e.getCount()));
                    return visitorDto;
                })
                .collect(Collectors.toList());
    }

    private static void readRoleList() throws IOException {
        roleList = CSVUtil.read(DefaultDataConstant.ROLE_CSV_PATH, Role.class, DefaultDataConstant.DEFAULT_MEMBERS)
                .stream()
                // use the builder to build a new role dto
                .map(e -> new RoleDto.Builder()
                        .roleId(e.getRoleId())
                        .roleName(e.getRoleName())
                        .oneMonth(new BigDecimal(e.getOneMonth()))
                        .threeMonth(new BigDecimal(e.getThreeMonth()))
                        .halfYear(new BigDecimal(e.getHalfYear()))
                        .fullYear(new BigDecimal(e.getFullYear()))
                        .gym("true".equals(e.getGym()))
                        .swimmingPool("true".equals(e.getSwimmingPool()))
                        .courseList(DataSourceHandler.findCoursesByCourseName(e.getCourseList()))
                        .build())
                .collect(Collectors.toList());
    }

    private static void readCustomerList() throws IOException {
        customerList =
                CSVUtil.read(DefaultDataConstant.CUSTOMER_CSV_PATH, Customer.class, false).stream()
                        .peek(e -> {
                            // cover the original file that missing some value in fields
                            if("".equals(e.getId())) {
                                e.setId(IDUtil.generateUUID());
                            }
                            if(! "".equals(e.getDateOfBirth())) {
                                e.setDateOfBirth(DateUtil.format(e.getDateOfBirth()));
                            }
                            if("".equals(e.getState())) {
                                e.setState(CustomerSateEnum.EXPIRED.getName());
                            }
                            if("".equals(e.getDuration())) {
                                e.setDuration("-1");
                            }
                            if("".equals(e.getGender())) {
                                e.setGender(UIConstant.MEMBER_GENDER_LIST[2]);
                            }
                        })
                        // use the builder pattern to converse to a customer dto
                        .map(e -> new CustomerDto.Builder()
                                .id(e.getId())
                                .firstName(e.getFirstName())
                                .lastName(e.getLastName())
                                .dateOfBirth(DateUtil.str2Date(e.getDateOfBirth()))
                                .gender(e.getGender())
                                .fees(new BigDecimal(e.getFees()))
                                .healthCondition(e.getHealthCondition())
                                .startDate(DateUtil.str2Date(e.getStartDate()))
                                .expireTime(DateUtil.str2Date(e.getExpireTime()))
                                .homeAddress(e.getHomeAddress())
                                .phoneNumber(e.getPhoneNumber())
                                .duration(Integer.parseInt(e.getDuration()))
                                .type(DataSourceHandler.findRoleDtoById(e.getType()))
                                .state(e.getState())
                                .parentId(e.getParentId())
                                .build())
                        // use the peek to cover the age calculation
                        .peek(e -> {
                            e.setAge(DateUtil.calculateAge(e.getDateOfBirth()));
                            // to make sure the membership correctly state
                            if(Objects.isNull(e.getState()) || "".equals(e.getState())) {
                                if(Objects.nonNull(e.getStartDate()) && Objects.nonNull(e.getExpireTime())) {
                                    boolean before = DateUtil.isBefore(e.getExpireTime(), LocalDate.now());
                                    if(before) {
                                        e.setState(CustomerSateEnum.EXPIRED.getName());
                                    } else {
                                        e.setState(CustomerSateEnum.ACTIVE.getName());
                                    }
                                }
                            }
                        })
                        .sorted(Comparator.comparing(CustomerDto::getState))
                        .collect(Collectors.toList());
    }

    // getter
    public static List<RoleDto> getRoleList() {
        return roleList;
    }

    public static List<Course> getCourseList() {
        return courseList;
    }

    public static List<CustomerDto> getCustomerList() {
        return customerList;
    }

    public static List<Consumption> getConsumptionList() {
        return consumptionList;
    }

    public static List<Promotion> getPromotionList() {
        return promotionList;
    }

    public static List<VisitorDto> getVisitorDtoList() {
        return visitorDtoList;
    }

    /**
     * execute this method when call subscribe method. It will put the subscribers' info in the blocking queue
     *
     * @param channelInfo the subscriber info including the which data type they observer, the reference address,
     *                    cuz we will execute it onDataChange method when broadcast
     */
    public static void subscribe(DataSourceChannelInfo channelInfo) {
        // if the queue is full
        if(! queue.offer(channelInfo))
            throw new RuntimeException("Cannot subscribe datasource");
    }

    /**
     * Do the add action of each type
     * 1. it will store the corresponding type list into file
     * 2. broadcast who subscribe this type and execute their onDataChangeMethod
     *
     * @param t   the object need to be add
     * @param <T> the generic type
     */
    public static <T> void add(T t) {
        // use the instanceof to decide which type it is and do correspond strategy
        if(t instanceof CustomerDto customerDto) {
            customerList.add(customerDto);
            // save & broadcast
            save(DefaultDataConstant.CUSTOMER_CSV_PATH, "customer");
            broadcast(customerDto, DataManipulateEnum.INSERT);
        }

        if(t instanceof Consumption consumption) {
            consumptionList.add(consumption);
            // save & broadcast
            save(DefaultDataConstant.CONSUMPTION_CSV_PATH, "consumption");
            broadcast(consumption, DataManipulateEnum.INSERT);
        }

        if(t instanceof RoleDto roleDto) {
            roleList.add(roleDto);
            // save & broadcast
            save(DefaultDataConstant.ROLE_CSV_PATH, "role");
            broadcast(roleDto, DataManipulateEnum.INSERT);
        }

        if(t instanceof Course course) {
            courseList.add(course);
            // save & broadcast
            save(DefaultDataConstant.COURSE_CSV_PATH, "course");
            broadcast(course, DataManipulateEnum.INSERT);
        }

        if(t instanceof Promotion promotion) {
            promotionList.add(promotion);
            // save & broadcast
            save(DefaultDataConstant.PROMOTION_CSV_PATH, "promotion");
            broadcast(promotion, DataManipulateEnum.INSERT);
        }

        if(t instanceof VisitorDto visitorDto) {
            visitorDtoList.add(visitorDto);
            // save & broadcast
            save(DefaultDataConstant.VISITOR_CSV_PATH, "visitor");
            broadcast(visitorDto, DataManipulateEnum.INSERT);
        }

        // broadcast the subscribers with statistic data and action flag
        broadcast(new Statistics(), DataManipulateEnum.INSERT);
    }

    /**
     * Sames to the add method
     * 1. it will store the corresponding type list into file
     * 2. broadcast who subscribe this type and execute their onDataChangeMethod
     * <p>
     * Important, the remove is not directly remove the element from the collection/
     * It will filter it and create a new array and assign it address.
     *
     * @param list the list of object need to be removed
     * @param <T>  the generic type
     */
    public static <T> void remove(List<T> list) {
        // use the first element of the list to device which type it is
        if(list.get(0) instanceof CustomerDto customerDto) {
            for(T t : list) {
                CustomerDto dto = (CustomerDto) t;
                customerList = customerList
                        .stream()
                        // filter and create a new list
                        .filter(e -> ! e.getId().equals(dto.getId()))
                        .collect(Collectors.toList());
            }
            // save & broadcast
            save(DefaultDataConstant.CUSTOMER_CSV_PATH, "customer");
            broadcast(customerDto, DataManipulateEnum.DELETE);
        }

        // same to before
        if(list.get(0) instanceof Consumption consumption) {
            for(T t : list) {
                Consumption c = (Consumption) t;
                consumptionList = consumptionList.stream()
                        .filter(e -> ! e.orderId().equals(c.orderId()))
                        .collect(Collectors.toList());
            }
            save(DefaultDataConstant.CONSUMPTION_CSV_PATH, "consumption");
            broadcast(consumption, DataManipulateEnum.DELETE);
        }

        // same to before
        if(list.get(0) instanceof Promotion promotion) {
            for(T t : list) {
                Promotion p = (Promotion) t;
                promotionList = promotionList.stream()
                        .filter(e -> ! e.getPromotionId().equals(p.getPromotionId()))
                        .collect(Collectors.toList());
            }
            save(DefaultDataConstant.PROMOTION_CSV_PATH, "promotion");
            broadcast(promotion, DataManipulateEnum.DELETE);
        }

        // broadcast the subscribers with statistic data and action flag
        broadcast(new Statistics(), DataManipulateEnum.DELETE);
    }

    /**
     * Sames to the add & remove
     *
     * @param t   the object need to be updated
     * @param <T> the generic type
     */
    public static <T> void update(T t) {
        if(t instanceof CustomerDto customerDto) {
            // save & broadcast
            broadcast(customerDto, DataManipulateEnum.UPDATE);
            save(DefaultDataConstant.CUSTOMER_CSV_PATH, "customer");
        }

        if(t instanceof RoleDto roleDto) {
            // save & broadcast
            broadcast(roleDto, DataManipulateEnum.UPDATE);
            save(DefaultDataConstant.ROLE_CSV_PATH, "role");
        }

        if(t instanceof Course course) {
            // save & broadcast
            broadcast(course, DataManipulateEnum.UPDATE);
            save(DefaultDataConstant.COURSE_CSV_PATH, "course");
        }

        if(t instanceof VisitorDto visitorDto) {
            // save & broadcast
            broadcast(visitorDto, DataManipulateEnum.UPDATE);
            save(DefaultDataConstant.VISITOR_CSV_PATH, "visitor");
        }
        // broadcast although no data type below, sometimes maybe need to fetch again the data
        broadcast(new Statistics(), DataManipulateEnum.UPDATE);
    }

    /**
     * The broadcast method will iterate the queue in queue order, then filter the target the subscribes we need to
     * notify, and then execute their onDataChange method
     *
     * @param t                  the t object which will used to reflect to get the class type
     * @param dataManipulateEnum the subscriber info, it contains the type they want to subscribe and their heap address,
     *                           so that we can call the onDataChange method
     * @param <T>                the generic type
     */
    private static <T> void broadcast(T t, DataManipulateEnum dataManipulateEnum) {
        CompletableFuture<?>[] futures = queue.stream().filter(e -> e.gettClass().equals(t.getClass()))
                .map(DataSourceChannelInfo::getDataSourceChannel)
                .map(e -> CompletableFuture.runAsync(() -> e.onDataChange(t, dataManipulateEnum)))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures);
    }

    /**
     * The save method will async to write to the file and will not block the ui thread
     *
     * @param path the path we need to write to
     * @param type the action flag
     */
    private static void save(String path, String type) {
        if("customer".equals(type)) {
            CompletableFuture.runAsync(() -> {
                // sort by first name firstly
                customerList.sort(Comparator.comparing(CustomerDto::getFirstName));
                // set the progress value of the data size
                ClubFrameView.syncState(customerList.size());
                // write method
                CSVUtil.write(path, customerList);
            });
        }
        // same above
        if("consumption".equals(type)) {
            CompletableFuture.runAsync(() -> {
                consumptionList.sort(Comparator.comparing(Consumption::orderId).reversed());
                ClubFrameView.syncState(consumptionList.size());
                CSVUtil.write(path, consumptionList);
            });
        }
        // same above
        if("role".equals(type)) {
            CompletableFuture.runAsync(() -> {
                roleList.sort(Comparator.comparing(RoleDto::getRoleId));
                ClubFrameView.syncState(roleList.size());
                CSVUtil.write(path, roleList);
            });
        }
        // same above
        if("course".equals(type)) {
            CompletableFuture.runAsync(() -> {
                courseList.sort(Comparator.comparing(Course::getCourseName));
                ClubFrameView.syncState(courseList.size());
                CSVUtil.write(path, courseList);
            });
        }
        // same above
        if("promotion".equals(type)) {
            CompletableFuture.runAsync(() -> {
                ClubFrameView.syncState(promotionList.size());
                CSVUtil.write(path, promotionList);
            });
        }
        // same above
        if("visitor".equals(type)) {
            CompletableFuture.runAsync(() -> {
                ClubFrameView.syncState(visitorDtoList.size());
                CSVUtil.write(path, visitorDtoList);
            });
        }
    }

    /**
     * Initialize a timer to do the work each 1 hour
     */
    private void refreshJob() {
        Logger.info("Data source will auto refresh per hour");
        Logger.info("Data source will backup per hour");
        Timer timer = new Timer((int) TimeUnit.HOURS.toMillis(1), this);
        timer.start();
    }

    /**
     * The timer will execute the actionPerformed method
     *
     * @param actionEvent the action event
     */
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            // call the refresh method
            refresh();
        } catch(Exception e) {
            // cover the error
            Logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, """
                    Data source refreshed failed!
                                        
                    Suggestion:                    
                    1. Please check the output of the console. 
                    """, "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            // call the back-up method
            backup();
        } catch(Exception e) {
            // cover the error
            Logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, """
                    Data source backup failed!
                                        
                    Suggestion:                  
                    1. Please check the output of the console. 
                    """, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * The refresh job we need to execute
     */
    private void refresh() {
        Logger.info("======== Data source start refreshing ========");
        int count = 0; // store each time of the updated members in a refresh
        CustomerDto customerDto = new CustomerDto();
        for(CustomerDto e : customerList) {
            Date expireTime = e.getExpireTime();
            // if the expiry time is over today, it is active
            if(DateUtil.isAfter(expireTime, LocalDate.now())) {
                // if it is not expired, and it is active too, continue to next round
                if(CustomerSateEnum.ACTIVE.getName().equals(e.getState())) {
                    continue;
                }
                // sometime, there are few errors, although it is not expired but modification but some unknown way,
                // therefore need to be correct
                e.setState(CustomerSateEnum.ACTIVE.getName());
            } else {
                // if is expired today set to expired state and count the number of update account
                if(CustomerSateEnum.ACTIVE.getName().equals(e.getState())) {
                    e.setState(CustomerSateEnum.EXPIRED.getName());
                    count++;
                }
            }
        }
        // only need to output the result if there are some updates happen
        if(count > 0) {
            Logger.info("Update " + count + " account this time");
            broadcast(customerDto, DataManipulateEnum.UPDATE);
            save(DefaultDataConstant.CUSTOMER_CSV_PATH, "customer");
        }
        Logger.info("======== Data source refreshed successfully ========");
    }

    // the steps of the backup function
    private void backup() {
        Logger.info("******** Data source start backup ********");
        CSVUtil.backup(DefaultDataConstant.CUSTOMER_CSV_PATH, customerList);
        CSVUtil.backup(DefaultDataConstant.ROLE_CSV_PATH, roleList);
        CSVUtil.backup(DefaultDataConstant.COURSE_CSV_PATH, courseList);
        CSVUtil.backup(DefaultDataConstant.CONSUMPTION_CSV_PATH, consumptionList);
        CSVUtil.backup(DefaultDataConstant.PROMOTION_CSV_PATH, promotionList);
        CSVUtil.backup(DefaultDataConstant.VISITOR_CSV_PATH, visitorDtoList);
        Logger.info("******** Data source backup successfully ********");
    }
}
