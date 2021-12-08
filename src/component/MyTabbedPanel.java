package component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:35
 */
public class MyTabbedPanel extends JTabbedPane implements ChangeListener {
  private static final boolean SHOW_TABS_HEADER = false;

  public MyTabbedPanel() {
    this.setUI(new MyTabbedPaneUI());
    this.addChangeListener(this);
  }

  @Override
  public void stateChanged(ChangeEvent e) {
    this.setEnabledAt(this.getSelectedIndex(), false);
  }

  private static class MyTabbedPaneUI extends BasicTabbedPaneUI {


    @Override
    protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
      if (SHOW_TABS_HEADER) {
        return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
      } else {
        return -1;
      }
    }

    @Override
    protected void paintTab(
        Graphics g,
        int tabPlacement,
        Rectangle[] rects,
        int tabIndex,
        Rectangle iconRect,
        Rectangle textRect) {
      if (SHOW_TABS_HEADER) {
        super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
      }
    }

    @Override
    protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
      if (SHOW_TABS_HEADER) {
        super.paintContentBorder(g, tabPlacement, selectedIndex);
      }
    }

    @Override
    public int tabForCoordinate(JTabbedPane pane, int x, int y) {
      if (SHOW_TABS_HEADER) {
        return super.tabForCoordinate(pane, x, y);
      } else {
        return -1;
      }
    }
  }
}
