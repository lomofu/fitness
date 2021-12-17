/**
 * @author lomofu
 * <p>
 * This class defines the common constants in the system UI
 */

public final class UIConstant {
    public static final String SYSTEM_NAME = "CLUB OS";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    public static final int MENU_ICON_SIZE = 24;

    public static final String[][] MENU_LIST = {
            {"Home", ImageConstant.HOME},
            {"Membership", ImageConstant.MEMBERSHIP},
            {"Consumption", ImageConstant.MONEY},
            {"Role", ImageConstant.USER},
            {"Course", ImageConstant.BICEP},
            {"Promotion", ImageConstant.PROMOTION},
            {"Visitor", ImageConstant.VISITOR}
    };

    public static final String[][] TABLE_TOOL_LIST = {
            {"Add", ImageConstant.PLUS},
            {"Edit", ImageConstant.EDIT},
            {"Remove", ImageConstant.REMOVE},
            {"Filter", ImageConstant.FILTER},
            {"Close", ImageConstant.CANCEL},
            {"Search", ImageConstant.SEARCH},
            {"Refresh", ImageConstant.REFRESH},
            {"Consumption", ImageConstant.DOLLAR},
            {"Renew", ImageConstant.RENEWABLE_ENERGY},
            {"Help", ImageConstant.QUESTION},
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
            "Expire Date",
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

    public static final String[] VISITOR_COLUMNS = {
            "Date", "Count",
    };

    public static final int[] VISITOR_SEARCH_FILTER_COLUMNS = {0};

    public static final String[] HELP_INFO = {
            """
            How to use the membership table?
            
            Below the headings there is a row of function buttons and a search box, which basically covers all the functions of the entire form.
            
            - Add
            \tThe Add button allows you to add new members to your system, while the new member added may generate a consumption record, 
            \twhich will of course be recorded in the consumption list.
            
            - Edit
            \tIf a member's personal information needs to be changed, then this button can be used to do so. It is important to note that 
            \tchanges can only be made to a basic piece of personal information, and that changing the type of membership (for example from 
            \tindividual to family or family to other) is not permitted through edit as it involves a number of complex operations. 
            
            \tOf course, if this is required, we will need to remove the membership type of the existing customer (optional, depending on 
            \twhether you wish to have overlapping memberships) and then add the new membership type.
            
            - Remove
            \tUse this function with care as it will remove pre-existing member information from the system.
            
            \t** Special cover!! **
            \t"
            \t For family members, if the user is the primary member, then when it is deleted, all other 
            \t family members (with parent id equal to its member id) will be removed.
            \t"
            
            - Consumption
            \tThis button is used to view the member's purchase history. If the full name is visitor, the member has brought a child under 12 
            \tyears of age into the gym as a guardian.
         
            - Refresh
            \tThis button is not normally used, if there is no error in the system, the data you see is real-time, but sometimes if there is some 
            \tunknown error in the ui, or the data is not quite correct, use this button to synchronize the data, of course at the same time for 
            \tthe selected row we will also reset
            
            - Renew
            \tWhen a member needs to perform a renewal operation, click on this button. There are three scenarios here:
            \t\t1. When the member is active at the time of renewal, the expiry time will be added to the original time, but the start time will not be affected.
            \t\t2. When a member is in an expired state at the time of renewal, the start time will start directly from today and the expiry time will depend on 
            \t\tthe zone you have chosen.
            \t\t3. For some reason, the user does not have a membership type, it is not possible to perform a renewal operation. In this case you will need to 
            \t\ttransfer the information to a new account via the transfer function in edit and then renew the membership.
            
            - Filters
            \tWhen we need some specific criteria to filter the data, we can click on this filtered chart, then the filter panel will appear and you 
            \tcan filter according to the criteria you need.
            """,
            """
            What is the consumption table?
            
            The consumption table mainly records the user's consumption information, which may be generated by the membership renewal operation or by the visitor's consumption.
            """,
            """
            What is the role table?
            
            We abstract the membership types into roles, so that each role corresponds to a membership type. The system defaults to two role types, individual and family members. 
            We can roughly divide them into three categories, individual membership, family membership and custom membership.
            
            - Individual members
            \tIndividual members have an age limit of 12 years or older, except for the options that can be adjusted in the table.
            
            - Family members
            \tFamily members have an age limit of 18 years or older, except for the options that can be adjusted in the table. In addition, only family members can have a primary 
            \tand secondary card relationship.
            
            Customised membership
            \tThe age limit for individual members is 18 years or older, except for the options that can be adjusted in the table. 
            """,
            """
            What is the course table?
            
            The list of courses mainly serves the role list, as we take into account that different roles may have restrictions on the courses. This is why the course list is split out 
            and managed separately. Any operations performed in this table will affect the course selection operations performed in the role list.
            """,
            """
            What is the promotion table?
            
            Sometimes we make adjustments to fees to attract and stimulate spending. promotion codes can be used for cash offsets and discounts.
            """,
            """
            What is the visitor table?
            
            This table will record the number of visitors per day
            """


    };

    private UIConstant() {
        // do nothing
    }
}
