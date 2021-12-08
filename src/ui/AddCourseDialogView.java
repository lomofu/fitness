package ui;

import utils.IDUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 07/Dec/2021 03:56
 */
public class AddCourseDialogView extends JDialog {
  private static final int X_GAP = 20;
  private static final int Y_GAP = 20;

  private final JLabel courseIdLabel = new JLabel("Course ID");
  private final JLabel courseNameLabel = new JLabel("Course Name");

  private final JTextField courseIdTextField = new JTextField(20);
  private final JTextField courseNameTextField = new JTextField(20);

  private final JButton submit = new JButton("Submit");
  private final JButton reset = new JButton("Reset");
  private final SpringLayout springLayout = new SpringLayout();

  public AddCourseDialogView(Frame owner) {
    initDialogSetting(owner);
    initFormElements();
    JPanel panel = initPanel();
    initListener();
    this.add(panel);
  }

  public static void showDig(Frame owner) {
    AddCourseDialogView addCourseDialogView = new AddCourseDialogView(owner);
    addCourseDialogView.setVisible(true);
  }

  private void initDialogSetting(Frame owner) {
    this.setTitle("New Course");
    this.setSize(600, 200);
    this.setPreferredSize(new Dimension(600, 200));
    this.setResizable(true);
    this.setLocationRelativeTo(owner);
  }

  private void initFormElements() {
    courseIdTextField.setEditable(false);
    courseIdTextField.setFocusable(false);
    courseIdTextField.setBackground(new Color(0, 0, 0, 0));
    courseIdTextField.setBorder(null);
    courseIdTextField.setText(IDUtil.generateId("C"));

    submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setForeground(Color.RED);
  }

  private JPanel initPanel() {
    JPanel panel = new JPanel(springLayout);
    Box box = getBox(submit, reset);
    initSpringLayout(panel, box);

    panel.add(courseIdLabel);
    panel.add(courseNameLabel);
    panel.add(courseIdTextField);
    panel.add(courseNameTextField);
    panel.add(box);
    return panel;
  }

  private Box getBox(Component component, Component component1) {
    Box box = Box.createHorizontalBox();
    box.add(component);
    box.add(Box.createHorizontalStrut(X_GAP));
    box.add(component1);
    return box;
  }

  private void initSpringLayout(JPanel panel, Box box) {
    Spring childWidth =
        Spring.sum(
            Spring.sum(Spring.width(courseIdLabel), Spring.width(courseIdTextField)),
            Spring.constant(X_GAP));

    initRoleIdRow(panel, childWidth);
    initChildRow(courseIdLabel, courseNameLabel, courseNameTextField);

    springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, courseNameLabel);
    springLayout.putConstraint(SpringLayout.SOUTH, box, -Y_GAP, SpringLayout.SOUTH, panel);
  }

  private void initRoleIdRow(JPanel panel, Spring childWidth) {
    springLayout.putConstraint(
        SpringLayout.WEST,
        courseIdLabel,
        -childWidth.getValue() / 2,
        SpringLayout.HORIZONTAL_CENTER,
        panel);
    springLayout.putConstraint(SpringLayout.NORTH, courseIdLabel, Y_GAP, SpringLayout.NORTH, panel);

    springLayout.putConstraint(
        SpringLayout.NORTH, courseIdTextField, 0, SpringLayout.NORTH, courseIdLabel);
    springLayout.putConstraint(
        SpringLayout.WEST, courseIdTextField, X_GAP, SpringLayout.EAST, courseIdLabel);
  }

  private void initChildRow(JLabel refer, JLabel label, Component component) {
    springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
    springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

    springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
    springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
  }

  private void initListener() {}
}
