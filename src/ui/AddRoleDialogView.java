package ui;

import bean.Course;
import component.MyCard;
import data.DataSource;
import dto.RoleDto;
import utils.IDUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 04:32
 */
public class AddRoleDialogView extends JDialog {
  private static final int X_GAP = 20;
  private static final int Y_GAP = 20;
  private final JLabel roleIdLabel = new JLabel("Role ID");
  private final JLabel roleNameLabel = new JLabel("Role Name");
  private final JLabel feesOf1MonthLabel = new JLabel("Fees of 1 Month");
  private final JLabel feesOf3MonthLabel = new JLabel("Fees of 3 Month");
  private final JLabel feesOf6MonthLabel = new JLabel("Fees of 6 Month");
  private final JLabel feesOf12MonthLabel = new JLabel("Fees of 12 Month");
  private final JLabel gymLabel = new JLabel("Gym");
  private final JLabel swimmingPoolLabel = new JLabel("Swimming Pool");
  private final JLabel courseListLabel = new JLabel("Course List");
  private final JTextField roleIdTextField = new JTextField(20);
  private final JTextField roleNameTextField = new JTextField(20);
  private final JTextField feesOf1MonthTextField = new JTextField(20);
  private final JTextField feesOf3MonthTextField = new JTextField(20);
  private final JTextField feesOf6MonthTextField = new JTextField(20);
  private final JTextField feesOf12TextField = new JTextField(20);
  private final JCheckBox gymCheckBox = new JCheckBox();
  private final JCheckBox swimmingPoolCheckBox = new JCheckBox();
  private final JButton courseListBtn = new JButton("Choose");
  private final MyCard myCard = new MyCard("Course List", true);
  private final JList<String> courseJList = new JList<>();
  private final JScrollPane courseTextScrollPane = new JScrollPane(courseJList);
  private final JButton submit = new JButton("Submit");
  private final JButton reset = new JButton("Reset");
  private final SpringLayout springLayout = new SpringLayout();
  private List<Course> courseList = new ArrayList<>();
  private Frame owner;

  public AddRoleDialogView(Frame owner) {
    initDialogSetting(owner);
    initFormElements();
    JPanel panel = initPanel();
    initListener();
    this.add(panel);
  }

  public static void showDig(Frame owner) {
    AddRoleDialogView addRoleDialogView = new AddRoleDialogView(owner);
    addRoleDialogView.setVisible(true);
  }

  public List<String> getCourseListId() {
    return courseList.stream().map(Course::getCourseId).collect(Collectors.toList());
  }

  public void setCourseList(List<Course> courseList) {
    this.courseList = courseList;
    this.courseJList.setListData(
        courseList.stream().map(Course::getCourseName).toArray(String[]::new));
    this.courseJList.updateUI();
  }

  private void initDialogSetting(Frame owner) {
    this.setTitle("New Role");
    this.setSize(600, 600);
    this.setPreferredSize(new Dimension(600, 600));
    this.setResizable(true);
    this.setLocationRelativeTo(owner);
    this.owner = owner;
  }

  private void initFormElements() {
    roleIdTextField.setEditable(false);
    roleIdTextField.setFocusable(false);
    roleIdTextField.setBackground(new Color(0, 0, 0, 0));
    roleIdTextField.setBorder(null);
    roleIdTextField.setText(IDUtil.generateId("R"));

    courseTextScrollPane.setBorder(null);
    courseTextScrollPane.setBackground(null);
    courseJList.setEnabled(false);
    courseJList.setOpaque(false);

    submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setForeground(Color.RED);
    myCard.addC(courseTextScrollPane);
  }

  private JPanel initPanel() {
    JPanel panel = new JPanel(springLayout);
    Box box = getBox(submit, reset);
    initSpringLayout(panel, box);

    panel.add(roleIdLabel);
    panel.add(roleNameLabel);
    panel.add(feesOf1MonthLabel);
    panel.add(feesOf3MonthLabel);
    panel.add(feesOf6MonthLabel);
    panel.add(feesOf12MonthLabel);
    panel.add(gymLabel);
    panel.add(swimmingPoolLabel);
    panel.add(courseListLabel);

    panel.add(roleIdTextField);
    panel.add(roleNameTextField);
    panel.add(feesOf1MonthTextField);
    panel.add(feesOf3MonthTextField);
    panel.add(feesOf6MonthTextField);
    panel.add(feesOf12TextField);
    panel.add(gymCheckBox);
    panel.add(swimmingPoolCheckBox);
    panel.add(courseListBtn);
    panel.add(myCard);

    panel.add(box);
    return panel;
  }

  private void initSpringLayout(JPanel panel, Box box) {
    Spring childWidth =
        Spring.sum(
            Spring.sum(Spring.width(roleIdLabel), Spring.width(roleIdTextField)),
            Spring.constant(X_GAP));

    initRoleIdRow(panel, childWidth);
    initChildRow(roleIdLabel, roleNameLabel, roleNameTextField);
    initChildRow(roleNameLabel, feesOf1MonthLabel, feesOf1MonthTextField);
    initChildRow(feesOf1MonthLabel, feesOf3MonthLabel, feesOf3MonthTextField);
    initChildRow(feesOf3MonthLabel, feesOf6MonthLabel, feesOf6MonthTextField);
    initChildRow(feesOf6MonthLabel, feesOf12MonthLabel, feesOf12TextField);
    initChildRow(feesOf12MonthLabel, gymLabel, gymCheckBox);
    initChildRow(gymLabel, swimmingPoolLabel, swimmingPoolCheckBox);
    initChildRow(swimmingPoolLabel, courseListLabel, courseListBtn);

    springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, courseListLabel);
    springLayout.putConstraint(SpringLayout.EAST, myCard, 0, SpringLayout.EAST, feesOf12TextField);
    springLayout.putConstraint(
        SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, courseListBtn);

    springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, swimmingPoolLabel);
    springLayout.putConstraint(SpringLayout.SOUTH, box, -Y_GAP, SpringLayout.SOUTH, panel);
  }

  private void initRoleIdRow(JPanel panel, Spring childWidth) {
    springLayout.putConstraint(
        SpringLayout.WEST,
        roleIdLabel,
        -childWidth.getValue() / 2,
        SpringLayout.HORIZONTAL_CENTER,
        panel);
    springLayout.putConstraint(SpringLayout.NORTH, roleIdLabel, Y_GAP, SpringLayout.NORTH, panel);

    springLayout.putConstraint(
        SpringLayout.NORTH, roleIdTextField, 0, SpringLayout.NORTH, roleIdLabel);
    springLayout.putConstraint(
        SpringLayout.WEST, roleIdTextField, X_GAP, SpringLayout.EAST, roleIdLabel);
  }

  private void initChildRow(JLabel refer, JLabel label, Component component) {
    springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
    springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

    springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
    springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
  }

  private Box getBox(Component component, Component component1) {
    Box box = Box.createHorizontalBox();
    box.add(component);
    box.add(Box.createHorizontalStrut(X_GAP));
    box.add(component1);
    return box;
  }

  private void initListener() {
    courseListBtn.addActionListener(e -> AddCourseListDialogView.showDig(this.owner, this));
    submit.addActionListener(
        e -> {
          DataSource.add(
              new RoleDto.Builder()
                  .roleId(roleIdTextField.getText())
                  .roleName(roleNameTextField.getText())
                  .oneMonth(new BigDecimal(feesOf1MonthTextField.getText()))
                  .threeMonth(new BigDecimal(feesOf3MonthTextField.getText()))
                  .halfYear(new BigDecimal(feesOf6MonthTextField.getText()))
                  .fullYear(new BigDecimal(feesOf12TextField.getText()))
                  .courseList(this.courseList)
                  .gym(gymCheckBox.isSelected())
                  .swimmingPool(swimmingPoolCheckBox.isSelected())
                  .build());
          this.dispose();
        });
  }
}
