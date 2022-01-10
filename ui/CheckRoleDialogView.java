package ui;

import bean.Course;
import component.MyCard;
import core.RoleService;
import dto.RoleDto;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 21:56
 */
public class CheckRoleDialogView extends JDialog {
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

    private final JTextField roleIdTextField = new JTextField(20);
    private final JLabel roleNameTextField = new JLabel();
    private final JLabel feesOf1MonthTextField = new JLabel();
    private final JLabel feesOf3MonthTextField = new JLabel();
    private final JLabel feesOf6MonthTextField = new JLabel();
    private final JLabel feesOf12TextField = new JLabel();
    private final JCheckBox gymCheckBox = new JCheckBox();
    private final JCheckBox swimmingPoolCheckBox = new JCheckBox();
    private final MyCard myCard = new MyCard("Course List", true);
    private final JList<String> courseJList = new JList<>();
    private final JScrollPane courseTextScrollPane = new JScrollPane(courseJList);

    private final JButton close = new JButton("Close");
    private final SpringLayout springLayout = new SpringLayout();

    private Frame owner;

    public CheckRoleDialogView(Frame owner, String id) {
        initDialogSetting(owner);
        initFormElements();
        bindData(id);

        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    public static void showDig(Frame owner, String id) {
        if(checkData(id))
            return;
        CheckRoleDialogView checkRoleDialogView = new CheckRoleDialogView(owner, id);
        checkRoleDialogView.setVisible(true);
    }

    private static boolean checkData(String id) {
        if(id == null || "".equals(id)) {
            JOptionPane.showMessageDialog(
                    null, """
                            This member do not have a role!
                                                        
                            Suggestions:
                            1. Click the edit button on the top of the table or edit menu (Right click).
                            2. Click the transfer button to transfer to a new role for this account.
                            """, "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        Optional<RoleDto> op = RoleService.findRoleDtoByIdOp(id);
        if(op.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "Can not find this role!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true);
        this.setTitle("Role Info");
        this.setSize(550, 650);
        this.setPreferredSize(new Dimension(550, 650));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.owner = owner;
    }

    private void initFormElements() {
        roleIdTextField.setEditable(false);
        roleIdTextField.setFocusable(false);
        roleIdTextField.setBackground(new Color(0, 0, 0, 0));
        roleIdTextField.setBorder(null);

        gymCheckBox.setEnabled(false);
        swimmingPoolCheckBox.setEnabled(false);

        courseTextScrollPane.setBorder(null);
        courseTextScrollPane.setBackground(null);
        courseTextScrollPane.getViewport().setBackground(null);
        courseJList.setEnabled(false);
        courseJList.setOpaque(false);
        courseJList.setBackground(null);
        courseJList.setCellRenderer(new CellRenderer());

        close.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setForeground(Color.RED);
        myCard.addC(courseTextScrollPane);
    }


    private void bindData(String roleId) {
        RoleDto roleDto = RoleService.findRoleDtoByIdOp(roleId).get();
        roleIdTextField.setText(roleDto.getRoleId());
        roleNameTextField.setText(roleDto.getRoleName());
        feesOf1MonthTextField.setText(roleDto.getOneMonth().toString());
        feesOf3MonthTextField.setText(roleDto.getThreeMonth().toString());
        feesOf6MonthTextField.setText(roleDto.getHalfYear().toString());
        feesOf12TextField.setText(roleDto.getFullYear().toString());
        gymCheckBox.setSelected(roleDto.isGym());
        swimmingPoolCheckBox.setSelected(roleDto.isSwimmingPool());
        courseJList.setListData(roleDto.getCourseList().stream().map(Course::getCourseName).toArray(String[]::new));
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel(springLayout);

        initSpringLayout(panel);
        panel.add(roleIdLabel);
        panel.add(roleNameLabel);
        panel.add(feesOf1MonthLabel);
        panel.add(feesOf3MonthLabel);
        panel.add(feesOf6MonthLabel);
        panel.add(feesOf12MonthLabel);
        panel.add(gymLabel);
        panel.add(swimmingPoolLabel);

        panel.add(roleIdTextField);
        panel.add(roleNameTextField);
        panel.add(feesOf1MonthTextField);
        panel.add(feesOf3MonthTextField);
        panel.add(feesOf6MonthTextField);
        panel.add(feesOf12TextField);
        panel.add(gymCheckBox);
        panel.add(swimmingPoolCheckBox);
        panel.add(myCard);

        panel.add(close);
        return panel;
    }

    private void initSpringLayout(JPanel panel) {
        Spring childWidth = Spring.sum(Spring.sum(Spring.width(roleIdLabel), Spring.width(roleIdTextField)),
                Spring.constant(X_GAP));

        initRoleIdRow(panel, childWidth);
        initChildRow(roleIdLabel, roleNameLabel, roleNameTextField);
        initChildRow(roleNameLabel, feesOf1MonthLabel, feesOf1MonthTextField);
        initChildRow(feesOf1MonthLabel, feesOf3MonthLabel, feesOf3MonthTextField);
        initChildRow(feesOf3MonthLabel, feesOf6MonthLabel, feesOf6MonthTextField);
        initChildRow(feesOf6MonthLabel, feesOf12MonthLabel, feesOf12TextField);
        initChildRow(feesOf12MonthLabel, gymLabel, gymCheckBox);
        initChildRow(gymLabel, swimmingPoolLabel, swimmingPoolCheckBox);

        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, feesOf12MonthLabel);
        springLayout.putConstraint(SpringLayout.EAST, myCard, 0, SpringLayout.EAST, roleIdTextField);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, swimmingPoolCheckBox);

        springLayout.putConstraint(SpringLayout.WEST, close, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, close, Y_GAP, SpringLayout.SOUTH, myCard);
    }

    private void initRoleIdRow(JPanel panel, Spring childWidth) {
        springLayout.putConstraint(SpringLayout.WEST, roleIdLabel, - childWidth.getValue() / 2, SpringLayout.HORIZONTAL_CENTER, panel);
        springLayout.putConstraint(SpringLayout.NORTH, roleIdLabel, Y_GAP, SpringLayout.NORTH, panel);

        springLayout.putConstraint(SpringLayout.NORTH, roleIdTextField, 0, SpringLayout.NORTH, roleIdLabel);
        springLayout.putConstraint(SpringLayout.WEST, roleIdTextField, X_GAP, SpringLayout.EAST, roleIdLabel);
    }

    private void initChildRow(JLabel refer, JLabel label, Component component) {
        springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
        springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

        springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
        springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
    }

    private void initListener() {
        close.addActionListener(__ -> {
            this.dispose();
            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        });
    }

    private static class CellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JLabel label = (JLabel) c;
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }
}
