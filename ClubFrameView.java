import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 12:49
 */
public class ClubFrameView extends JFrame {
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

    public static void syncState(int count) {
        statePanel.setVisible(true);
        progressBar.setMaximum(count);
        progressBar.setMinimum(0);
        progressBar.setValue(0);
    }

    public static void updateProgressBar() {
        var i = progressBar.getValue();
        i += i;
        progressBar.setValue(i);
    }

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
