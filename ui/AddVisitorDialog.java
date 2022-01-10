package ui;

import component.MyCard;
import core.MembershipService;
import core.VisitorService;
import dto.CustomerDto;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

import static constant.DefaultDataConstant.VISITOR_FEES;

/**
 * @author lomofu
 * @desc
 * @create 12/Dec/2021 19:33
 */
public class AddVisitorDialog extends JDialog {
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
        this.setModal(true);
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
        Box box = getHBox(feesLabel, feesValue);
        Box box1 = getHBox(mainLabel, mainValue);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(box);
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(box1);

        int myCardWidth =
                Spring.sum(Spring.sum(Spring.width(memberIdLabel), Spring.width(memberIDBox)), Spring.constant(X_GAP))
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
        Spring childWidth = Spring.sum(Spring.sum(Spring.width(memberIdLabel), Spring.width(memberIDBox)),
                Spring.constant(X_GAP));

        initMemberIdRow(panel, childWidth);
        initChildRow(memberIdLabel, yourAge, ageCheckBox);
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

    private void initChildRow(JLabel refer, JLabel label, Component component) {
        // align with refer
        springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
        springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

        springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
        springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
    }

    private void initListener() {
        cancel.addActionListener(__ -> this.dispose());

        ageCheckBox.addActionListener(__ -> updateMemberStateWithAge());

        applyBtn.addActionListener(__ -> updateMemberStateWithApply());

        submit.addActionListener(__ -> {
            String memberId = mainValue.getText();
            boolean isUpper24 = ageCheckBox.isSelected();

            if("".equals(memberId) && ! isUpper24) {
                JOptionPane.showMessageDialog(
                        this, """
                                No access permission
                                                                
                                Suggestions:
                                1.Any person under 12 years old can use facilities without a fee, if accompanied by a member. 
                                2. A visitor of at least 12 years old can also use facilities without being a member, but by paying a fee for each visit.
                                """, "No access permission", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            if(! "".equals(memberId) && isUpper24) {
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

    private void updateMemberStateWithAge() {
        String memberId = memberIDTextField.getText();
        boolean isUpper24 = ageCheckBox.isSelected();
        if("".equals(memberId) || isUpper24) {
            feesValue.setText(VISITOR_FEES.toString());
        }

        Optional<CustomerDto> op = MembershipService.findCustomerByIdOp(memberId);
        if(op.isPresent() && ! isUpper24) {
            feesValue.setText("0");
        } else if(op.isEmpty() && isUpper24) {
            feesValue.setText(VISITOR_FEES.toString());
        } else {
            feesValue.setText("0");
        }
    }
}
