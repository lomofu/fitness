package ui;

import bean.Customer;
import component.MyCard;
import component.MyDatePicker;
import data.DataSource;
import utils.UUIDUtil;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Objects;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 24/Nov/2021 01:05
 */
public class AddMemberDialogView extends JDialog {
  private static final int X_GAP = 20;
  private static final int Y_GAP = 20;

  private final JLabel memberIdLabel = new JLabel("Member ID");
  private final JLabel firstNameLabel = new JLabel("First Name");
  private final JLabel lastNameLabel = new JLabel("Last Name");
  private final JLabel dateOfBirthLabel = new JLabel("Date Of Birth");
  private final JLabel genderLabel = new JLabel("Gender");
  private final JLabel homeAddressLabel = new JLabel("Home Address");
  private final JLabel phoneNumberLabel = new JLabel("Phone Number");
  private final JLabel healthConditionLabel = new JLabel("Health Condition");
  private final JLabel membershipLabel = new JLabel("Membership");
  private final JLabel startDateLabel = new JLabel("Start Date");
  private final JLabel durationDateLabel = new JLabel("Duration(Month)");
  private final JLabel codeLabel = new JLabel("Promotion Code");

  private final JTextField memberIDTextField = new JTextField(20);
  private final JTextField firstNameTextField = new JTextField(20);
  private final JTextField lastNameTextField = new JTextField(20);
  private final MyDatePicker dateOfBirthTextField =
      new MyDatePicker(1940, LocalDate.now().getYear());
  private final JComboBox<String> genderComBox = new JComboBox<>(GENDER_LIST);
  private final JTextArea homeAddressTextField = new JTextArea(4, 20);
  private final JScrollPane jScrollPaneWithHomeAddress = new JScrollPane(homeAddressTextField);
  private final JTextField phoneNumberTextField = new JTextField(20);
  private final JTextArea healthConditionTextField = new JTextArea(4, 20);
  private final JScrollPane jScrollPaneWithHealthCondition =
      new JScrollPane(healthConditionTextField);
  private final JComboBox<String> memberComBox = new JComboBox<>(DEFAULT_MEMBERS);
  private final JTextField startDateTextField = new JTextField(20);
  private final JComboBox<String> durationComboBox = new JComboBox<>(DEFAULT_DURATION);
  private final JTextField codeTextField = new JTextField(20);

  private final MyCard myCard = new MyCard("Member Info", true);
  private final JLabel yourAgeLabel = new JLabel("Your Age", JLabel.LEFT);
  private final JLabel yourAgeValue = new JLabel("0", JLabel.RIGHT);
  private final JLabel expireTimeLabel = new JLabel("Expire Time");
  private final JLabel expireTimeValue = new JLabel("0");
  private final JLabel discountLabel = new JLabel("Discount");
  private final JLabel discountValue = new JLabel("0");
  private final JLabel membershipFeesLabel = new JLabel("Membership Fees    ");
  private final JLabel membershipFeesValue = new JLabel("0 GBP");

  private final JButton submit = new JButton("Submit");
  private final JButton reset = new JButton("Reset");
  private final SpringLayout springLayout = new SpringLayout();

  public AddMemberDialogView(Frame owner) {
    initDialogSetting(owner);
    initFormElements();
    JPanel panel = initPanel();
    initListener();
    this.add(panel);
  }

  public static Customer showDig(Frame owner) {
    AddMemberDialogView addMemberDialogView = new AddMemberDialogView(owner);
    addMemberDialogView.setVisible(true);
    return new Customer();
  }

  private void initDialogSetting(Frame owner) {
    this.setTitle("New Member");
    this.setSize(600, 800);
    this.setPreferredSize(new Dimension(600, 800));
    this.setResizable(true);
    this.setLocationRelativeTo(owner);
  }

  private void initFormElements() {
    memberIDTextField.setEditable(false);
    memberIDTextField.setFocusable(false);
    memberIDTextField.setBackground(new Color(0, 0, 0, 0));
    memberIDTextField.setBorder(null);
    memberIDTextField.setText(UUIDUtil.generate());

    submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setForeground(Color.RED);
  }

  private void initListener() {
    submit.addActionListener(
        e -> {
          Customer customer =
              new Customer.Builder()
                  .id(memberIDTextField.getText())
                  .firstName(firstNameTextField.getText())
                  .lastName(lastNameTextField.getText())
                  .dateOfBirth("")
                  .gender(GENDER_LIST[genderComBox.getSelectedIndex()])
                  .homeAddress(homeAddressTextField.getText())
                  .phoneNumber(phoneNumberTextField.getText())
                  .healthCondition(healthConditionTextField.getText())
                  .type((String) memberComBox.getSelectedItem())
                  .startDate(startDateTextField.getText())
                  .expireTime(expireTimeValue.getText())
                  .duration(
                      Integer.parseInt(
                          (String) Objects.requireNonNull(durationComboBox.getSelectedItem())))
                  .fees(membershipFeesValue.getText())
                  .build();

          DataSource.add(customer);
          this.dispose();
        });
  }

  private JPanel initPanel() {
    initComponentsSetting();
    JPanel panel = new JPanel(springLayout);
    initSpringLayout(panel);
    panel.add(memberIdLabel);
    panel.add(firstNameLabel);
    panel.add(lastNameLabel);
    panel.add(dateOfBirthLabel);
    panel.add(genderLabel);
    panel.add(homeAddressLabel);
    panel.add(phoneNumberLabel);
    panel.add(healthConditionLabel);
    panel.add(membershipLabel);
    panel.add(startDateLabel);
    panel.add(durationDateLabel);
    panel.add(codeLabel);

    panel.add(memberIDTextField);
    panel.add(firstNameTextField);
    panel.add(lastNameTextField);
    panel.add(dateOfBirthTextField);
    panel.add(genderComBox);
    panel.add(jScrollPaneWithHomeAddress);
    panel.add(phoneNumberTextField);
    panel.add(jScrollPaneWithHealthCondition);
    panel.add(memberComBox);
    panel.add(startDateTextField);
    panel.add(durationComboBox);
    panel.add(myCard);
    panel.add(codeTextField);

    panel.add(submit);
    panel.add(reset);
    return panel;
  }

  private void initComponentsSetting() {
    healthConditionTextField.setLineWrap(true);
    healthConditionTextField.setWrapStyleWord(true);

    homeAddressTextField.setLineWrap(true);
    homeAddressTextField.setWrapStyleWord(true);

    discountLabel.setForeground(Color.RED);
    discountLabel.setVisible(false);
    discountValue.setForeground(Color.RED);
    discountValue.setVisible(false);

    Font yourAgeValueFont = yourAgeValue.getFont();
    yourAgeValue.setFont(
        new Font(yourAgeValueFont.getFontName(), Font.BOLD, yourAgeValueFont.getSize()));

    Font expireTimeValueFont = expireTimeValue.getFont();
    expireTimeValue.setFont(
        new Font(expireTimeValueFont.getFontName(), Font.BOLD, expireTimeValueFont.getSize()));

    Font discountValueFont = discountValue.getFont();
    discountValue.setFont(
        new Font(discountValueFont.getFontName(), Font.BOLD, discountValueFont.getSize()));

    Font membershipFeesValueFont = membershipFeesValue.getFont();
    membershipFeesValue.setFont(
        new Font(
            membershipFeesValueFont.getFontName(), Font.BOLD, membershipFeesValueFont.getSize()));

    initMemberInfoCard();
  }

  private void initSpringLayout(JPanel panel) {
    Spring childWidth =
        Spring.sum(
            Spring.sum(Spring.width(memberIdLabel), Spring.width(memberIDTextField)),
            Spring.constant(X_GAP));

    initMemberIdRow(panel, childWidth);

    initChildRow(memberIdLabel, firstNameLabel, firstNameTextField);
    initChildRow(firstNameLabel, lastNameLabel, lastNameTextField);
    initChildRow(lastNameLabel, dateOfBirthLabel, dateOfBirthTextField);
    initChildRow(dateOfBirthLabel, genderLabel, genderComBox);
    initChildRow(genderLabel, phoneNumberLabel, phoneNumberTextField);
    initChildRow(phoneNumberLabel, homeAddressLabel, jScrollPaneWithHomeAddress);
    initChildMultiRow(
        homeAddressLabel,
        jScrollPaneWithHomeAddress,
        healthConditionLabel,
        jScrollPaneWithHealthCondition);
    initChildMultiRow(
        healthConditionLabel, jScrollPaneWithHealthCondition, membershipLabel, memberComBox);

    initChildRow(membershipLabel, startDateLabel, startDateTextField);
    initChildRow(startDateLabel, durationDateLabel, durationComboBox);
    initChildRow(durationDateLabel, codeLabel, codeTextField);

    springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, codeLabel);
    springLayout.putConstraint(
        SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, codeTextField);

    springLayout.putConstraint(SpringLayout.SOUTH, submit, -Y_GAP, SpringLayout.SOUTH, panel);
    springLayout.putConstraint(SpringLayout.NORTH, reset, 0, SpringLayout.NORTH, submit);
    springLayout.putConstraint(SpringLayout.WEST, reset, X_GAP, SpringLayout.EAST, submit);
  }

  private void initMemberIdRow(JPanel panel, Spring childWidth) {
    // HORIZONTAL_CENTER
    springLayout.putConstraint(
        SpringLayout.WEST,
        memberIdLabel,
        -childWidth.getValue() / 2,
        SpringLayout.HORIZONTAL_CENTER,
        panel);
    springLayout.putConstraint(SpringLayout.NORTH, memberIdLabel, 20, SpringLayout.NORTH, panel);

    springLayout.putConstraint(
        SpringLayout.NORTH, memberIDTextField, 0, SpringLayout.NORTH, memberIdLabel);
    springLayout.putConstraint(
        SpringLayout.WEST, memberIDTextField, 20, SpringLayout.EAST, memberIdLabel);
  }

  private void initChildRow(JLabel refer, JLabel label, Component component) {
    // align with refer
    springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
    springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

    springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
    springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
  }

  private void initChildMultiRow(
      JLabel referLab, Component refer, JLabel label, Component component) {
    springLayout.putConstraint(SpringLayout.NORTH, component, Y_GAP, SpringLayout.SOUTH, refer);
    springLayout.putConstraint(SpringLayout.WEST, component, 0, SpringLayout.WEST, refer);

    springLayout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, component);
    springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, referLab);
  }

  private void initMemberInfoCard() {
    Box box = getHBox(yourAgeLabel, yourAgeValue);
    Box box1 = getHBox(expireTimeLabel, expireTimeValue);
    Box box2 = getHBox(discountLabel, discountValue);
    Box box3 = getHBox(membershipFeesLabel, membershipFeesValue);

    Box verticalBox = Box.createVerticalBox();
    verticalBox.add(box);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box1);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box2);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box3);

    int myCardWidth =
        Spring.sum(
                Spring.sum(Spring.width(durationDateLabel), Spring.width(memberIDTextField)),
                Spring.constant(X_GAP))
            .getValue();
    int myCardHeight =
        Spring.sum(Spring.height(verticalBox), Spring.constant(Y_GAP * 2)).getValue();

    myCard.setCSize(myCardWidth, myCardHeight);
    myCard.addC(verticalBox);
  }

  private Box getHBox(JLabel label, JLabel value) {
    Box box = Box.createHorizontalBox();
    box.add(label);
    box.add(Box.createHorizontalGlue());
    box.add(value);
    return box;
  }
}
