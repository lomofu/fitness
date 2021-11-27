package ui;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 12:49
 */
public class ClubFrameView extends JFrame {

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

    contentPane.add(leftMenuView, BorderLayout.WEST);
    contentPane.add(leftMenuView.getTabbedPane(), BorderLayout.CENTER);
  }

  private void initLeftMenu() {
    this.leftMenuView = new LeftMenuView(this.frameWidth, this.frameHeight);
  }

  private void initTabs() {
    this.leftMenuView.addTab(
        "home",
        new HomeView(
            new FlowLayout(FlowLayout.CENTER),
            this.frameWidth,
            this.frameHeight,
            this.leftMenuView.getTabbedPane()));

    this.leftMenuView.addTab("member", new MemberView(this));
    this.leftMenuView.addTab("consumption", new ConsumptionView(this));
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
