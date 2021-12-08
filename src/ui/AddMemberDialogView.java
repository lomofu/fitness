package ui;

import component.MyCard;
import component.MyDatePicker;
import data.DataSource;
import data.DataSourceHandler;
import dto.CustomerDto;
import dto.RoleDto;
import utils.DateUtil;
import utils.IDUtil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
  private final JComboBox<String> genderComBox = new JComboBox<>(MEMBER_GENDER_LIST);
  private final JTextArea homeAddressTextField = new JTextArea(4, 20);
  private final JScrollPane jScrollPaneWithHomeAddress = new JScrollPane(homeAddressTextField);
  private final JTextField phoneNumberTextField = new JTextField(20);
  private final JTextArea healthConditionTextField = new JTextArea(4, 20);
  private final JScrollPane jScrollPaneWithHealthCondition =
      new JScrollPane(healthConditionTextField);
  private final JComboBox<String> memberComBox =
      new JComboBox<>(DataSourceHandler.findRoleDtoList());
  private final JButton parentIdChoose = new JButton("Choose main");
  private final Box memberBox = Box.createHorizontalBox();
  private final MyDatePicker startDateTextField = new MyDatePicker(1940, LocalDate.now().getYear());
  private final JComboBox<String> durationComboBox = new JComboBox<>(MEMBER_DEFAULT_DURATION);
  private final JTextField codeTextField = new JTextField(20);

  private final MyCard myCard = new MyCard("Member Info", true);
  private final JLabel yourAgeLabel = new JLabel("Your Age  ", JLabel.LEFT);
  private final JLabel yourAgeValue = new JLabel("0", JLabel.RIGHT);
  private final JLabel expireTimeLabel = new JLabel("Expire Time  ");
  private final JLabel expireTimeValue = new JLabel("");
  private final JLabel parentIdLabel = new JLabel("Main MID");
  private final JLabel parentIdValue = new JLabel("");
  private final JLabel discountLabel = new JLabel("Discount");
  private final JLabel discountValue = new JLabel("0");
  private final JLabel membershipFeesLabel = new JLabel("Membership Fees    ");
  private final JLabel membershipFeesValue = new JLabel("0");

  private final JButton submit = new JButton("Submit");
  private final JButton reset = new JButton("Reset");
  private final SpringLayout springLayout = new SpringLayout();
  private Frame owner;

  private AddMemberDialogView(Frame owner) {
    initDialogSetting(owner);
    initFormElements();
    JPanel panel = initPanel();
    initListener();
    this.add(panel);
  }

  public static void showDig(Frame owner) {
    AddMemberDialogView addMemberDialogView = new AddMemberDialogView(owner);
    addMemberDialogView.setVisible(true);
  }

  public String getParentId() {
    return parentIdValue.getText();
  }

  public void setParentId(String parentId) {
    parentIdValue.setText(parentId);
  }

  private void initDialogSetting(Frame owner) {
    this.setTitle("New Member");
    this.setSize(600, 800);
    this.setPreferredSize(new Dimension(600, 800));
    this.setResizable(true);
    this.setLocationRelativeTo(owner);
    this.owner = owner;
  }

  private void initFormElements() {
    memberIDTextField.setEditable(false);
    memberIDTextField.setFocusable(false);
    memberIDTextField.setBackground(new Color(0, 0, 0, 0));
    memberIDTextField.setBorder(null);
    memberIDTextField.setText(IDUtil.generateUUID());

    parentIdChoose.setVisible(false);
    parentIdChoose.setEnabled(false);
    memberBox.add(memberComBox);
    memberBox.add(Box.createHorizontalStrut(5));
    memberBox.add(parentIdChoose);

    submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
    reset.setForeground(Color.RED);
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
    panel.add(memberBox);
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

    parentIdLabel.setVisible(false);
    parentIdValue.setVisible(false);

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
        healthConditionLabel, jScrollPaneWithHealthCondition, membershipLabel, memberBox);

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
    springLayout.putConstraint(SpringLayout.NORTH, memberIdLabel, Y_GAP, SpringLayout.NORTH, panel);

    springLayout.putConstraint(
        SpringLayout.NORTH, memberIDTextField, 0, SpringLayout.NORTH, memberIdLabel);
    springLayout.putConstraint(
        SpringLayout.WEST, memberIDTextField, X_GAP, SpringLayout.EAST, memberIdLabel);
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
    Box box2 = getHBox(parentIdLabel, parentIdValue);
    Box box3 = getHBox(discountLabel, discountValue);
    Box box4 = getHBox(membershipFeesLabel, membershipFeesValue);

    Box verticalBox = Box.createVerticalBox();
    verticalBox.add(box);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box1);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box2);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box3);
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(box4);

    int myCardWidth =
        Spring.sum(
                Spring.sum(Spring.width(durationDateLabel), Spring.width(memberIDTextField)),
                Spring.constant(X_GAP))
            .getValue();
    int myCardHeight =
        Spring.sum(Spring.height(verticalBox), Spring.constant(Y_GAP * 3)).getValue();

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

  private void initListener() {
    memberComBox.addActionListener(
        __ -> {
          String memberType = (String) memberComBox.getSelectedItem();
          if (DEFAULT_MEMBERS[1].getRoleName().equals(memberType)) {
            parentIdChoose.setVisible(true);
            parentIdChoose.setEnabled(true);
            parentIdLabel.setVisible(true);
            parentIdValue.setVisible(true);
            return;
          }
          parentIdChoose.setVisible(false);
          parentIdChoose.setEnabled(false);
          parentIdLabel.setVisible(false);
          parentIdValue.setVisible(false);
        });

    parentIdChoose.addActionListener(__ -> AddMainMemberDialogView.showDig(this.owner, this));

    durationComboBox.addActionListener(
        __ -> {
          Date date = startDateTextField.getDate();
          if (Objects.isNull(date)) {
            return;
          }

          String expireTime =
              DateUtil.plusMonths(
                  date,
                  Optional.ofNullable(durationComboBox.getSelectedItem())
                      .map(String.class::cast)
                      .map(Integer::parseInt)
                      .orElse(-1));

          expireTimeValue.setText(expireTime);
        });

    submit.addActionListener(
        e -> {
          CompletableFuture<RoleDto> roleDtoCompletableFuture =
              CompletableFuture.supplyAsync(
                  () -> {
                    String roleName = (String) memberComBox.getSelectedItem();
                    return DataSourceHandler.findRoleDtoByName(roleName);
                  });

          CustomerDto customerDto =
              new CustomerDto.Builder()
                  .id(memberIDTextField.getText())
                  .firstName(firstNameTextField.getText())
                  .lastName(lastNameTextField.getText())
                  .dateOfBirth(dateOfBirthTextField.getDate())
                  .gender(MEMBER_GENDER_LIST[genderComBox.getSelectedIndex()])
                  .homeAddress(homeAddressTextField.getText())
                  .phoneNumber(phoneNumberTextField.getText())
                  .healthCondition(healthConditionTextField.getText())
                  .type(roleDtoCompletableFuture.join())
                  .startDate(startDateTextField.getDate())
                  .expireTime(DateUtil.str2Date(expireTimeValue.getText()))
                  .duration(
                      Optional.ofNullable(durationComboBox.getSelectedItem())
                          .map(String.class::cast)
                          .map(Integer::parseInt)
                          .orElse(-1))
                  .fees(new BigDecimal(membershipFeesValue.getText()))
                  .build();

          DataSource.add(customerDto);
          this.dispose();
        });
  }
}
