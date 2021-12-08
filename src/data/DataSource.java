package data;

import bean.*;
import dto.CustomerDto;
import dto.RoleDto;
import utils.CSVUtil;
import utils.DateUtil;
import utils.IDUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:19
 */
public class DataSource {
  private static final BlockingQueue<DataSourceChannelInfo> queue = new LinkedBlockingQueue<>();
  private static List<Course> courseList = new ArrayList<>();
  private static List<RoleDto> roleList = new ArrayList<>();
  private static List<CustomerDto> customerList = new ArrayList<>();
  private static List<Consumption> consumptionList = new ArrayList<>();
  private static List<Promotion> promotionList = new ArrayList<>();

  public static void init() throws IOException {
    readCourseList();
    readRoleList();
    readCustomerList();
    readConsumptionList();
    readPromotionList();
  }

  private static void readCourseList() throws IOException {
    courseList = CSVUtil.read(COURSE_CSV_PATH, Course.class, DEFAULT_COURSES);
  }

  private static void readRoleList() throws IOException {
    roleList = CSVUtil.read(ROLE_CSV_PATH, Role.class, DEFAULT_MEMBERS)
                    .stream()
            .map(e->new RoleDto.Builder()
                    .roleId(e.getRoleId())
                    .roleName(e.getRoleName())
                    .oneMonth(new BigDecimal(e.getOneMonth()))
                    .threeMonth(new BigDecimal(e.getThreeMonth()))
                    .halfYear(new BigDecimal(e.getHalfYear()))
                    .fullYear(new BigDecimal(e.getFullYear()))
                    .gym("true".equals(e.getGym()))
                    .swimmingPool("true".equals(e.getGym()))
                    .courseList(DataSourceHandler.findCoursesByCourseName(e.getCourseList()))
                    .build())
            .collect(Collectors.toList());
  }

  private static void readCustomerList() throws IOException {
    customerList =
        CSVUtil.read(CUSTOMER_CSV_PATH, Customer.class,false).stream()
            .peek(
                e -> {
                  if ("".equals(e.getId())) {
                    e.setId(IDUtil.generateUUID());
                  }
                  if (!"".equals(e.getDateOfBirth())) {
                    e.setDateOfBirth(DateUtil.format(e.getDateOfBirth()));
                  }
                  if("".equals(e.getState())){
                    e.setState(CustomerSate.EXPIRED.getName());
                  }
                  if("".equals(e.getDuration())){
                    e.setDuration("-1");
                  }
                })
                .map(e->new CustomerDto.Builder()
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
                .peek(e->{
                  e.setAge(DateUtil.calculateAge(e.getDateOfBirth()));
                  if(Objects.nonNull(e.getStartDate()) && Objects.nonNull(e.getExpireTime())){
                    boolean before = DateUtil.isBefore(e.getExpireTime(), LocalDate.now());
                    if(before){
                      e.setState(CustomerSate.ACTIVE.getName());
                    }else {
                      e.setState(CustomerSate.EXPIRED.getName());
                    }
                  }
                })
            .collect(Collectors.toList());
  }

  private static void readConsumptionList() throws IOException {
    consumptionList = CSVUtil.read(CONSUMPTION_CSV_PATH, Consumption.class);
  }

  private static void readPromotionList() throws IOException {
    promotionList = CSVUtil.read(PROMOTION_CSV_PATH, Promotion.class);
  }

  public static List<RoleDto> getRoleList(){
    return roleList;
  }

  public static List<Course> getCourseList(){return courseList;}

  public static List<CustomerDto> getCustomerList() {
    return customerList;
  }

  public static List<Consumption> getConsumptionList() {
    return consumptionList;
  }

  public static List<Promotion> getPromotionList(){
    return promotionList;
}

  public static void subscribe(DataSourceChannelInfo channelInfo) {
    if (!queue.offer(channelInfo)) throw new RuntimeException("Cannot subscribe datasource");
  }

  public static <T> void add(T t) {
    if (t instanceof CustomerDto customerDto) {
      customerList.add(customerDto);
      broadcast(customerDto);
      save();
    }

    if(t instanceof RoleDto roleDto){
      roleList.add(roleDto);
      broadcast(roleDto);
      save();
    }
  }

  public static void remove(Customer customer) {
    save();
    broadcast(customer);
  }

  public static void update(Customer customer) {
    // Customer updated =
    //     customerList.stream()
    //         .filter(e -> e.getId().equals(customer.getId()))
    //         .findFirst()
    //         .orElseThrow(() -> new RuntimeException("Cannot find customer info to update"));

    save();
    broadcast(customer);
  }

  private static <T> void broadcast(T t) {
    queue.stream().filter(e->e.gettClass().equals(t.getClass()))
            .forEachOrdered(e->e.getDataSourceChannel().onDataChange(t));

  }

  private static <T,U> void broadcast(T t, U u) {
    queue.forEach(e -> ((BiDataSourceChannel) e).onDataChange(t,u));
  }

  private static void save(String... paths) {}
}
