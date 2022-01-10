package component;

import constant.ColorConstant;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class extend the jtabbedPanel with repaint the tabs. The final effect is the tabs on the top will not be paint.
 * And only the select function of the component left.
 */
public class MyTabbedPanel extends JTabbedPane implements ChangeListener {

    public MyTabbedPanel() {
        // set the ui paint with customize
        this.setUI(new MyTabbedPaneUI());
        // add change listener
        this.addChangeListener(this);
        // set background color
        this.setBackground(ColorConstant.PANTONE649C);
        // make the component not has a border
        this.setBorder(null);
    }

    // implement the change listener and switch the index
    @Override
    public void stateChanged(ChangeEvent e) {
        this.setEnabledAt(this.getSelectedIndex(), false);
    }

    // extend the basic tabbed pane ui ti repaint
    private static class MyTabbedPaneUI extends BasicTabbedPaneUI {
        // calculate the table area, default give a number that fill the tab height, therefore, we need make it be minus
        // to avoid to paint
        @Override
        protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
            return - 12;
        }


        // avoid painting the tab
        @Override
        protected void paintTab(Graphics g,
                                int tabPlacement,
                                Rectangle[] rects,
                                int tabIndex,
                                Rectangle iconRect,
                                Rectangle textRect) {
            //do nothing
        }

        // avoid paint content border
        @Override
        protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
            // do nothing
        }

        // return the tab of the coordinate, avoid by pass -1
        @Override
        public int tabForCoordinate(JTabbedPane pane, int x, int y) {
            return - 1;
        }

        // for the tab border, clear the rect to make it not paint
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
