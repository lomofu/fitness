import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * @author Jiaqi Fu
 * <p>
 * This class set the home panel and implement related functions
 * <p>
 * extends@MyPanel: extends abstract panel function
 * implements@DataSourceChannel: let this object be observer to observe the source change
 * and call back the override onchange functions(mainly update the UI).
 * Meanwhile, it will subscrib the data source when home panel init
 */
public class HomeView extends MyPanel implements DataSourceChannel<Statistics> {
    private final Box firstRow = Box.createHorizontalBox();
    private final Box secondRow = Box.createHorizontalBox();
    private JTabbedPane jTabbedPane;
    private int panelWidth;
    private MyTextCard activeUsers;
    private MyTextCard totalMembers;
    private MyTextCard turnover;
    private MyTextCard visitors;
    private MyTextCard courses;
    private MyTextCard promotionCode;

    public HomeView(int frameWidth, int frameHeight, JTabbedPane tabbedPane) {
        this.subscribe(Statistics.class);
        initHomeViewPanel(frameHeight, frameWidth, tabbedPane);

        initComponents();
        this.add(firstRow);
        this.add(secondRow);
    }

    private void initHomeViewPanel(int frameHeight, int frameWidth, JTabbedPane tabbedPane) {
        panelWidth = (int) (frameWidth * 0.8);
        this.jTabbedPane = tabbedPane;
        this.setLayout(new GridLayout(2, 1));
        this.setSize(panelWidth, frameHeight);
        this.setBackground(ColorConstant.PANTONE649C);
    }

    private void initComponents() {
        initFirstBox();
        initSecondBox();
    }

    /**
     * This method creates a box container to hold the statistical content
     */
    private void initFirstBox() {
        Statistics statistics = StatisticsService.get();

        Box verticalBox = Box.createVerticalBox();
        Box labelBox = Box.createHorizontalBox();
        Box cardBox = Box.createHorizontalBox();
        Box cardBox1 = Box.createHorizontalBox();

        initCards(statistics);
        MyLabel label = new MyLabel("Statistics", SwingConstants.LEFT);
        label.setMargin(0, 30, 0, 0);
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 24));

        labelBox.add(label);
        labelBox.add(Box.createHorizontalGlue());

        verticalBox.add(Box.createVerticalStrut(30));
        verticalBox.add(labelBox);
        verticalBox.add(Box.createVerticalStrut(20));

        cardBox.add(Box.createHorizontalStrut(20));
        cardBox.add(visitors);
        cardBox.add(Box.createHorizontalStrut(20));
        cardBox.add(turnover);
        cardBox.add(Box.createHorizontalStrut(20));
        cardBox.add(totalMembers);
        cardBox.add(Box.createHorizontalStrut(20));
        cardBox.add(activeUsers);
        cardBox.add(Box.createGlue());

        verticalBox.add(cardBox);
        verticalBox.add(Box.createVerticalStrut(10));

        cardBox1.add(Box.createHorizontalStrut(20));
        cardBox1.add(courses);
        cardBox1.add(Box.createHorizontalStrut(20));
        cardBox1.add(promotionCode);
        cardBox1.add(Box.createGlue());

        verticalBox.add(cardBox1);
        firstRow.add(verticalBox);
    }

    /**
     * This method init six cards related to corresponding data statistics result
     *
     * @param statistics
     */
    private void initCards(Statistics statistics) {
        turnover = initTextCard("Total Turnover today",
                statistics.getFees(),
                e -> this.jTabbedPane.setSelectedIndex(2));
        turnover.setBackgroundColor(ColorConstant.PANTONE321C);
        turnover.setForegroundColor(Color.WHITE);
        turnover.setBorderColor(Color.WHITE);

        activeUsers = initCountCard("Active Members",
                statistics.getActiveUsers(),
                e -> this.jTabbedPane.setSelectedIndex(1));
        activeUsers.setBackgroundColor(ColorConstant.PANTONE2725C);
        activeUsers.setForegroundColor(Color.WHITE);
        activeUsers.setBorderColor(Color.WHITE);

        totalMembers = initCountCard(
                "Total number of members",
                statistics.getUsers(),
                e -> this.jTabbedPane.setSelectedIndex(1));
        totalMembers.setBackgroundColor(ColorConstant.PANTONE359C);
        totalMembers.setForegroundColor(Color.WHITE);
        totalMembers.setBorderColor(Color.WHITE);

        visitors = initCountCard(
                "Day visitors",
                statistics.getVisitors(),
                e -> this.jTabbedPane.setSelectedIndex(6));
        visitors.setBackgroundColor(ColorConstant.PANTONE170C);
        visitors.setForegroundColor(Color.WHITE);
        visitors.setBorderColor(Color.WHITE);

        courses = initCountCard(
                "Courses",
                statistics.getCourses(),
                e -> this.jTabbedPane.setSelectedIndex(4));
        courses.setBackgroundColor(ColorConstant.PANTONE124C);
        courses.setForegroundColor(Color.WHITE);
        courses.setBorderColor(Color.WHITE);

        promotionCode = initCountCard(
                "Promotion Codes",
                statistics.getPromotionCode(),
                e -> this.jTabbedPane.setSelectedIndex(5));
        promotionCode.setBackgroundColor(ColorConstant.PANTONE190C);
        promotionCode.setForegroundColor(Color.WHITE);
        promotionCode.setBorderColor(Color.WHITE);
    }

    /**
     * This method creates a box container to hold the visitor add function panel
     */
    private void initSecondBox() {
        Box verticalBox = Box.createVerticalBox();
        Box labelBox = Box.createHorizontalBox();
        Box cardBox = Box.createHorizontalBox();

        MyTextCard visitor = initTextCard("",
                "Visitor",
                e -> AddVisitorDialog.showDig(null));
        visitor.setBackgroundColor(ColorConstant.PANTONE1205C);
        visitor.setForegroundColor(Color.WHITE);
        visitor.setBorderColor(Color.WHITE);

        MyLabel label = new MyLabel("Functions", SwingConstants.LEFT);
        MyLabel power = new MyLabel("Powered By Jiaqi Fu", SwingConstants.CENTER);
        power.setEnabled(false);
        label.setMargin(0, 30, 0, 0);
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, 24));

        labelBox.add(label);
        labelBox.add(Box.createHorizontalGlue());

        verticalBox.add(Box.createVerticalStrut(30));
        verticalBox.add(labelBox);
        verticalBox.add(Box.createVerticalStrut(20));

        cardBox.add(Box.createHorizontalStrut(20));
        cardBox.add(visitor);
        cardBox.add(Box.createHorizontalGlue());

        verticalBox.add(cardBox);
        verticalBox.add(Box.createVerticalGlue());
        verticalBox.add(power);
        secondRow.add(verticalBox);
    }

    /**
     * This method is used to set the count card info
     *
     * @param title
     * @param number
     * @param consumer
     *
     * @return countCard
     */
    private MyTextCard initCountCard(String title, int number, Consumer<MouseEvent> consumer) {
        var width = panelWidth / 4;
        MyTextCard countCard = new MyTextCard(title, width, 300, consumer);
        countCard.setMaximumSize(new Dimension(width, 300));
        countCard.setValue(number);
        return countCard;
    }

    /**
     * This method is used to set the text card info
     *
     * @param title
     * @param number
     * @param consumer
     *
     * @return textCard
     */
    private MyTextCard initTextCard(String title, String number, Consumer<MouseEvent> consumer) {
        var width = panelWidth / 4;
        MyTextCard textCard = new MyTextCard(title, width, 300, consumer);
        textCard.setMaximumSize(new Dimension(width, 300));
        textCard.setValue(number);
        return textCard;
    }

    /**
     * This method refresh the data from data source
     */
    private void fetchData() {
        Statistics statistics = StatisticsService.get();
        turnover.setValue(statistics.getFees());
        activeUsers.setValue(statistics.getActiveUsers());
        totalMembers.setValue(statistics.getUsers());
        visitors.setValue(statistics.getVisitors());
        courses.setValue(statistics.getCourses());
        promotionCode.setValue(statistics.getPromotionCode());
    }

    /**
     * This method override the observer hook function if the corresponding data in
     * data source has mutable.
     *
     * @param statistics parameter from data source of
     *                   which consumptions object has been changed(only have value if is a update operation)
     * @param flag       operation type
     */
    @Override
    public void onDataChange(Statistics statistics, DataManipulateEnum flag) {
        fetchData();
    }

    /**
     * This method subscribe data source when the object is init
     *
     * @param statisticsClass Statistics.class
     */
    @Override
    public void subscribe(Class<Statistics> statisticsClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, statisticsClass));
    }
}
