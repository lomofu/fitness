import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 03:39
 */
public class CheckMemberInfoDialogView extends JDialog {
    private static final int X_GAP = 20;
    private static final int Y_GAP = 20;

    private final JLabel memberIdLabel = new JLabel("Member ID");
    private final JLabel firstNameLabel = new JLabel("First Name");
    private final JLabel lastNameLabel = new JLabel("Last Name");
    private final JLabel dateOfBirthLabel = new JLabel("Date Of Birth");
    private final JLabel yourAgeLabel = new JLabel("Your Age");
    private final JLabel genderLabel = new JLabel("Gender");
    private final JLabel homeAddressLabel = new JLabel("Home Address");
    private final JLabel phoneNumberLabel = new JLabel("Phone Number (+44)");
    private final JLabel healthConditionLabel = new JLabel("Health Condition");


    private final JLabel memberIDTextField = new JLabel();
    private final JLabel firstNameTextField = new JLabel();
    private final JLabel lastNameTextField = new JLabel();
    private final JLabel dateOfBirthTextField = new JLabel();
    private final JLabel yourAgeValue = new JLabel();
    private final JLabel genderComBox = new JLabel();
    private final JTextArea homeAddressTextField = new JTextArea(4, 20);
    private final JScrollPane jScrollPaneWithHomeAddress = new JScrollPane(homeAddressTextField);
    private final JLabel phoneNumberTextField = new JLabel();
    private final JTextArea healthConditionTextField = new JTextArea(4, 20);
    private final JScrollPane jScrollPaneWithHealthCondition =
            new JScrollPane(healthConditionTextField);

    private final MyCard myCard = new MyCard("Member Info", true);
    private final JLabel membershipLabel = new JLabel("Membership  ");
    private final JLabel memberTextField = new JLabel();
    private final JLabel startDateLabel = new JLabel("Start Date");
    private final JLabel startDateTextField = new JLabel("");
    private final JLabel durationDateLabel = new JLabel("Duration(Month)");
    private final JLabel durationComboBox = new JLabel("");
    private final JLabel expireTimeLabel = new JLabel("Expire Time  ");
    private final JLabel expireTimeValue = new JLabel("");
    private final JLabel parentIdLabel = new JLabel("Main ID  ");
    private final JLabel parentIdValue = new JLabel("");
    private final JLabel membershipFeesLabel = new JLabel("Membership Fees    ");
    private final JLabel membershipFeesValue = new JLabel();

    private final JButton edit = new JButton("Edit");
    private final JButton close = new JButton("Close");
    private final SpringLayout springLayout = new SpringLayout();
    private Frame owner;
    private CustomerDto customerDto;

    private CheckMemberInfoDialogView(Frame owner, String id) {
        initDialogSetting(owner);
        boolean flag = bindData(id);
        if(flag) {
            initFormElements();
            JPanel panel = initPanel();
            initListener();
            this.add(panel);
        } else {
            this.dispose();
        }
    }

    public static void showDig(Frame owner, String id) {
        CheckMemberInfoDialogView checkMemberInfoDialogView = new CheckMemberInfoDialogView(owner, id);
        checkMemberInfoDialogView.setVisible(true);
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true);
        this.setTitle("Personal Info");
        this.setSize(650, 850);
        this.setPreferredSize(new Dimension(650, 850));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.owner = owner;
    }

    private boolean bindData(String id) {
        Optional<CustomerDto> op = MembershipService.findCustomerByIdOp(id);
        if(op.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Can not find this member!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        this.customerDto = op.get();

        memberIDTextField.setText(customerDto.getId());
        firstNameTextField.setText(customerDto.getFirstName());
        lastNameTextField.setText(customerDto.getLastName());

        String dateOfBirth = DateUtil.format(customerDto.getDateOfBirth());
        if("".equals(dateOfBirth)) {
            dateOfBirthTextField.setText("No data");
        } else {
            dateOfBirthTextField.setText(dateOfBirth);
        }

        String age = String.valueOf(customerDto.getAge());
        if("".equals(age)) {
            yourAgeValue.setText("No data");
        } else {
            yourAgeValue.setText(age);
        }

        String homeAddress = customerDto.getHomeAddress();
        if("".equals(homeAddress)) {
            homeAddressTextField.setText("No data");
        } else {
            homeAddressTextField.setText(homeAddress);
        }

        genderComBox.setText(customerDto.getGender());

        String phoneNumber = customerDto.getPhoneNumber();
        if("".equals(phoneNumber)) {
            phoneNumberTextField.setText("No data");
        } else {
            phoneNumberTextField.setText(phoneNumber);
        }

        String healthCondition = customerDto.getHealthCondition();
        if("".equals(healthCondition)) {
            healthConditionTextField.setText("No data");
        } else {
            healthConditionTextField.setText(healthCondition);
        }

        String roleName = customerDto.getRole().getRoleName();
        if(roleName == null || "".equals(roleName)) {
            memberTextField.setText("Lost Info");
        } else {
            memberTextField.setText(roleName);
        }

        String parent = customerDto.getParent();
        if("".equals(parent)) {
            parentIdLabel.setVisible(false);
            parentIdValue.setVisible(false);
        } else {
            parentIdValue.setText(parent);
        }

        String startDate = DateUtil.format(customerDto.getStartDate());
        if("".equals(startDate)) {
            startDateTextField.setText("No data");
        } else {
            startDateTextField.setText(startDate);
        }

        String duration = String.valueOf(customerDto.getDuration());
        if("".equals(duration)) {
            durationComboBox.setText("No data");
        } else {
            durationComboBox.setText(duration);
        }

        String expireTime = DateUtil.format(customerDto.getExpireTime());
        if("".equals(expireTime)) {
            expireTimeValue.setText("No data");
        } else {
            expireTimeValue.setText(expireTime);
        }

        String fees = customerDto.getFees().toString();
        if("".equals(fees)) {
            membershipFeesValue.setText("No data");
        } else {
            membershipFeesValue.setText(fees);
        }
        return true;
    }


    private void initFormElements() {
        edit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setForeground(Color.RED);
    }

    private JPanel initPanel() {
        initComponentsSetting();
        JPanel panel = new JPanel(springLayout);
        initSpringLayout(panel);
        panel.add(memberIdLabel);
        panel.add(firstNameLabel);
        panel.add(lastNameLabel);
        panel.add(dateOfBirthLabel);
        panel.add(yourAgeLabel);
        panel.add(genderLabel);
        panel.add(homeAddressLabel);
        panel.add(phoneNumberLabel);
        panel.add(healthConditionLabel);

        panel.add(memberIDTextField);
        panel.add(firstNameTextField);
        panel.add(lastNameTextField);
        panel.add(dateOfBirthTextField);
        panel.add(yourAgeValue);
        panel.add(genderComBox);
        panel.add(jScrollPaneWithHomeAddress);
        panel.add(phoneNumberTextField);
        panel.add(jScrollPaneWithHealthCondition);
        panel.add(myCard);

        panel.add(edit);
        panel.add(close);
        return panel;

    }

    private void initComponentsSetting() {
        healthConditionTextField.setLineWrap(true);
        healthConditionTextField.setWrapStyleWord(true);
        healthConditionTextField.setEditable(false);
        jScrollPaneWithHealthCondition.setBackground(new Color(0, 0, 0, 0));
        jScrollPaneWithHealthCondition.setBorder(null);

        homeAddressTextField.setLineWrap(true);
        homeAddressTextField.setWrapStyleWord(true);
        homeAddressTextField.setEditable(false);
        jScrollPaneWithHomeAddress.setBorder(null);
        jScrollPaneWithHomeAddress.setBackground(new Color(0, 0, 0, 0));


        Font memberTextFieldFont = memberTextField.getFont();
        memberTextField.setFont(new Font(memberTextFieldFont.getFontName(), Font.BOLD, memberTextFieldFont.getSize()));

        Font startDateTextFieldFont = startDateTextField.getFont();
        startDateTextField.setFont(new Font(startDateTextFieldFont.getFontName(), Font.BOLD, startDateTextFieldFont.getSize()));

        Font durationComboBoxFont = durationComboBox.getFont();
        durationComboBox.setFont(new Font(durationComboBoxFont.getFontName(), Font.BOLD, durationComboBoxFont.getSize()));

        Font expireTimeValueFont = expireTimeValue.getFont();
        expireTimeValue.setFont(new Font(expireTimeValueFont.getFontName(), Font.BOLD, expireTimeValueFont.getSize()));

        Font membershipFeesValueFont = membershipFeesValue.getFont();
        membershipFeesValue.setFont(new Font(membershipFeesValueFont.getFontName(), Font.BOLD, membershipFeesValueFont.getSize()));

        initMemberInfoCard();
    }

    private void initMemberInfoCard() {
        Box box = getHBox(membershipLabel, memberTextField);
        Box box1 = getHBox(startDateLabel, startDateTextField);
        Box box2 = getHBox(durationDateLabel, durationComboBox);
        Box box3 = getHBox(expireTimeLabel, expireTimeValue);
        Box box4 = getHBox(parentIdLabel, parentIdValue);
        Box box5 = getHBox(membershipFeesLabel, membershipFeesValue);

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
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(box5);

        int myCardWidth =
                Spring.sum(Spring.sum(Spring.width(phoneNumberLabel), Spring.width(healthConditionTextField)),
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

    private void initSpringLayout(JPanel panel) {
        Spring childWidth =
                Spring.sum(
                        Spring.sum(Spring.width(memberIdLabel), Spring.width(new JTextField(20))),
                        Spring.constant(X_GAP));

        initMemberIdRow(panel, childWidth);

        initChildRow(memberIdLabel, firstNameLabel, firstNameTextField);
        initChildRow(firstNameLabel, lastNameLabel, lastNameTextField);
        initChildRow(lastNameLabel, dateOfBirthLabel, dateOfBirthTextField);
        initChildRow(dateOfBirthLabel, yourAgeLabel, yourAgeValue);
        initChildRow(yourAgeLabel, genderLabel, genderComBox);
        initChildRow(genderLabel, phoneNumberLabel, phoneNumberTextField);
        initChildRow(phoneNumberLabel, homeAddressLabel, jScrollPaneWithHomeAddress);
        initChildMultiRow(
                homeAddressLabel,
                jScrollPaneWithHomeAddress,
                healthConditionLabel,
                jScrollPaneWithHealthCondition);

        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, phoneNumberLabel);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, 0, SpringLayout.EAST, healthConditionTextField);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, jScrollPaneWithHealthCondition);

        springLayout.putConstraint(SpringLayout.WEST, edit, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, edit, Y_GAP, SpringLayout.SOUTH, myCard);

        springLayout.putConstraint(SpringLayout.NORTH, close, 0, SpringLayout.NORTH, edit);
        springLayout.putConstraint(SpringLayout.EAST, close, 0, SpringLayout.EAST, myCard);
    }

    private void initMemberIdRow(JPanel panel, Spring childWidth) {
        // HORIZONTAL_CENTER
        springLayout.putConstraint(
                SpringLayout.WEST,
                memberIdLabel,
                - childWidth.getValue() / 2,
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

    private void initListener() {
        close.addActionListener(__ -> this.dispose());
        edit.addActionListener(__ -> {
            this.dispose();
            EditMemberDialogView.showDig(owner, customerDto.getId());
        });
    }
}
