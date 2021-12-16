import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * @author lomofu
 * @desc
 * @create 12/Dec/2021 04:17
 */
public final class StatisticsService {
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

    public static long calculateActiveUsers() {
        List<CustomerDto> customerList = DataSource.getCustomerList();
        return customerList.stream()
                .filter(e -> e.getState().equals(CustomerSateEnum.ACTIVE.getName()))
                .distinct()
                .count();
    }

    public static String calculateTurnover() {
        BigDecimal reduce = DataSource.getConsumptionList()
                .stream()
                .filter(e -> e.createTime().equals(DateUtil.format(new Date())))
                .map(e -> new BigDecimal(e.fees()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return reduce.toString();
    }
}
