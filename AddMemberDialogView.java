import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * @author lomofu
 * <p>
 * The class covers functions below:
 * 1. Input and display new member information.
 * 2. For the old member records who lose the membership type, it can be transferred to active members.
 */
public class AddMemberDialogView extends JDialog implements Validator {
    private static final int X_GAP = 20; // define the default gap for each component in the layout
    private static final int Y_GAP = 20;

    private final JLabel memberIdLabel = new JLabel("* Member ID");
    private final JLabel firstNameLabel = new JLabel("* First Name");
    private final JLabel lastNameLabel = new JLabel("* Last Name");
    private final JLabel dateOfBirthLabel = new JLabel("* Date Of Birth");
    private final JLabel genderLabel = new JLabel("* Gender");
    private final JLabel homeAddressLabel = new JLabel("Home Address");
    private final JLabel phoneNumberLabel = new JLabel("* Phone Number (+44)");
    private final JLabel healthConditionLabel = new JLabel("Health Condition");
    private final JLabel membershipLabel = new JLabel("* Membership");
    private final JLabel startDateLabel = new JLabel("* Start Date");
    private final JLabel durationDateLabel = new JLabel("* Duration(Month)");
    private final JLabel codeLabel = new JLabel("Promotion Code");

    private final JTextField memberIDTextField = new JTextField(20);
    private final JTextField firstNameTextField = new JTextField(20);
    private final JTextField lastNameTextField = new JTextField(20);
    private final MyDatePicker dateOfBirthTextField = new MyDatePicker(1940, LocalDate.now().getYear());
    private final JComboBox<String> genderComBox = new JComboBox<>(UIConstant.MEMBER_GENDER_LIST);
    private final JTextArea homeAddressTextField = new JTextArea(4, 20);
    private final JScrollPane jScrollPaneWithHomeAddress = new JScrollPane(homeAddressTextField);
    private final JTextField phoneNumberTextField = new JTextField(20);
    private final JTextArea healthConditionTextField = new JTextArea(4, 20);
    private final JScrollPane jScrollPaneWithHealthCondition = new JScrollPane(healthConditionTextField);
    private final JComboBox<String> memberComBox = new JComboBox<>(DataSourceHandler.findRoleDtoList());
    private final JButton parentIdChoose = new JButton("Choose main");
    private final Box memberBox = Box.createHorizontalBox();
    private final MyDatePicker startDateTextField = new MyDatePicker(LocalDate.now().getYear(), LocalDate.now().getYear());
    private final JComboBox<String> durationComboBox = new JComboBox<>(UIConstant.MEMBER_DEFAULT_DURATION);
    private final JTextField codeTextField = new JTextField(15);
    private final JButton codeBtn = new JButton("Apply");
    private final Box codeBox = Box.createHorizontalBox();

    private final MyCard myCard = new MyCard("Member Info", true);
    private final JLabel yourAgeLabel = new JLabel("Your Age  ", JLabel.LEFT);
    private final JLabel yourAgeValue = new JLabel("0", JLabel.RIGHT);
    private final JLabel expireTimeLabel = new JLabel("Expire Time  ");
    private final JLabel expireTimeValue = new JLabel("");
    private final JLabel parentIdLabel = new JLabel("Main ID  ");
    private final JLabel parentIdValue = new JLabel("");
    private final JLabel discountLabel = new JLabel("Discount");
    private final JLabel discountValue = new JLabel("0");
    private final JLabel membershipFeesLabel = new JLabel("Membership Fees    ");
    private final JLabel membershipFeesValue = new JLabel("0");

    private final JButton submit = new JButton("Submit");
    private final JButton cancel = new JButton("Cancel");
    private final SpringLayout springLayout = new SpringLayout();

    private Frame owner; // store the parent frame


    private boolean transfer;  /* a flag marks the dialog show have a transfer button or not */
    private CustomerDto transferDto; /* store the account needed to be transfer  */
    private boolean apply; /* a flag marks the promotion code is applied that it can work when calculate the fee */

    private AddMemberDialogView(Frame owner) {
        initDialogSetting(owner);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    private AddMemberDialogView(Frame owner, boolean transfer, CustomerDto customerDto) {
        this.transfer = transfer;
        this.transferDto = customerDto;
        initDialogSetting(owner);
        bindData(customerDto);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    /**
     * This factory method will create a new dialog when click the "Add" Button on the see @MemberTable.
     *
     * @param owner the parent component
     */
    public static void showDig(Frame owner) {
        AddMemberDialogView addMemberDialogView = new AddMemberDialogView(owner);
        addMemberDialogView.setVisible(true);
    }

    /**
     * This factory method will create a new dialog when click the "Choose Main" Button.
     *
     * @param owner       the parent component
     * @param customerDto the info of membership (missing the membership type) need to be transferred to a new account
     */
    public static void showDig(Frame owner, CustomerDto customerDto) {
        AddMemberDialogView addMemberDialogView = new AddMemberDialogView(owner, true, customerDto);
        addMemberDialogView.setVisible(true);
    }

    /**
     * Only need invoke if is a transfer situation. Therefore, some membership info will be set in to the view
     *
     * @param customerDto the info of membership (missing the membership type) need to be transferred to a new account
     */
    private void bindData(CustomerDto customerDto) {
        firstNameTextField.setText(customerDto.getFirstName());
        lastNameTextField.setText(customerDto.getLastName());
        dateOfBirthTextField.setDate(customerDto.getDateOfBirth());
        genderComBox.setSelectedItem(customerDto.getGender());
        homeAddressTextField.setText(customerDto.getHomeAddress());
        phoneNumberTextField.setText(customerDto.getPhoneNumber());
        updateAge();
    }

    /**
     * It is a getter function to should the parentId to other component, which need to know the parent id
     * see@ AddMainMemberDialogView.
     * <p>
     * Warning:
     * The parentId value will be "" is the membership type is not a family member
     *
     * @return the parent id
     */
    public String getParentId() {
        return parentIdValue.getText();
    }

    /**
     * This function covers if a customer is sub-account of the family member. The parent membership info
     * (start date, duration and expire date) will be synced to this account.
     *
     * @param parent the parent info
     */
    public void syncParentInfo(CustomerDto parent) {
        startDateTextField.setDate(parent.getStartDate());
        durationComboBox.setSelectedItem(String.valueOf(parent.getDuration()));
        expireTimeValue.setText(DateUtil.format(parent.getExpireTime()));
        membershipFeesValue.setText("0"); // the sub-account will not have a fee
        parentIdValue.setText(parent.getId());

        startDateTextField.setLock(); // lock the date, it cannot be edited and only same to the main account
        durationComboBox.setEnabled(false); // cannot edit the duration, only same to the main account
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true); // make this dialog on the top and interrupt to operate the parent component
        if(transfer) {
            this.setTitle("Transfer to a new Member");
        } else {
            this.setTitle("New Member");
        }
        this.setSize(650, 850);
        this.setPreferredSize(new Dimension(650, 850));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.owner = owner;
    }

    private void initFormElements() {
        memberIDTextField.setEditable(false);
        memberIDTextField.setFocusable(false);
        memberIDTextField.setBackground(new Color(0, 0, 0, 0));
        memberIDTextField.setBorder(null);
        memberIDTextField.setText(IDUtil.generateUUID()); // each new member will be given a UUID

        startDateTextField.setNow(); // make the start date automatically is current
        updateExpireDate(); // the expired date will be updated depend on the expired date
        updateFees(); // the fees will be calculated depending on the duration

        parentIdChoose.setVisible(false);
        parentIdChoose.setEnabled(false);

        memberBox.add(memberComBox);
        memberBox.add(Box.createHorizontalStrut(5));
        memberBox.add(parentIdChoose);

        codeBox.add(codeTextField);
        codeBox.add(Box.createHorizontalStrut(5));
        codeBox.add(codeBtn);

        submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
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
        panel.add(codeBox);

        panel.add(submit);
        panel.add(cancel);
        return panel;
    }

    /**
     * change some style of components in the form
     */
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

        phoneNumberTextField.setDocument(new NumberDocument());

        // the following font object is to make the text bold
        Font yourAgeValueFont = yourAgeValue.getFont();
        yourAgeValue.setFont(
                new Font(yourAgeValueFont.getFontName(), Font.BOLD, yourAgeValueFont.getSize()));

        Font expireTimeValueFont = expireTimeValue.getFont();
        expireTimeValue.setFont(new Font(expireTimeValueFont.getFontName(), Font.BOLD, expireTimeValueFont.getSize()));

        Font discountValueFont = discountValue.getFont();
        discountValue.setFont(new Font(discountValueFont.getFontName(), Font.BOLD, discountValueFont.getSize()));

        Font membershipFeesValueFont = membershipFeesValue.getFont();
        membershipFeesValue.setFont(new Font(membershipFeesValueFont.getFontName(), Font.BOLD, membershipFeesValueFont.getSize()));

        initMemberInfoCard(); // init the member info card setting
    }

    private void initSpringLayout(JPanel panel) {
        // use the Spring to calculate the width of the memberIdLabel width + memberIDTextField width + const X_GAP (20)
        Spring childWidth = Spring.sum(Spring.sum(Spring.width(memberIdLabel), Spring.width(memberIDTextField)),
                Spring.constant(X_GAP));

        // pack the first row
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
        initChildRow(durationDateLabel, codeLabel, codeBox);

        // use the springlayout to put constraint
        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, codeLabel);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, codeBox);

        springLayout.putConstraint(SpringLayout.WEST, submit, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, submit, Y_GAP, SpringLayout.SOUTH, myCard);

        springLayout.putConstraint(SpringLayout.NORTH, cancel, 0, SpringLayout.NORTH, submit);
        springLayout.putConstraint(SpringLayout.EAST, cancel, 0, SpringLayout.EAST, myCard);

    }

    private void initMemberIdRow(JPanel panel, Spring childWidth) {
        // horizontal center
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

    /**
     * The function abstract a row with a JLabel and a component (most is the JTextField)
     *
     * @param refer     the reference label
     * @param label     the label need to put constraint
     * @param component the component need to put constraint
     */
    private void initChildRow(JLabel refer, JLabel label, Component component) {
        // let the label align to refer
        springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
        springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

        // let the component align to label
        springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
        springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
    }

    /**
     * The function covers the component with multi-row (like the JTextArea) and seems to the see@initChildRow
     *
     * @param referLab  the reference label
     * @param refer     the reference component
     * @param label     the label need to put constraint
     * @param component the component need to put constraint
     */
    private void initChildMultiRow(
            JLabel referLab, Component refer, JLabel label, Component component) {
        // let the component align to refer
        springLayout.putConstraint(SpringLayout.NORTH, component, Y_GAP, SpringLayout.SOUTH, refer);
        springLayout.putConstraint(SpringLayout.WEST, component, 0, SpringLayout.WEST, refer);

        // let the label align to component and referLab
        springLayout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.NORTH, component);
        springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, referLab);
    }

    private void initMemberInfoCard() {
        // use box for layout
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

        // use the Spring to calculate the width of the durationDateLabel + memberIDTextField + const X_GAP (20)
        int myCardWidth =
                Spring.sum(Spring.sum(Spring.width(durationDateLabel), Spring.width(memberIDTextField)),
                                Spring.constant(X_GAP))
                        .getValue();

        // use the Spring to calculate the height of the verticalBox + const Y_GAP (20) * 3
        int myCardHeight =
                Spring.sum(Spring.height(verticalBox), Spring.constant(Y_GAP * 3)).getValue();

        // set the myCard height and width
        myCard.setCSize(myCardWidth, myCardHeight);
        myCard.addC(verticalBox);
    }

    /* this function will create a HorizontalBox filled with two labels */
    private Box getHBox(JLabel label, JLabel value) {
        Box box = Box.createHorizontalBox();
        box.add(label);
        box.add(Box.createHorizontalGlue());
        box.add(value);
        return box;
    }

    @Override
    public void dispose() {
        super.dispose();
        owner.setEnabled(true);
    }

    /**
     * This function manger the
     */
    private void initListener() {
        dateOfBirthTextField.onSelect(e -> updateAge());

        memberComBox.addActionListener(__ -> {
            String memberType = (String) memberComBox.getSelectedItem();
            if(DefaultDataConstant.DEFAULT_MEMBERS[1].getRoleName().equals(memberType)) {
                parentIdChoose.setVisible(true);
                parentIdChoose.setEnabled(true);
                parentIdLabel.setVisible(true);
                parentIdValue.setVisible(true);
            } else {
                startDateTextField.setUnLock();
                parentIdChoose.setVisible(false);
                parentIdChoose.setEnabled(false);
                parentIdLabel.setVisible(false);
                parentIdValue.setVisible(false);
            }
            if(startDateTextField.getDate() != null) {
                updateFees();
            }
            String code = codeTextField.getText();
            Optional<Promotion> promotion = PromotionCodeService.findPromotionCodeOp(code);
            if(startDateTextField.getDate() != null && promotion.isPresent()) {
                updateFeesWithDiscount();
            }
        });

        parentIdChoose.addActionListener(__ -> AddMainMemberDialogView.showDig(this));

        startDateTextField.onSelect(__ -> {
            updateExpireDate();
            String code = codeTextField.getText();
            Optional<Promotion> promotion = PromotionCodeService.findPromotionCodeOp(code);
            if(promotion.isEmpty()) {
                updateFees();
                return;
            }
            updateFeesWithDiscount();
        });

        durationComboBox.addActionListener(__ -> {
            updateExpireDate();
            if(! apply) {
                updateFees();
                return;
            }

            String code = codeTextField.getText();
            Optional<Promotion> promotion = PromotionCodeService.findPromotionCodeOp(code);
            if(promotion.isEmpty()) {
                updateFees();
                return;
            }
            updateFeesWithDiscount();
        });

        codeBtn.addActionListener(__ -> {
            String code = codeTextField.getText();
            if(code == null || PromotionCodeService.findPromotionCodeOp(code).isEmpty()) {
                this.apply = false;
                JOptionPane.showMessageDialog(
                        this, "Code Invalid", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.apply = true;
            updateFeesWithDiscount();
        });

        submit.addActionListener(__ -> {
            CompletableFuture<RoleDto> roleDtoCompletableFuture =
                    CompletableFuture.supplyAsync(
                            () -> {
                                String roleName = (String) memberComBox.getSelectedItem();
                                return DataSourceHandler.findRoleDtoByName(roleName);
                            });

            if(! "".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            String message = """
                    Are you sure to create a member?
                                            
                    Please follow the steps:
                    1. check the sensitive information has been filled correctly.
                    2. check the customer paid.
                    """;

            String message1 = """
                    Are you sure to create a member with transfer function?
                                            
                    Please follow the steps:
                    1. check the sensitive information has been filled correctly.
                    2. check the customer paid.
                                        
                    Warning:
                    - The old account will be remove from the system
                    """;

            int result = JOptionPane.showConfirmDialog(
                    this,
                    transfer ? message1 : message,
                    "Create Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if(result != JOptionPane.YES_OPTION) {
                return;
            }

            CustomerDto customerDto = new CustomerDto.Builder()
                    .id(memberIDTextField.getText())
                    .firstName(firstNameTextField.getText())
                    .lastName(lastNameTextField.getText())
                    .dateOfBirth(dateOfBirthTextField.getDate())
                    .gender(UIConstant.MEMBER_GENDER_LIST[genderComBox.getSelectedIndex()])
                    .homeAddress(homeAddressTextField.getText())
                    .phoneNumber(phoneNumberTextField.getText())
                    .healthCondition(healthConditionTextField.getText())
                    .type(roleDtoCompletableFuture.join())
                    .age(Integer.parseInt(yourAgeValue.getText()))
                    .startDate(startDateTextField.getDate())
                    .expireTime(DateUtil.str2Date(expireTimeValue.getText()))
                    .duration(
                            Optional.ofNullable(durationComboBox.getSelectedItem())
                                    .map(String.class::cast)
                                    .map(Integer::parseInt)
                                    .orElse(- 1))
                    .parentId(parentIdValue.getText())
                    .fees(new BigDecimal(membershipFeesValue.getText()))
                    .build();

            if(transfer) {
                MembershipService.transfer(transferDto.getId(), customerDto);
            } else {
                MembershipService.createNew(customerDto);
            }
            this.dispose();
        });

        cancel.addActionListener(__ -> this.dispose());
    }

    private void updateAge() {
        Date date = dateOfBirthTextField.getDate();
        int age = DateUtil.calculateAge(date);
        yourAgeValue.setText(String.valueOf(age));
    }

    private void updateExpireDate() {
        Date date = startDateTextField.getDate();
        if(Objects.isNull(date)) {
            return;
        }
        String expireTime = DateUtil.plusMonths(date, Optional.ofNullable(durationComboBox.getSelectedItem())
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(- 1));

        expireTimeValue.setText(expireTime);
    }

    private void updateFees() {
        String type = (String) memberComBox.getSelectedItem();
        String duration = (String) durationComboBox.getSelectedItem();
        String mid = parentIdValue.getText();
        if(DefaultDataConstant.DEFAULT_MEMBERS[1].getRoleName().equals(type) && "".equals(mid)) {
            return;
        }
        BigDecimal fees = FeesService.getFees(type, duration);
        discountLabel.setVisible(false);
        discountValue.setVisible(false);
        discountValue.setText("0");
        membershipFeesValue.setText(fees.toString());
    }

    private void updateFeesWithDiscount() {
        String type = (String) memberComBox.getSelectedItem();
        String mid = parentIdValue.getText();
        if(DefaultDataConstant.DEFAULT_MEMBERS[1].getRoleName().equals(type) && "".equals(mid)) {
            return;
        }
        String duration = (String) durationComboBox.getSelectedItem();
        String code = codeTextField.getText();
        BigDecimal[] fees = FeesService.getFees(type, duration, code);

        discountLabel.setVisible(true);
        discountValue.setVisible(true);
        discountValue.setText(fees[0].toString());
        membershipFeesValue.setText(fees[1].toString());
    }

    @Override
    public String valid() {
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("Member ID", new Validation(memberIDTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("First Name", new Validation(firstNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Last Name", new Validation(lastNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Date of Birth", new Validation(dateOfBirthTextField.getDate(), ValidationEnum.NOT_NULL, ValidationEnum.NOT_GREATER_THAN_NOW));
        if(DefaultDataConstant.DEFAULT_MEMBERS[0].getRoleName().equals(memberComBox.getSelectedItem())) {
            map.put("Age", new Validation(yourAgeValue.getText(), ValidationEnum.GREATER_THAN_12));
        } else {
            map.put("Age", new Validation(yourAgeValue.getText(), ValidationEnum.GREATER_THAN_18));
        }
        map.put("Phone Number", new Validation(phoneNumberTextField.getText(), ValidationEnum.HAS_LEN, ValidationEnum.UK_PHONE_NUMBER));
        map.put("Start Date", new Validation(startDateTextField.getDate(), ValidationEnum.NOT_NULL, ValidationEnum.NOT_GREATER_THAN_NOW));

        List<String> errorMsg = ValidationEnum.valid(map);
        if(errorMsg.isEmpty()) {
            return "";
        }
        return errorMsg.get(0);
    }

    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            if(this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch(NumberFormatException var3) {
                return false;
            }
        }
    }
}