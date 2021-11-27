package constant;

/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 04:09
 */
public final class UIConstant {
  public static final String SYSTEM_NAME = "CLUB OS";
  public static final String DATE_FORMAT = "dd/MM/yyyy";

  public static final int MENU_ICON_SIZE = 24;
  public static final String[][] MENU_LIST = {
    {"Home", "code/assets/leftmenu/home.png"},
    {"MemberShip", "code/assets/leftmenu/membership.png"},
    {"Consumption", "code/assets/leftmenu/money.png"}
  };

  public static final String[][] MEMBER_TOOL_LIST = {
    {"Add", "code/assets/table/plus.png"},
    {"Edit", "code/assets/table/edit.png"},
    {"Remove", "code/assets/table/remove.png"},
    {"Filter", "code/assets/table/filter.png"},
    {"Close", "code/assets/table/cancel.png"},
    {"Search", "code/assets/table/search.png"},
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
    "Expire Time"
  };
  public static final int[] MEMBER_SEARCH_FILTER_COLUMNS = {0, 1, 2, 6};

  public static final String[] GENDER_LIST = {"Male", "Female", "Unknown"};
  public static final String[] DEFAULT_MEMBERS = {"individual member", "family member"};
  public static final String[] DEFAULT_DURATION = {"1", "3", "6", "12"};
  public static final String[] CONSUMPTION_COLUMNS = {
    "Order ID", "Consumer", "Member ID", "Create Time", "Fees"
  };

  private UIConstant() {}
}
