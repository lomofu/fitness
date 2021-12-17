import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lomofu
 * <p>
 * This class is a main frame of the whole application
 */
public class ClubFrameView extends JFrame {
    // set the bottom tool if there is an IO operation you can see the sync progress bar
    private static Box statePanel = Box.createHorizontalBox();
    private static JProgressBar progressBar = new JProgressBar();

    static {
        JFrame.setDefaultLookAndFeelDecorated(true);
        initStateTool();
    }

    private int frameWidth;
    private int frameHeight;
    private LeftMenuView leftMenuView;

    public ClubFrameView() throws InterruptedException {
        Container contentPane = this.getContentPane();
        initFrame();
        initLeftMenu();
        initTabs();

        contentPane.add(leftMenuView, BorderLayout.WEST);
        contentPane.add(leftMenuView.getTabbedPane(), BorderLayout.CENTER);
        contentPane.add(statePanel, BorderLayout.SOUTH);
        SplashView.dispose();
    }

    /**
     * This method set the length of progress bar
     *
     * @param count
     */
    public static void syncState(int count) {
        statePanel.setVisible(true);
        progressBar.setMaximum(count);
        progressBar.setValue(0);
    }

    /**
     * This method exposure the progress bar
     */
    public static void updateProgressBar() {
        try {
            var i = progressBar.getValue();
            i++;
            if(i>progressBar.getMaximum()){
                progressBar.setValue(0);
            }
            Thread.sleep(500);
            progressBar.setValue(i);
        } catch (InterruptedException e) {
            removeSyncState();
            Logger.error(e.getMessage());
        }
    }

    /**
     * This method finish the progress bar
     */
    public static void removeSyncState() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        statePanel.setVisible(false);
        progressBar.setValue(0);
    }

    private static void initStateTool() {
        progressBar.setStringPainted(true);
        progressBar.setIndeterminate(false);
        progressBar.setString("Sync");
        progressBar.setValue(0);
        progressBar.setMinimum(0);
        progressBar.setMaximumSize(new Dimension(150, 30));
        statePanel.setFocusable(false);
        statePanel.add(Box.createHorizontalGlue());
        statePanel.add(progressBar);
        statePanel.add(Box.createHorizontalStrut(10));
        statePanel.setVisible(false);
    }

    private void initLeftMenu() {
        this.leftMenuView = new LeftMenuView(this.frameWidth, this.frameHeight);
    }

    /**
     * This method initialize the tab of different data statistics view
     */
    private void initTabs() {
        this.leftMenuView.addTab(
                UIConstant.MENU_LIST[0][0],
                new HomeView(this.frameWidth,
                        this.frameHeight,
                        this.leftMenuView.getTabbedPane()));

        this.leftMenuView.addTab(UIConstant.MENU_LIST[1][0], new MemberView(this));
        this.leftMenuView.addTab(UIConstant.MENU_LIST[2][0], new ConsumptionView(this));
        this.leftMenuView.addTab(UIConstant.MENU_LIST[3][0], new RoleView(this));
        this.leftMenuView.addTab(UIConstant.MENU_LIST[4][0], new CourseView(this));
        this.leftMenuView.addTab(UIConstant.MENU_LIST[5][0], new PromotionView(this));
        this.leftMenuView.addTab(UIConstant.MENU_LIST[6][0], new VisitorView(this));
    }

    private void initFrame() {
        this.setTitle("Club Membership System");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.frameWidth = screenSize.width;
        this.frameHeight = screenSize.height;
        this.setSize(this.frameWidth, this.frameHeight);
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
}
