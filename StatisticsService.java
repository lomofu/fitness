import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author Jiaqi Fu
 * <p>
 * This class deals with business logic related to data statistics records
 */
public final class StatisticsService {

    /**
     * This method get some data statistics
     *
     * @return statistics type
     */
    public static Statistics get() {
        int users = DataSource.getCustomerList().size();
        int visitors = DataSource.getVisitorDtoList()
                .stream()
                .filter(e -> DateUtil.isEqual(e.getDate(), LocalDate.now()))
                .map(VisitorDto::getCount)
                .findFirst()
                .orElse(0);
        long activeUser = calculateActiveUsers();
        int courses = DataSource.getCourseList().size();
        int promotions = DataSource.getPromotionList().size();
        String fees = calculateTurnover();

        Statistics statistics = new Statistics();
        statistics.setUsers(users);
        statistics.setActiveUsers((int) activeUser);
        statistics.setFees(fees);
        statistics.setVisitors(visitors);
        statistics.setCourses(courses);
        statistics.setPromotionCode(promotions);
        return statistics;
    }

    /**
     * This method is used to calculate the number of active members
     *
     * @return the count number of active members
     */
    public static long calculateActiveUsers() {
        List<CustomerDto> customerList = DataSource.getCustomerList();
        return customerList.stream()
                .filter(e -> e.getState().equals(CustomerSateEnum.ACTIVE.getName()))
                .distinct()
                .count();
    }

    /**
     * This method is used to calculate the turnover
     *
     * @return the turnover (String type)
     */
    public static String calculateTurnover() {
        BigDecimal reduce = DataSource.getConsumptionList()
                .stream()
                .filter(e -> e.createTime().equals(DateUtil.format(new Date())))
                .map(e -> new BigDecimal(e.fees()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return reduce.toString();
    }
}
