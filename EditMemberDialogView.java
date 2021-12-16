import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author lomofu
 * @desc
 * @create 25/Nov/2021 11:28
 */
public class EditMemberDialogView extends JDialog implements Validator {
    private static final int X_GAP = 20;
    private static final int Y_GAP = 20;

    private final JLabel memberIdLabel = new JLabel("* Member ID");
    private final JLabel firstNameLabel = new JLabel("* First Name");
    private final JLabel lastNameLabel = new JLabel("* Last Name");
    private final JLabel dateOfBirthLabel = new JLabel("* Date Of Birth");
    private final JLabel genderLabel = new JLabel("* Gender");
    private final JLabel homeAddressLabel = new JLabel("Home Address");
    private final JLabel phoneNumberLabel = new JLabel("* Phone Number (+44)");
    private final JLabel healthConditionLabel = new JLabel("Health Condition");
    private final JLabel membershipLabel = new JLabel("Membership");

    private final JTextField memberIDTextField = new JTextField(20);
    private final JTextField firstNameTextField = new JTextField(20);
    private final JTextField lastNameTextField = new JTextField(20);
    private final MyDatePicker dateOfBirthTextField = new MyDatePicker(1940, LocalDate.now().getYear());
    private final JComboBox<String> genderComBox = new JComboBox<>(UIConstant.MEMBER_GENDER_LIST);
    private final JTextArea homeAddressTextField = new JTextArea(4, 20);
    private final JScrollPane jScrollPaneWithHomeAddress = new JScrollPane(homeAddressTextField);
    private final JTextField phoneNumberTextField = new JTextField(20);
    private final JTextArea healthConditionTextField = new JTextArea(4, 20);
    private final JScrollPane jScrollPaneWithHealthCondition =
            new JScrollPane(healthConditionTextField);
    private final Box memberBox = Box.createHorizontalBox();
    private final JButton transferBtn = new JButton("Transfer");
    private final JTextField memberTextField = new JTextField(10);

    private final MyCard myCard = new MyCard("Member Info", true);
    private final JLabel yourAgeLabel = new JLabel("Your Age  ", JLabel.LEFT);
    private final JLabel yourAgeValue = new JLabel("0", JLabel.RIGHT);

    private final JButton update = new JButton("Update");
    private final JButton cancel = new JButton("Cancel");

    private final SpringLayout springLayout = new SpringLayout();
    private Frame owner;

    public EditMemberDialogView(Frame owner) {
        initDialogSetting(owner);
        initFormElements();
        JPanel myPanel = initPanel();
        initListener();
        this.add(myPanel);
    }

    public static void showDig(Frame owner, String customerId) {
        CustomerDto customerDto = MembershipService.findCustomerById(customerId);
        EditMemberDialogView editMemberDialogView = new EditMemberDialogView(owner);
        bindData(editMemberDialogView, customerDto);
        editMemberDialogView.setVisible(true);
    }

    private static void bindData(EditMemberDialogView editMemberDialogView, CustomerDto data) {
        Optional.ofNullable(data)
                .orElseThrow(() -> new RuntimeException("Selected Item cannot be null"));

        editMemberDialogView.memberIDTextField.setText(data.getId());
        editMemberDialogView.firstNameTextField.setText(data.getFirstName());
        editMemberDialogView.lastNameTextField.setText(data.getLastName());
        editMemberDialogView.dateOfBirthTextField.setDate(data.getDateOfBirth());
        editMemberDialogView.genderComBox.setSelectedItem(data.getGender());
        editMemberDialogView.homeAddressTextField.setText(data.getHomeAddress());
        editMemberDialogView.phoneNumberTextField.setText(data.getPhoneNumber());
        editMemberDialogView.healthConditionTextField.setText(data.getHealthCondition());
        if (data.getRole().getRoleName() == null || "".equals(data.getRole().getRoleName())) {
            editMemberDialogView.memberTextField.setText("Lost Info");
            editMemberDialogView.transferBtn.setVisible(true);
        } else {
            editMemberDialogView.memberTextField.setText(data.getRole().getRoleName());
        }
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true);
        this.setTitle("Edit Member");
        this.setSize(700, 650);
        this.setPreferredSize(new Dimension(700, 650));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.owner = owner;
    }

    private void initFormElements() {
        memberIDTextField.setEditable(false);
        memberIDTextField.setFocusable(false);
        memberIDTextField.setBackground(new Color(0, 0, 0, 0));
        memberIDTextField.setBorder(null);
        memberIDTextField.setText("NULL");

        memberTextField.setFocusable(false);
        memberTextField.setEnabled(false);
        memberTextField.setBackground(new Color(0, 0, 0, 0));
        memberTextField.setBorder(null);

        memberBox.add(memberTextField);
        memberBox.add(transferBtn);
        memberBox.add(Box.createHorizontalGlue());
        transferBtn.setVisible(false);

        phoneNumberTextField.setDocument(new NumberDocument());

        update.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        cancel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        cancel.setForeground(Color.RED);
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

        panel.add(memberIDTextField);
        panel.add(firstNameTextField);
        panel.add(lastNameTextField);
        panel.add(dateOfBirthTextField);
        panel.add(genderComBox);
        panel.add(jScrollPaneWithHomeAddress);
        panel.add(phoneNumberTextField);
        panel.add(jScrollPaneWithHealthCondition);
        panel.add(memberBox);
        panel.add(myCard);

        panel.add(update);
        panel.add(cancel);
        return panel;
    }

    private void initComponentsSetting() {
        healthConditionTextField.setLineWrap(true);
        healthConditionTextField.setWrapStyleWord(true);

        homeAddressTextField.setLineWrap(true);
        homeAddressTextField.setWrapStyleWord(true);

        Font yourAgeValueFont = yourAgeValue.getFont();
        yourAgeValue.setFont(
                new Font(yourAgeValueFont.getFontName(), Font.BOLD, yourAgeValueFont.getSize()));

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

        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, membershipLabel);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, memberBox);

        springLayout.putConstraint(SpringLayout.WEST, update, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, update, Y_GAP, SpringLayout.SOUTH, myCard);

        springLayout.putConstraint(SpringLayout.NORTH, cancel, 0, SpringLayout.NORTH, update);
        springLayout.putConstraint(SpringLayout.EAST, cancel, 0, SpringLayout.EAST, myCard);
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

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(box);
        verticalBox.add(Box.createVerticalStrut(10));

        int myCardWidth =
                Spring.sum(Spring.sum(Spring.width(healthConditionLabel), Spring.width(healthConditionTextField)),
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

    private void initListener() {
        dateOfBirthTextField.onSelect(
                __ -> {
                    Date date = dateOfBirthTextField.getDate();
                    int age = DateUtil.calculateAge(date);
                    yourAgeValue.setText(String.valueOf(age));
                });

        update.addActionListener(__ -> {
            CompletableFuture<Optional<CustomerDto>> future = CompletableFuture.supplyAsync(
                    () -> MembershipService.findCustomerByIdOp(memberIDTextField.getText()));

            if (!"".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Optional<CustomerDto> customerDto = future.join();
            if (customerDto.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "No membership find", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            CustomerDto dto = customerDto.get();
            dto.setFirstName(firstNameTextField.getText());
            dto.setLastName(lastNameTextField.getText());
            dto.setDateOfBirth(dateOfBirthTextField.getDate());
            dto.setGender(UIConstant.MEMBER_GENDER_LIST[genderComBox.getSelectedIndex()]);
            dto.setPhoneNumber(phoneNumberTextField.getText());
            dto.setHomeAddress(homeAddressTextField.getText());
            dto.setHealthCondition(healthConditionTextField.getText());
            dto.setAge(Integer.parseInt(yourAgeValue.getText()));

            DataSource.update(dto);
            this.dispose();
        });

        transferBtn.addActionListener(__ -> {
            Optional<CustomerDto> op = MembershipService.findCustomerByIdOp(memberIDTextField.getText());
            if (op.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "No membership find", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            this.dispose();
            AddMemberDialogView.showDig(this.owner, op.get());
        });

        cancel.addActionListener(__ -> this.dispose());
    }

    @Override
    public String valid() {
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("First Name", new Validation(firstNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Last Name", new Validation(lastNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Date of Birth", new Validation(dateOfBirthTextField.getDate(), ValidationEnum.NOT_NULL, ValidationEnum.NOT_GREATER_THAN_NOW));

        if (DefaultDataConstant.DEFAULT_MEMBERS[0].getRoleName().equals(memberTextField.getText())) {
            map.put("Age", new Validation(yourAgeValue.getText(), ValidationEnum.GREATER_THAN_12));
        } else if (DefaultDataConstant.DEFAULT_MEMBERS[1].getRoleName().equals(memberTextField.getText())) {
            map.put("Age", new Validation(yourAgeValue.getText(), ValidationEnum.GREATER_THAN_18));
        } else if ("Lost Info".equals(memberTextField.getText())) {
            return "The member info has been lost, please click the transfer button to create a new account";
        }

        map.put("Phone Number", new Validation(phoneNumberTextField.getText(), ValidationEnum.HAS_LEN, ValidationEnum.UK_PHONE_NUMBER));

        List<String> errorMsg = ValidationEnum.valid(map);
        if (errorMsg.isEmpty()) {
            return "";
        }
        return errorMsg.get(0);
    }

    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            if (this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }

}
