package ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import static constant.UIConstant.MENU_LIST;

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
  }

  private int frameWidth;
  private int frameHeight;
  private LeftMenuView leftMenuView;

  public ClubFrameView() throws IOException {
    Container contentPane = this.getContentPane();
    initFrame();
    initLeftMenu();
    initTabs();
    Box box = initStateTool();

    contentPane.add(leftMenuView, BorderLayout.WEST);
    contentPane.add(leftMenuView.getTabbedPane(), BorderLayout.CENTER);
    contentPane.add(box, BorderLayout.SOUTH);
  }

  private Box initStateTool() {
    Box horizontalBox = Box.createHorizontalBox();
    JProgressBar progressBar = new JProgressBar();
    progressBar.setIndeterminate(true);
    progressBar.setMaximumSize(new Dimension(100, 20));
    horizontalBox.add(Box.createHorizontalGlue());
    horizontalBox.add(progressBar);
    return horizontalBox;
  }

  private void initLeftMenu() {
    this.leftMenuView = new LeftMenuView(this.frameWidth, this.frameHeight);
  }

  private void initTabs() {
    this.leftMenuView.addTab(
        MENU_LIST[0][0],
        new HomeView(
            new FlowLayout(FlowLayout.CENTER),
            this.frameWidth,
            this.frameHeight,
            this.leftMenuView.getTabbedPane()));

    this.leftMenuView.addTab(MENU_LIST[1][0], new MemberView(this));
    this.leftMenuView.addTab(MENU_LIST[2][0], new ConsumptionView(this));
    this.leftMenuView.addTab(MENU_LIST[3][0], new RoleView(this));
    this.leftMenuView.addTab(MENU_LIST[4][0], new CourseView(this));
    this.leftMenuView.addTab(MENU_LIST[5][0], new PromotionView(this));
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
