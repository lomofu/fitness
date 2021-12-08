package constant;

import bean.Course;
import bean.Role;

/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 04:09
 */
public final class UIConstant {
  public static final String SYSTEM_NAME = "CLUB OS";
  public static final String DATE_FORMAT = "dd/MM/yyyy";

  public static final String CUSTOMER_CSV_PATH = "customerlist.csv";
  public static final String CONSUMPTION_CSV_PATH = "consumptionlist.csv";
  public static final String ROLE_CSV_PATH = "rolelist.csv";
  public static final String COURSE_CSV_PATH = "courselist.csv";
  public static final String PROMOTION_CSV_PATH = "promotionlist.csv";

  public static final Role[] DEFAULT_MEMBERS = {
    new Role("Individual Member", "36", "36", "36", "36", "true", "true", "Yoga|Aerobics"),
    new Role("Family Member", "60", "60", "60", "60", "true", "true", "Yoga|Aerobics"),
  };

  public static final Course[] DEFAULT_COURSES = {new Course("Yoga"), new Course("Aerobics")};

  public static final int MENU_ICON_SIZE = 24;
  public static final String[][] MENU_LIST = {
    {"Home", "assets/leftmenu/home.png"},
    {"Membership", "assets/leftmenu/membership.png"},
    {"Consumption", "assets/leftmenu/money.png"},
    {"Role", "assets/leftmenu/user.png"},
    {"Course", "assets/leftmenu/bicep.png"},
    {"Promotion", "assets/leftmenu/promotion.png"}
  };

  public static final String[][] TABLE_TOOL_LIST = {
    {"Add", "assets/table/plus.png"},
    {"Edit", "assets/table/edit.png"},
    {"Remove", "assets/table/remove.png"},
    {"Filter", "assets/table/filter.png"},
    {"Close", "assets/table/cancel.png"},
    {"Search", "assets/table/search.png"},
  };

  public static final String[] MEMBER_COLUMNS = {
    "Member ID",
    "First Name",
    "Last Name",
    "Date Of Birth",
    "Gender",
    "Home Address",
    "Phone Number",
    "Membership",
    "Health",
    "Start Date",
    "Expire Time",
    "Parent Id",
    "State"
  };
  public static final String[] MEMBER_COLUMNS_SELECTED_MODE = {
    "Member ID", "First Name", "Last Name", "Select"
  };
  public static final int[] MEMBER_COLUMNS_SELECTED_MODE_FILTER_COLUMNS = {0, 1, 2};
  public static final int[] MEMBER_SEARCH_FILTER_COLUMNS = {0, 1, 2, 6};
  public static final String[] MEMBER_GENDER_LIST = {"Male", "Female", "Unknown"};
  public static final String[] MEMBER_DEFAULT_DURATION = {"1", "3", "6", "12"};

  public static final String[] CONSUMPTION_COLUMNS = {
    "Order ID", "Consumer", "Member ID", "Create Time", "Fees"
  };
  public static final int[] CONSUMPTION_SEARCH_FILTER_COLUMNS = {0, 1, 2};

  public static final String[] ROLE_COLUMNS = {
    "Role ID",
    "Role Name",
    "Fees of 1 Month",
    "Fees of 3 Months",
    "Fees of 6 Months",
    "Fees of 12 Months",
    "Gym",
    "Swimming Pool",
    "Course list"
  };
  public static final int[] ROLE_SEARCH_FILTER_COLUMNS = {0, 1};

  public static final String[] COURSE_COLUMNS = {
    "Course ID", "Course Name",
  };

  public static final String[] COURSE_COLUMNS_SELECTED_MODE = {
    "Course ID", "Course Name", "Select"
  };

  public static final int[] COURSE_SEARCH_FILTER_COLUMNS = {0, 1};

  public static final String[] PROMOTION_COLUMNS = {
    "Promotion ID", "Promotion Code", "Promotion Type", "Promotion Value"
  };
  public static final String[] PROMOTION_TYPES = {"Vouchers", "Discount"};
  public static final int[] PROMOTION_SEARCH_FILTER_COLUMNS = {0, 1};

  private UIConstant() {}
}
