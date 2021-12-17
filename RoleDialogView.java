import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * * This class is used to create a new role or edit role
 */
public class RoleDialogView extends JDialog implements Validator {
    // define the default gap for each component in the layout
    private static final int X_GAP = 20;
    private static final int Y_GAP = 20;

    private final JLabel roleIdLabel = new JLabel("* Role ID");
    private final JLabel roleNameLabel = new JLabel("* Role Name");
    private final JLabel feesOf1MonthLabel = new JLabel("* Fees of 1 Month");
    private final JLabel feesOf3MonthLabel = new JLabel("* Fees of 3 Month");
    private final JLabel feesOf6MonthLabel = new JLabel("* Fees of 6 Month");
    private final JLabel feesOf12MonthLabel = new JLabel("* Fees of 12 Month");
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
    private final JButton update = new JButton("Update");
    private final JButton submit = new JButton("Submit");
    private final JButton close = new JButton("Close");
    private final SpringLayout springLayout = new SpringLayout();
    private List<Course> courseList = new ArrayList<>();
    private Frame owner;
    private RoleDto roleDto;
    private boolean isEdit;

    // default mode is add a new role
    public RoleDialogView(Frame owner) {
        initDialogSetting(owner);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    // If the parameters include the role id, the mode is edit role
    public RoleDialogView(Frame owner, String roleId) {
        initDialogSetting(owner);
        initFormElements();
        bindData(roleId);
        this.isEdit = true;
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    /**
     * This factory method will create a new dialog when click the add button on the
     * see@RoleView
     *
     * @param owner parent component
     */
    public static void showDig(Frame owner) {
        RoleDialogView roleDialogView = new RoleDialogView(owner);
        roleDialogView.setVisible(true);
    }

    /**
     * This factory method will create a new dialog when click the edit button on the
     * see@RoleView
     *
     * @param owner  parent component
     * @param roleId role id
     */
    public static void showDig(Frame owner, String roleId) {
        if (checkData(roleId))
            return;
        RoleDialogView roleDialogView = new RoleDialogView(owner, roleId);
        roleDialogView.setVisible(true);
    }

    /**
     * This method will check if the input role id is existed
     *
     * @param roleId role id
     * @return the check result
     */
    private static boolean checkData(String roleId) {
        Optional<RoleDto> op = RoleService.findRoleDtoByIdOp(roleId);
        if (op.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "Can not find this role!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * This method will find information about a given role id and present the data in the corresponding component
     *
     * @param roleId role id
     */
    private void bindData(String roleId) {
        this.roleDto = RoleService.findRoleDtoByIdOp(roleId).get();
        roleIdTextField.setText(roleDto.getRoleId());
        roleNameTextField.setText(roleDto.getRoleName());
        feesOf1MonthTextField.setText(roleDto.getOneMonth().toString());
        feesOf3MonthTextField.setText(roleDto.getThreeMonth().toString());
        feesOf6MonthTextField.setText(roleDto.getHalfYear().toString());
        feesOf12TextField.setText(roleDto.getFullYear().toString());
        gymCheckBox.setSelected(roleDto.isGym());
        swimmingPoolCheckBox.setSelected(roleDto.isSwimmingPool());
        this.courseList = roleDto.getCourseList();
        courseJList.setListData(courseList.stream().map(Course::getCourseName).toArray(String[]::new));
    }

    /**
     * This method return a course id list about the given role
     *
     * @return return a course id list
     */
    public List<String> getCourseListId() {
        return courseList.stream().map(Course::getCourseId).collect(Collectors.toList());
    }

    /**
     * This method set the course list of a role
     *
     * @param courseList course list
     */
    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
        this.courseJList.setListData(
                courseList.stream().map(Course::getCourseName).toArray(String[]::new));
        this.courseJList.updateUI();
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true);
        this.setTitle("New Role");
        this.setSize(550, 650);
        this.setPreferredSize(new Dimension(550, 650));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.owner = owner;
    }

    // set the format of some components
    private void initFormElements() {
        roleIdTextField.setEditable(false);
        roleIdTextField.setFocusable(false);
        roleIdTextField.setBackground(new Color(0, 0, 0, 0));
        roleIdTextField.setBorder(null);
        roleIdTextField.setText(IDUtil.generateId("R"));

        feesOf1MonthTextField.setDocument(new NumberDocument());
        feesOf3MonthTextField.setDocument(new NumberDocument());
        feesOf6MonthTextField.setDocument(new NumberDocument());
        feesOf12TextField.setDocument(new NumberDocument());

        courseTextScrollPane.setBorder(null);
        courseTextScrollPane.setBackground(null);
        courseTextScrollPane.getViewport().setBackground(null);
        courseTextScrollPane.setPreferredSize(new Dimension(200, 100));
        courseJList.setEnabled(false);
        courseJList.setOpaque(false);
        courseJList.setBackground(null);
        courseJList.setCellRenderer(new CellRenderer());

        submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setForeground(Color.RED);
        myCard.addC(courseTextScrollPane);
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel(springLayout);
        Box box;

        // show update button in edit role mode and submit button in new role mode
        if (isEdit) {
            box = getBox(update, close);
        } else {
            box = getBox(submit, close);
        }
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
        // use the Spring to calculate the width of the roleIdLabel + roleIdTextField + const X_GAP (20)
        Spring childWidth = Spring.sum(Spring.sum(Spring.width(roleIdLabel), Spring.width(roleIdTextField)),
                Spring.constant(X_GAP));

        // pack the first row
        initRoleIdRow(panel, childWidth);

        initChildRow(roleIdLabel, roleNameLabel, roleNameTextField);
        initChildRow(roleNameLabel, feesOf1MonthLabel, feesOf1MonthTextField);
        initChildRow(feesOf1MonthLabel, feesOf3MonthLabel, feesOf3MonthTextField);
        initChildRow(feesOf3MonthLabel, feesOf6MonthLabel, feesOf6MonthTextField);
        initChildRow(feesOf6MonthLabel, feesOf12MonthLabel, feesOf12TextField);
        initChildRow(feesOf12MonthLabel, gymLabel, gymCheckBox);
        initChildRow(gymLabel, swimmingPoolLabel, swimmingPoolCheckBox);
        initChildRow(swimmingPoolLabel, courseListLabel, courseListBtn);

        // use the spring layout to put constraint
        springLayout.putConstraint(SpringLayout.WEST, myCard, 0, SpringLayout.WEST, courseListLabel);
        springLayout.putConstraint(SpringLayout.EAST, myCard, 0, SpringLayout.EAST, feesOf12TextField);
        springLayout.putConstraint(SpringLayout.NORTH, myCard, Y_GAP, SpringLayout.SOUTH, courseListBtn);
        springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, myCard);
        springLayout.putConstraint(SpringLayout.NORTH, box, Y_GAP, SpringLayout.SOUTH, myCard);
    }

    private void initRoleIdRow(JPanel panel, Spring childWidth) {
        // HORIZONTAL_CENTER
        springLayout.putConstraint(SpringLayout.WEST, roleIdLabel, -childWidth.getValue() / 2, SpringLayout.HORIZONTAL_CENTER, panel);
        springLayout.putConstraint(SpringLayout.NORTH, roleIdLabel, Y_GAP, SpringLayout.NORTH, panel);
        springLayout.putConstraint(SpringLayout.NORTH, roleIdTextField, 0, SpringLayout.NORTH, roleIdLabel);
        springLayout.putConstraint(SpringLayout.WEST, roleIdTextField, X_GAP, SpringLayout.EAST, roleIdLabel);
    }

    /**
     * The function abstract a row with a JLabel and a component (most are the JTextFields)
     *
     * @param refer     the reference label
     * @param label     the label need to put constraint
     * @param component the component need to put constraint
     */
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

    /**
     * This function manger all the callback events of the components
     */
    private void initListener() {
        courseListBtn.addActionListener(e -> AddCourseListDialogView.showDig(this.owner, this));
        submit.addActionListener(e -> {
            if (!"".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            RoleDto roleDto = new RoleDto.Builder()
                    .roleId(roleIdTextField.getText())
                    .roleName(roleNameTextField.getText())
                    .oneMonth(new BigDecimal(feesOf1MonthTextField.getText()))
                    .threeMonth(new BigDecimal(feesOf3MonthTextField.getText()))
                    .halfYear(new BigDecimal(feesOf6MonthTextField.getText()))
                    .fullYear(new BigDecimal(feesOf12TextField.getText()))
                    .courseList(this.courseList)
                    .gym(gymCheckBox.isSelected())
                    .swimmingPool(swimmingPoolCheckBox.isSelected())
                    .build();
            RoleService.createNew(roleDto);
            this.dispose();
        });
        update.addActionListener(__ -> {
            if (!"".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            RoleDto roleDto = new RoleDto.Builder()
                    .roleId(roleIdTextField.getText())
                    .roleName(roleNameTextField.getText())
                    .oneMonth(new BigDecimal(feesOf1MonthTextField.getText()))
                    .threeMonth(new BigDecimal(feesOf3MonthTextField.getText()))
                    .halfYear(new BigDecimal(feesOf6MonthTextField.getText()))
                    .fullYear(new BigDecimal(feesOf12TextField.getText()))
                    .courseList(this.courseList)
                    .gym(gymCheckBox.isSelected())
                    .swimmingPool(swimmingPoolCheckBox.isSelected())
                    .build();
            RoleService.update(roleDto);
            this.dispose();
        });
    }

    @Override
    public String valid() {
        // create a validation map, configure an authentication rule for each field.
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("Role Name", new Validation(roleNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Fees of 1 Month", new Validation(roleNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Fees of 3 Month", new Validation(roleNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Fees of 6 Month", new Validation(roleNameTextField.getText(), ValidationEnum.HAS_LEN));
        map.put("Fees of 12 Month", new Validation(roleNameTextField.getText(), ValidationEnum.HAS_LEN));
        // call the valid method, the return list will be empty if all inputs are correct
        List<String> errorMsg = ValidationEnum.valid(map);
        if (errorMsg.isEmpty()) {
            return "";
        }
        // only get the top message need to be displayed on the dialog
        return errorMsg.get(0);
    }

    // this inner class limits the input document only should be numbed type
    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            // only insert the value to the component if it is a number, otherwise call the system prompt
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

    /**
     * This inner class create a render to center the table content
     */
    private static class CellRenderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            JLabel label = (JLabel) c;
            label.setHorizontalAlignment(SwingConstants.CENTER);
            return label;
        }
    }
}
