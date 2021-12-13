package component;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

import static constant.ColorConstant.PANTONE649C;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:35
 */
public class MyTabbedPanel extends JTabbedPane implements ChangeListener {

    public MyTabbedPanel() {
        this.setUI(new MyTabbedPaneUI());
        this.addChangeListener(this);
        this.setBackground(PANTONE649C);
        this.setBorder(null);
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        this.setEnabledAt(this.getSelectedIndex(), false);
    }

    private static class MyTabbedPaneUI extends BasicTabbedPaneUI {
        @Override
        protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
            return - 12;
        }

        @Override
        protected void paintTab(Graphics g,
                                int tabPlacement,
                                Rectangle[] rects,
                                int tabIndex,
                                Rectangle iconRect,
                                Rectangle textRect) {}

        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {}

        @Override
        public int tabForCoordinate(JTabbedPane pane, int x, int y) {
            return - 1;
        }

        @Override
        protected void paintTabBorder(Graphics g,
                                      int tabPlacement,
                                      int tabIndex,
                                      int x,
                                      int y,
                                      int w,
                                      int h,
                                      boolean isSelected) {
            g.clearRect(x, y, w, h);
        }
    }
}
