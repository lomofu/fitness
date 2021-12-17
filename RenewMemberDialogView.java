import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Jiaqi Fu
 * <p>
 * This class is used to create a new dialog to renew the membership expire date
 */
public class RenewMemberDialogView extends JDialog {
    // define the default gap for each component in the layout
    private static final int X_GAP = 20;
    private static final int Y_GAP = 20;

    private final JLabel memberIdLabel = new JLabel("Member ID");
    private final JLabel memberIDTextField = new JLabel();

    private final JLabel durationDateLabel = new JLabel("Duration(Month)");
    private final JComboBox<String> durationComboBox = new JComboBox<>(UIConstant.MEMBER_DEFAULT_DURATION);

    private final JLabel codeLabel = new JLabel("Promotion Code");
    private final JTextField codeTextField = new JTextField(15);
    private final JButton codeBtn = new JButton("Apply");
    private final Box codeBox = Box.createHorizontalBox();

    private final MyCard myCard = new MyCard("Member Info", true);
    private final JLabel membershipLabel = new JLabel("Membership  ");
    private final JLabel memberTextField = new JLabel();
    private final JLabel startDateLabel = new JLabel("Start Date");
    private final JLabel startDateTextField = new JLabel("");
    private final JLabel expireTimeLabel = new JLabel("Expire Time  ");
    private final JLabel expireTimeValue = new JLabel("");
    private final JLabel discountLabel = new JLabel("Discount");
    private final JLabel discountValue = new JLabel("0");
    private final JLabel membershipFeesLabel = new JLabel("Membership Fees    ");
    private final JLabel membershipFeesValue = new JLabel("0");

    private final JButton renew = new JButton("Renew");
    private final JButton close = new JButton("Close");
    private final SpringLayout springLayout = new SpringLayout();
    private Frame owner;
    private CustomerDto customerDto;
    private boolean apply;

    private RenewMemberDialogView(Frame owner, String id) {
        initDialogSetting(owner);
        bindData(id);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    /**
     * This factory method will create a new dialog when click the renew button on the
     * see@MemberTable
     *
     * @param owner parent component
     * @param id    member id
     */
    public static void showDig(Frame owner, String id) {
        if (checkData(id))
            return;
        RenewMemberDialogView renewMemberDialogView = new RenewMemberDialogView(owner, id);
        renewMemberDialogView.setVisible(true);
    }

    /**
     * This method will check if the selected member can be renewed
     *
     * @param id member id
     */
    private static boolean checkData(String id) {
        Optional<CustomerDto> op = MembershipService.findCustomerByIdOp(id);
        // if the member cannot be found, show the error message and no action is taken
        if (op.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Can not find this member!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        CustomerDto customerDto = op.get();
        String roleName = customerDto.getRole().getRoleName();
        // if the membership type info lost,show the error message and no action is taken
        if (roleName == null || "".equals(roleName)) {
            JOptionPane.showMessageDialog(null, """
                    Can not renew this account!
                                        
                    Because the membership of this account has been lost, please transfer to a new account
                    """, "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        String parent = customerDto.getParent();
        // if the member is a sub-account family member, it cannot be renewed
        if (!(parent == null || "".equals(parent)) && roleName.equals(DefaultDataConstant.DEFAULT_MEMBERS[1].getRoleName())) {
            JOptionPane.showMessageDialog(null, """
                    Can not renew this account!
                                        
                    This account is a sub-account of the family member!
                    """, "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true);
        this.setTitle("Renew Membership");
        this.setSize(600, 450);
        this.setPreferredSize(new Dimension(600, 450));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.owner = owner;
    }

    /**
     * This method will find the relevant information based on the member ID to be displayed in the corresponding component
     *
     * @param id member id
     */
    private void bindData(String id) {
        this.customerDto = MembershipService.findCustomerByIdOp(id).get();
        memberIDTextField.setText(customerDto.getId());
        memberTextField.setText(customerDto.getRole().getRoleName());
        expireTimeValue.setText(DateUtil.format(customerDto.getExpireTime()));
        // if the member is within the expiry date, the membership is renewed at the original expiry date
        if (customerDto.getState().equals(CustomerSateEnum.ACTIVE.getName())) {
            startDateTextField.setText(DateUtil.format(customerDto.getStartDate()));
            updateExpireDateWithExpire();
        } else {
            //if the membership has expired, then the renewal starts on the day of renewal
            startDateTextField.setText(DateUtil.now());
            updateExpireDate();
        }
        updateFees();
    }

    // set the format of some components
    private void initFormElements() {
        renew.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setForeground(Color.RED);

        codeBox.add(codeTextField);
        codeBox.add(Box.createHorizontalStrut(5));
        codeBox.add(codeBtn);
    }

    private JPanel initPanel() {
        initComponentsSetting();

        JPanel panel = new JPanel(springLayout);
        initSpringLayout(panel);

        // add the components to the panel
        panel.add(memberIdLabel);
        panel.add(durationDateLabel);
        panel.add(codeLabel);

        panel.add(memberIDTextField);
        panel.add(durationComboBox);
        panel.add(codeBox);
        panel.add(myCard);

        panel.add(renew);
        panel.add(close);
        return panel;
    }

    /**
     * change some style of components in the form
     */
    private void initComponentsSetting() {
        discountLabel.setForeground(Color.RED);
        discountLabel.setVisible(false);
        discountValue.setForeground(Color.RED);
        discountValue.setVisible(false);

        Font memberTextFieldFont = memberTextField.getFont();
        memberTextField.setFont(new Font(memberTextFieldFont.getFontName(), Font.BOLD, memberTextFieldFont.getSize()));

        Font startDateTextFieldFont = startDateTextField.getFont();
        startDateTextField.setFont(new Font(startDateTextFieldFont.getFontName(), Font.BOLD, startDateTextFieldFont.getSize()));

        Font expireTimeValueFont = expireTimeValue.getFont();
        expireTimeValue.setFont(new Font(expireTimeValueFont.getFontName(), Font.BOLD, expireTimeValueFont.getSize()));

        Font discountValueFont = discountValue.getFont();
        discountValue.setFont(new Font(discountValueFont.getFontName(), Font.BOLD, discountValueFont.getSize()));

        Font membershipFeesValueFont = membershipFeesValue.getFont();
        membershipFeesValue.setFont(new Font(membershipFeesValueFont.getFontName(), Font.BOLD, membershipFeesValueFont.getSize()));

        initMemberInfoCard();
    }

    private void initMemberInfoCard() {
        Box box = getHBox(membershipLabel, memberTextField);
        Box box1 = getHBox(startDateLabel, startDateTextField);
        Box box2 = getHBox(expireTimeLabel, expireTimeValue);
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
                Spring.sum(Spring.sum(Spring.width(codeLabel), Spring.width(codeBox)),
                                Spring.constant(X_GAP))
                        .getValue();
        int myCardHeight =
                Spring.sum(Spring.height(verticalBox), Spring.constant(Y_GAP * 3)).getValue();

        myCard.setCSize(myCardWidth, myCardHeight);
        myCard.addC(verticalBox);
    }

    /**
     * this function will create a HorizontalBox filled with two labels
     */
    private Box getHBox(JLabel label, JLabel value) {
        Box box = Box.createHorizontalBox();
        box.add(label);
        box.add(Box.createHorizontalGlue());
        box.add(value);
        return box;
    }

    private void initSpringLayout(JPanel panel) {
        // use the Spring to calculate the width of the codeLabel width + codeBox width + const X_GAP (20)
        Spring childWidth =
                Spring.sum(
                        Spring.sum(Spring.width(codeLabel), Spring.width(codeBox)),
                        Spring.constant(X_GAP));

        // pack the first row
        initMemberIdRow(panel, childWidth);

        initChildRow(memberIdLabel, durationDateLabel, durationComboBox);
        initChildRow(durationDateLabel, codeLabel, codeBox);

        // use the spring layout to put constraint
        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, codeLabel);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, codeBox);

        springLayout.putConstraint(SpringLayout.WEST, renew, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, renew, Y_GAP, SpringLayout.SOUTH, myCard);

        springLayout.putConstraint(SpringLayout.NORTH, close, 0, SpringLayout.NORTH, renew);
        springLayout.putConstraint(SpringLayout.EAST, close, 0, SpringLayout.EAST, myCard);

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

    /**
     * The function abstract a row with a JLabel and a component (most are the JTextFields)
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
     * This function manger all the callback events of the components
     */
    private void initListener() {
        // this callback will handle the promotion code to the calculation system if it is legal
        codeBtn.addActionListener(__ -> {
            String code = codeTextField.getText();
            // cover the NPE or the promotion code is illegal
            if (code == null || PromotionCodeService.findPromotionCodeOp(code).isEmpty()) {
                this.apply = false;
                JOptionPane.showMessageDialog(
                        this, "Code Invalid", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.apply = true; // assign the flag to true that help update the fees
            // update the fees with discount, if the promotion code is applied before
            updateFeesWithDiscount();
        });

        // when choose the duration, the expired date should be updated too
        durationComboBox.addActionListener(__ -> {
            // if the member status is active, then the renewal is renewed from the original expiry date
            if (customerDto.getState().equals(CustomerSateEnum.ACTIVE.getName())) {
                updateExpireDateWithExpire();
            } else {
                // if the member status is expired, then the renewal is renewed from the renewal date
                updateExpireDate();
            }
            // if the flag is applied, update the fees with discount or keep original fee
            if (!apply) {
                updateFees();
                return;
            }

            String code = codeTextField.getText();
            Optional<Promotion> promotion = PromotionCodeService.findPromotionCodeOp(code);
            if (promotion.isEmpty()) {
                updateFees();
                return;
            }
            updateFeesWithDiscount();
        });

        // handle the form process of renewal
        renew.addActionListener(__ -> {
            int result = JOptionPane.showConfirmDialog(
                    this,
                    """
                            Are you sure to renew?
                                                    
                            Please follow the steps:
                            1. check the customer paid.
                            """,
                    "Renew Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );

            if (JOptionPane.YES_OPTION == result) {
                int duration = Integer.parseInt((String) durationComboBox.getSelectedItem());
                Date startDate = DateUtil.str2Date(startDateTextField.getText());
                Date expireDate = DateUtil.str2Date(expireTimeValue.getText());
                BigDecimal fees = new BigDecimal(membershipFeesValue.getText());
                customerDto.setDuration(duration);
                customerDto.setStartDate(startDate);
                customerDto.setExpireTime(expireDate);
                customerDto.setFees(fees);

                MembershipService.renew(customerDto);
                this.dispose();
            }
        });

        close.addActionListener(__ -> this.dispose());
    }

    /**
     * this function will calculate expire date based on the renewal date and update the result of the view
     */
    private void updateExpireDate() {
        String startDateTextFieldText = startDateTextField.getText();
        Date date = DateUtil.str2Date(startDateTextFieldText);
        // cover NPE
        if (Objects.isNull(date)) {
            return;
        }
        String expireTime = DateUtil.plusMonths(date, Optional.ofNullable(durationComboBox.getSelectedItem())
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(-1));

        expireTimeValue.setText(expireTime);
    }

    /**
     * this function will calculate expire date based on the original expired date and update the result of the view
     */
    private void updateExpireDateWithExpire() {
        String expireTimeValueText = expireTimeValue.getText();
        Date date = DateUtil.str2Date(expireTimeValueText);
        // cover NPE
        if (Objects.isNull(date)) {
            return;
        }
        String expireTime = DateUtil.plusMonths(date, Optional.ofNullable(durationComboBox.getSelectedItem())
                .map(String.class::cast)
                .map(Integer::parseInt)
                .orElse(-1));

        expireTimeValue.setText(expireTime);
    }

    /**
     * this function will calculate fees based on the membership type, duration
     */
    private void updateFees() {
        String type = memberTextField.getText();
        String duration = (String) durationComboBox.getSelectedItem();
        BigDecimal fees = FeesService.getFees(type, duration);
        discountLabel.setVisible(false);
        discountValue.setVisible(false);
        discountValue.setText("0");
        membershipFeesValue.setText(fees.toString());
    }

    /**
     * sames to the see@updateFees function, specially, when calculate the fees, it will render extra discount info in the view
     */
    private void updateFeesWithDiscount() {
        String type = memberTextField.getText();
        String duration = (String) durationComboBox.getSelectedItem();
        String code = codeTextField.getText();
        BigDecimal[] fees = FeesService.getFees(type, duration, code);

        discountLabel.setVisible(true);
        discountValue.setVisible(true);
        discountValue.setText(fees[0].toString());
        membershipFeesValue.setText(fees[1].toString());
    }
}
