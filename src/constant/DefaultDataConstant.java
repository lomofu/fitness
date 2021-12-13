package constant;

import bean.Course;
import bean.Role;

import java.math.BigDecimal;

/**
 * @author lomofu
 * @desc
 * @create 10/Dec/2021 01:41
 */
public final class DefaultDataConstant {
    public static final Role[] DEFAULT_MEMBERS = {
            new Role("Individual Member", "36", "32", "30", "28", "true", "true", "Yoga|Aerobics"),
            new Role("Family Member", "60", "54", "50", "45", "true", "true", "Yoga|Aerobics"),
    };

    public static final BigDecimal VISITOR_FEES = new BigDecimal(6);
    public static final Course[] DEFAULT_COURSES = {new Course("Yoga"), new Course("Aerobics")};
    public static final String CUSTOMER_CSV_PATH = "customerlist.csv";
    public static final String CONSUMPTION_CSV_PATH = "consumptionlist.csv";
    public static final String ROLE_CSV_PATH = "rolelist.csv";
    public static final String COURSE_CSV_PATH = "courselist.csv";
    public static final String PROMOTION_CSV_PATH = "promotionlist.csv";
    public static final String VISITOR_CSV_PATH = "visitorlist.csv";

    private DefaultDataConstant() {}
}
