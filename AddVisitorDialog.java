import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * @author lomofu
 * <p>
 * This class is used to add a visitor
 * It will create the dialog when click the add visitor card on the home page
 */
public class AddVisitorDialog extends JDialog {
    // define the default gap for each component in the layout
    private static final int X_GAP = 20;
    private static final int Y_GAP = 20;

    private final JLabel memberIdLabel = new JLabel("Member ID");
    private final JLabel yourAge = new JLabel("Age(greater than 12)");

    private final JTextField memberIDTextField = new JTextField(20);
    private final JButton applyBtn = new JButton("Apply");
    private final Box memberIDBox = Box.createHorizontalBox();
    private final JCheckBox ageCheckBox = new JCheckBox();

    private final MyCard myCard = new MyCard("Member Info", true);
    private final JLabel mainLabel = new JLabel("MID   ");
    private final JLabel mainValue = new JLabel("");
    private final JLabel feesLabel = new JLabel("Fees    ");
    private final JLabel feesValue = new JLabel("0");

    private final JButton submit = new JButton("Submit");
    private final JButton cancel = new JButton("Cancel");
    private final SpringLayout springLayout = new SpringLayout();

    private AddVisitorDialog(Frame owner) {
        super(owner);
        initDialog(owner);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    /**
     * This factory method will create a new dialog when click the add visitor card on the home page
     *
     * @param owner parent component
     */
    public static void showDig(Frame owner) {
        AddVisitorDialog addVisitorDialog = new AddVisitorDialog(owner);
        addVisitorDialog.setVisible(true);
    }

    private void initDialog(Frame owner) {
        this.setTitle("Add Visitor");
        this.setSize(650, 350);
        this.setPreferredSize(new Dimension(650, 350));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.setAlwaysOnTop(true);
        this.setModal(true); // make this dialog on the top
    }

    private void initFormElements() {
        submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        cancel.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        cancel.setForeground(Color.RED);
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel(springLayout);
        memberIDBox.add(memberIDTextField);
        memberIDBox.add(applyBtn);

        initMemberInfoCard();
        initSpringLayout(panel);

        // add the components to the panel
        panel.add(memberIdLabel);
        panel.add(memberIDBox);
        panel.add(yourAge);
        panel.add(ageCheckBox);
        panel.add(myCard);
        panel.add(submit);
        panel.add(cancel);

        return panel;
    }

    private void initMemberInfoCard() {
        // use box for layout
        Box box = getHBox(feesLabel, feesValue);
        Box box1 = getHBox(mainLabel, mainValue);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(box);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(box1);

        // use the Spring to calculate the width of the memberIdLabel + memberIDBox + const X_GAP (20)
        int myCardWidth =
                Spring.sum(Spring.sum(Spring.width(memberIdLabel), Spring.width(memberIDBox)), Spring.constant(X_GAP))
                        .getValue();
        // use the Spring to calculate the height of the verticalBox + const Y_GAP (20) * 3
        int myCardHeight =
                Spring.sum(Spring.height(verticalBox), Spring.constant(Y_GAP * 3)).getValue();

        // set the myCard height and width
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
        // use the Spring to calculate the width of the memberIdLabel width + memberIDBox width + const X_GAP (20)
        Spring childWidth =
                Spring.sum(
                        Spring.sum(Spring.width(memberIdLabel), Spring.width(memberIDBox)),
                        Spring.constant(X_GAP));

        // pack the first row
        initMemberIdRow(panel, childWidth);

        initChildRow(memberIdLabel, yourAge, ageCheckBox);

        // use the spring layout to put constraint
        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, memberIdLabel);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, yourAge);

        springLayout.putConstraint(SpringLayout.WEST, submit, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, submit, Y_GAP, SpringLayout.SOUTH, myCard);

        springLayout.putConstraint(SpringLayout.NORTH, cancel, 0, SpringLayout.NORTH, submit);
        springLayout.putConstraint(SpringLayout.EAST, cancel, 0, SpringLayout.EAST, myCard);
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
                SpringLayout.NORTH, memberIDBox, 0, SpringLayout.NORTH, memberIdLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, memberIDBox, X_GAP, SpringLayout.EAST, memberIdLabel);
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
        cancel.addActionListener(__ -> this.dispose());

        // check if the visitor is over 12 years old and update the result to the view
        ageCheckBox.addActionListener(__ -> updateMemberStateWithAge());

        //  check if the member id is existed and update the result to the view
        applyBtn.addActionListener(__ -> updateMemberStateWithApply());

        // handle the form process of submission
        submit.addActionListener(__ -> {
            String memberId = mainValue.getText();
            boolean isUpper12 = ageCheckBox.isSelected();

            // If the visitor is under 12 years of age and is not accompanied by a member, they will not be able to enter
            if("".equals(memberId) && !isUpper12) {
                JOptionPane.showMessageDialog(
                        this, """
                                No access permission
                                                                
                                Suggestions:
                                1.Any person under 12 years old can use facilities without a fee, if accompanied by a member. 
                                2. A visitor of at least 12 years old can also use facilities without being a member, but by paying a fee for each visit.
                                """, "No access permission", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // If the visitor is over 12 years old and also fills in the membership ID, the operation is invalid
            if(! "".equals(memberId) && isUpper12) {
                JOptionPane.showMessageDialog(
                        this, """
                                Invalid operation
                                                                
                                This is a membership, no necessary to check!
                                                                
                                Suggestions:
                                1.Any person under 12 years old can use facilities without a fee, if accompanied by a member. 
                                2. A visitor of at least 12 years old can also use facilities without being a member, but by paying a fee for each visit.
                                """, "Invalid operation", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int result = JOptionPane.showConfirmDialog(
                    this,
                    """
                            Are you sure to submit?
                                                    
                            Please check the visitor have paid the fees!
                            """,
                    "Submit Warning",
                    JOptionPane.YES_NO_CANCEL_OPTION
            );
            if(result == JOptionPane.YES_OPTION) {
                String fees = feesValue.getText();
                VisitorService.saveVisitorRecord(memberId, fees);
                this.dispose();
            }
        });
    }

    /**
     * this function will check three situations about the application of the membership id:
     *
     * 1. If the Apply button is clicked without filling in the membership ID, the operation is invalid
     * 2. If the member ID is not found after entering it and clicking the Apply button, the application of the member ID fails
     * 3. If the member ID entered exists, the application  is successful
     */
    private void updateMemberStateWithApply() {
        String memberId = memberIDTextField.getText();
        if("".equals(memberId)) {
            memberIDTextField.setText("");
            JOptionPane.showMessageDialog(
                    this, "Member ID cannot be empty", "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        Optional<CustomerDto> op = MembershipService.findCustomerByIdOp(memberId);
        if(op.isPresent()) {
            mainValue.setText(memberId);
            JOptionPane.showMessageDialog(
                    this, "Member ID applied successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(
                    this, "Cannot find this membership", "Failed", JOptionPane.INFORMATION_MESSAGE);
        }
        feesValue.setText("0");
    }

    /**
     * this function will check four situations about the check of the age and the application of the member id:
     *
     * 1. If the visitor is over 12 years old or has not entered a membership ID, a charge will apply
     * 2. If the membership number exists and the visitor is under 12 years old, there is no charge
     * 3. If the member ID does not exist but the visitor is over 12 years old, a charge will apply
     * 4. In case of no operation, the charge shows 0, waiting for other verification
     */
    private void updateMemberStateWithAge() {
        String memberId = memberIDTextField.getText();
        boolean isUpper12 = ageCheckBox.isSelected();
        if("".equals(memberId) || isUpper12) {
            feesValue.setText(DefaultDataConstant.VISITOR_FEES.toString());
        }

        Optional<CustomerDto> op = MembershipService.findCustomerByIdOp(memberId);
        if(op.isPresent() && !isUpper12) {
            feesValue.setText("0");
        } else if(op.isEmpty() && isUpper12) {
            feesValue.setText(DefaultDataConstant.VISITOR_FEES.toString());
        } else {
            feesValue.setText("0");
        }
    }
}
