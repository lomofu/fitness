package data;

import bean.*;
import constant.CustomerSateEnum;
import constant.DataManipulateEnum;
import constant.DefaultDataConstant;
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

import static constant.DefaultDataConstant.*;
import static constant.UIConstant.MEMBER_GENDER_LIST;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:19
 */
public class DataSource implements ActionListener {
    private static final BlockingQueue<DataSourceChannelInfo> queue = new LinkedBlockingQueue<>();
    private static List<Promotion> promotionList = new ArrayList<>();
    private static List<Course> courseList = new ArrayList<>();
    private static List<RoleDto> roleList = new ArrayList<>();
    private static List<CustomerDto> customerList = new ArrayList<>();
    private static List<Consumption> consumptionList = new ArrayList<>();
    private static List<VisitorDto> visitorDtoList = new ArrayList<>();

    public DataSource() {
        refreshJob();
    }

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
        promotionList = CSVUtil.read(PROMOTION_CSV_PATH, Promotion.class);
    }

    private static void readCourseList() throws IOException {
        courseList = CSVUtil.read(COURSE_CSV_PATH, Course.class, DEFAULT_COURSES);
    }

    private static void readConsumptionList() throws IOException {
        consumptionList = CSVUtil.read(CONSUMPTION_CSV_PATH, Consumption.class);
    }

    private static void readVisitorList() throws IOException {
        visitorDtoList = CSVUtil.read(VISITOR_CSV_PATH, Visitor.class)
                .stream()
                .map(e -> {
                    VisitorDto visitorDto = new VisitorDto();
                    visitorDto.setDate(DateUtil.str2Date(e.getDate()));
                    visitorDto.setCount(Integer.parseInt(e.getCount()));
                    return visitorDto;
                })
                .collect(Collectors.toList());
    }

    private static void readRoleList() throws IOException {
        roleList = CSVUtil.read(ROLE_CSV_PATH, Role.class, DEFAULT_MEMBERS)
                .stream()
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
                CSVUtil.read(CUSTOMER_CSV_PATH, Customer.class, false).stream()
                        .peek(e -> {
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
                                e.setGender(MEMBER_GENDER_LIST[2]);
                            }
                        })
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
                        .peek(e -> {
                            e.setAge(DateUtil.calculateAge(e.getDateOfBirth()));
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

    public static List<RoleDto> getRoleList() {
        return roleList;
    }

    public static List<Course> getCourseList() {return courseList;}

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

    public static void subscribe(DataSourceChannelInfo channelInfo) {
        if(! queue.offer(channelInfo))
            throw new RuntimeException("Cannot subscribe datasource");
    }

    public static <T> void add(T t) {
        if(t instanceof CustomerDto customerDto) {
            customerList.add(customerDto);
            save(CUSTOMER_CSV_PATH, "customer");
            broadcast(customerDto, DataManipulateEnum.INSERT);
        }

        if(t instanceof Consumption consumption) {
            consumptionList.add(consumption);
            save(CONSUMPTION_CSV_PATH, "consumption");
            broadcast(consumption, DataManipulateEnum.INSERT);
        }

        if(t instanceof RoleDto roleDto) {
            roleList.add(roleDto);
            save(ROLE_CSV_PATH, "role");
            broadcast(roleDto, DataManipulateEnum.INSERT);
        }

        if(t instanceof Course course) {
            courseList.add(course);
            save(COURSE_CSV_PATH, "course");
            broadcast(course, DataManipulateEnum.INSERT);
        }

        if(t instanceof Promotion promotion) {
            promotionList.add(promotion);
            save(PROMOTION_CSV_PATH, "promotion");
            broadcast(promotion, DataManipulateEnum.INSERT);
        }

        if(t instanceof VisitorDto visitorDto) {
            visitorDtoList.add(visitorDto);
            save(VISITOR_CSV_PATH, "visitor");
            broadcast(visitorDto, DataManipulateEnum.INSERT);
        }

        broadcast(new Statistics(), DataManipulateEnum.INSERT);
    }

    public static <T> void remove(List<T> list) {
        if(list.get(0) instanceof CustomerDto customerDto) {
            for(T t : list) {
                CustomerDto dto = (CustomerDto) t;
                customerList = customerList
                        .stream()
                        .filter(e -> ! e.getId().equals(dto.getId()))
                        .collect(Collectors.toList());
            }
            save(DefaultDataConstant.CUSTOMER_CSV_PATH, "customer");
            broadcast(customerDto, DataManipulateEnum.DELETE);
        }

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

        broadcast(new Statistics(), DataManipulateEnum.DELETE);
    }

    public static <T> void update(T t) {
        if(t instanceof CustomerDto customerDto) {
            broadcast(customerDto, DataManipulateEnum.UPDATE);
            save(CUSTOMER_CSV_PATH, "customer");
        }

        if(t instanceof RoleDto roleDto) {
            broadcast(roleDto, DataManipulateEnum.UPDATE);
            save(ROLE_CSV_PATH, "role");
        }

        if(t instanceof Course course) {
            broadcast(course, DataManipulateEnum.UPDATE);
            save(COURSE_CSV_PATH, "course");
        }

        if(t instanceof VisitorDto visitorDto) {
            broadcast(visitorDto, DataManipulateEnum.UPDATE);
            save(VISITOR_CSV_PATH, "visitor");
        }

        broadcast(new Statistics(), DataManipulateEnum.UPDATE);
    }

    private static <T> void broadcast(T t, DataManipulateEnum dataManipulateEnum) {
        CompletableFuture<?>[] futures = queue.stream().filter(e -> e.gettClass().equals(t.getClass()))
                .map(DataSourceChannelInfo::getDataSourceChannel)
                .map(e -> CompletableFuture.runAsync(() -> e.onDataChange(t, dataManipulateEnum)))
                .toArray(CompletableFuture[]::new);
        CompletableFuture.allOf(futures);
    }

    private static void save(String path, String type) {
        if("customer".equals(type)) {
            CompletableFuture.runAsync(() -> {
                customerList.sort(Comparator.comparing(CustomerDto::getFirstName));
                ClubFrameView.syncState(customerList.size());
                CSVUtil.write(path, customerList);
            });
        }
        if("consumption".equals(type)) {
            CompletableFuture.runAsync(() -> {
                consumptionList.sort(Comparator.comparing(Consumption::orderId).reversed());
                ClubFrameView.syncState(consumptionList.size());
                CSVUtil.write(path, consumptionList);
            });
        }
        if("role".equals(type)) {
            CompletableFuture.runAsync(() -> {
                roleList.sort(Comparator.comparing(RoleDto::getRoleId));
                ClubFrameView.syncState(roleList.size());
                CSVUtil.write(path, roleList);
            });
        }
        if("course".equals(type)) {
            CompletableFuture.runAsync(() -> {
                courseList.sort(Comparator.comparing(Course::getCourseName));
                ClubFrameView.syncState(courseList.size());
                CSVUtil.write(path, courseList);
            });
        }
        if("promotion".equals(type)) {
            CompletableFuture.runAsync(() -> {
                ClubFrameView.syncState(promotionList.size());
                CSVUtil.write(path, promotionList);
            });
        }
        if("visitor".equals(type)) {
            CompletableFuture.runAsync(() -> {
                ClubFrameView.syncState(visitorDtoList.size());
                CSVUtil.write(path, visitorDtoList);
            });
        }
    }

    private void refreshJob() {
        Logger.info("Data source will auto refresh per hour");
        Logger.info("Data source will backup per hour");
        Timer timer = new Timer((int) TimeUnit.HOURS.toMillis(1), this);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            refresh();
        } catch(Exception e) {
            Logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, """
                    Data source refreshed failed!
                                        
                    Suggestion:                    
                    1. Please check the output of the console. 
                    """, "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            backup();
        } catch(Exception e) {
            Logger.error(e.getMessage());
            JOptionPane.showMessageDialog(null, """
                    Data source backup failed!
                                        
                    Suggestion:                  
                    1. Please check the output of the console. 
                    """, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refresh() {
        Logger.info("======== Data source start refreshing ========");
        int count = 0;
        CustomerDto customerDto = new CustomerDto();
        for(CustomerDto e : customerList) {
            Date expireTime = e.getExpireTime();
            if(DateUtil.isAfter(expireTime, LocalDate.now())) {
                if(CustomerSateEnum.ACTIVE.getName().equals(e.getState())) {
                    continue;
                }
                e.setState(CustomerSateEnum.ACTIVE.getName());
            } else {
                if(CustomerSateEnum.ACTIVE.getName().equals(e.getState())) {
                    e.setState(CustomerSateEnum.EXPIRED.getName());
                    count++;
                }
            }
        }
        if(count > 0) {
            Logger.info("Update " + count + " account this time");
            broadcast(customerDto, DataManipulateEnum.UPDATE);
            save(DefaultDataConstant.CUSTOMER_CSV_PATH, "customer");
        }
        Logger.info("======== Data source refreshed successfully ========");
    }

    private void backup() {
        Logger.info("******** Data source start backup ********");
        CSVUtil.backup(CUSTOMER_CSV_PATH, customerList);
        CSVUtil.backup(ROLE_CSV_PATH, roleList);
        CSVUtil.backup(COURSE_CSV_PATH, courseList);
        CSVUtil.backup(CONSUMPTION_CSV_PATH, consumptionList);
        CSVUtil.backup(PROMOTION_CSV_PATH, promotionList);
        CSVUtil.backup(VISITOR_CSV_PATH, visitorDtoList);
        Logger.info("******** Data source backup successfully ********");
    }
}
