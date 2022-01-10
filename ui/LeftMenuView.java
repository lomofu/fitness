package ui;

import component.MyImageIcon;
import component.MyTabbedPanel;
import constant.ColorConstant;
import constant.UIConstant;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author lomofu
 * <p>
 * This class set the left menu panel
 */
public class LeftMenuView extends JPanel {
    private final List<MyMenuButton> MY_BUTTONS = new ArrayList<>(UIConstant.MENU_LIST.length);

    private MyTabbedPanel tabbedPane;
    private int panelWidth;
    private int panelHeight;

    public LeftMenuView(int frameWidth, int frameHeight) {
        // set the panel size
        panelWidth = (int) (frameWidth * 0.15);
        panelHeight = (int) (frameHeight * 0.15);

        // set the layout of panel as gridlayout
        this.setLayout(new GridLayout(10, 1));
        this.setBackground(ColorConstant.PANTONE433C);
        this.setPreferredSize(new Dimension(panelWidth, panelHeight));

        initTabs();
        initButtons();

        this.add(initLogo());
        this.add(Box.createVerticalStrut(1));
        this.MY_BUTTONS.forEach(this::add);
    }

    // set the logo
    private JLabel initLogo() {
        JLabel logoLabel = new JLabel(UIConstant.SYSTEM_NAME, JLabel.CENTER);
        logoLabel.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        return logoLabel;
    }

    // set the button of each option
    private void initButtons() {
        for (String[] strings : UIConstant.MENU_LIST) {
            MY_BUTTONS.add(
                    new MyMenuButton(
                            strings[0], MyImageIcon.build(strings[1], UIConstant.MENU_ICON_SIZE, UIConstant.MENU_ICON_SIZE)));
        }

        // loop injection of events
        IntStream.range(0, this.MY_BUTTONS.size())
                .forEachOrdered(
                        i -> MY_BUTTONS.get(i).addActionListener(e -> tabbedPane.setSelectedIndex(i)));
    }

    private void initTabs() {
        this.tabbedPane = new MyTabbedPanel();
    }

    public void addTab(String title, Component component) {
        this.tabbedPane.addTab(title, component);
        tabbedPane.setSelectedIndex(0);
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * This class customize the left menu items including a 32 pix icon, text and interaction
     */
    private static class MyMenuButton extends JButton implements MouseListener {
        public MyMenuButton(String label, ImageIcon imageIcon) throws HeadlessException {
            super(label, imageIcon);
            this.setFont(new java.awt.Font(Font.DIALOG, Font.BOLD, 15));
            this.setBorderPainted(false);
            this.setFocusPainted(true);
            this.setContentAreaFilled(false);
            this.setFocusable(true);
            this.setIconTextGap(20);
            this.setHorizontalAlignment(SwingConstants.LEFT);
            this.setForeground(Color.WHITE);
            this.setMargin(new Insets(0, 50, 0, 0));
            this.addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mousePressed(MouseEvent e) {
            // do nothing
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // do nothing
        }

        /**
         * This method handle the mouse enter event
         *
         * @param e mouse event
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            JButton component = (JButton) e.getComponent();
            component.setForeground(ColorConstant.PANTONE2727C);
            component.setCursor(new Cursor(Cursor.HAND_CURSOR));
            component.setBorderPainted(true);
            component.setFont(new Font(Font.DIALOG, Font.BOLD, this.getFont().getSize()));
        }

        /**
         * This method handle the mouse exit event
         *
         * @param e mouse event
         */
        @Override
        public void mouseExited(MouseEvent e) {
            JButton component = (JButton) e.getComponent();
            component.setForeground(Color.WHITE);
            component.setBorderPainted(false);
            component.setFont(new Font(Font.DIALOG, Font.PLAIN, this.getFont().getSize()));
        }
    }
}
