import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Jiaqi Fu
 * <p>
 * This class is used to create a new course or edit course
 */
public class CourseDialogView extends JDialog implements Validator {
    // define the default gap for each component in the layout
    private static final int X_GAP = 20;
    private static final int Y_GAP = 20;

    private final JLabel courseIdLabel = new JLabel("Course ID");
    private final JLabel courseNameLabel = new JLabel("Course Name");

    private final JTextField courseIdTextField = new JTextField(20);
    private final JTextField courseNameTextField = new JTextField(20);

    private final JButton submit = new JButton("Submit");
    private final JButton close = new JButton("Close");
    private final JButton update = new JButton("update");
    private final SpringLayout springLayout = new SpringLayout();
    private boolean isEdit; // identify the mode is edit course or add new course
    private Course course;

    // default mode is add a new course
    public CourseDialogView(Frame owner) {
        initDialogSetting(owner);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    // If the parameters include the course id, the mode is edit course
    public CourseDialogView(Frame owner, String courseId) {
        initDialogSetting(owner);
        initFormElements();
        bindData(courseId);
        this.isEdit = true;
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    /**
     * This factory method will create a new dialog when click the add button on the
     * see@CourseView
     *
     * @param owner parent component
     */
    public static void showDig(Frame owner) {
        CourseDialogView courseDialogView = new CourseDialogView(owner);
        courseDialogView.setVisible(true);
    }

    /**
     * This factory method will create a new dialog when click the edit button on the
     * see@CourseView
     *
     * @param owner    parent component
     * @param courseId course id
     */
    public static void showDig(Frame owner, String courseId) {
        if (checkData(courseId))
            return;
        CourseDialogView courseDialogView = new CourseDialogView(owner, courseId);
        courseDialogView.setVisible(true);
    }

    /**
     * This method will check if the input course id is existed
     *
     * @param courseId course id
     * @return the check result
     */
    private static boolean checkData(String courseId) {
        Optional<Course> courseOptional = CourseService.findCourseById(courseId);
        if (courseOptional.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "Can not find this course!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

    /**
     * This method will find information about a given course and present the data in the corresponding component
     *
     * @param courseId course id
     */
    private void bindData(String courseId) {
        this.course = CourseService.findCourseById(courseId).get();
        courseIdTextField.setText(course.getCourseId());
        courseNameTextField.setText(course.getCourseName());
    }

    private void initDialogSetting(Frame owner) {
        this.setModal(true);
        this.setTitle("New Course");
        this.setSize(600, 200);
        this.setPreferredSize(new Dimension(600, 200));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
    }

    // set the format of some components
    private void initFormElements() {
        courseIdTextField.setEditable(false);
        courseIdTextField.setFocusable(false);
        courseIdTextField.setBackground(new Color(0, 0, 0, 0));
        courseIdTextField.setBorder(null);
        courseIdTextField.setText(IDUtil.generateId("C"));

        submit.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        update.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        close.setForeground(Color.RED);
    }

    private JPanel initPanel() {
        JPanel panel = new JPanel(springLayout);
        Box box;
        // show update button in edit course mode and submit button in new course mode
        if (isEdit) {
            box = getBox(update, close);
        } else {
            box = getBox(submit, close);
        }

        initSpringLayout(panel, box);

        panel.add(courseIdLabel);
        panel.add(courseNameLabel);
        panel.add(courseIdTextField);
        panel.add(courseNameTextField);
        panel.add(box);
        return panel;
    }

    private Box getBox(Component component, Component component1) {
        Box box = Box.createHorizontalBox();
        box.add(component);
        box.add(Box.createHorizontalStrut(X_GAP));
        box.add(component1);
        return box;
    }

    private void initSpringLayout(JPanel panel, Box box) {
        // use the Spring to calculate the width of the courseIdLabel + courseIdTextField + const X_GAP (20)
        Spring childWidth =
                Spring.sum(
                        Spring.sum(Spring.width(courseIdLabel), Spring.width(courseIdTextField)),
                        Spring.constant(X_GAP));

        // pack the first row
        initRoleIdRow(panel, childWidth);

        initChildRow(courseIdLabel, courseNameLabel, courseNameTextField);

        // use the spring layout to put constraint
        springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, courseNameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, box, Y_GAP, SpringLayout.SOUTH, courseNameTextField);
    }

    private void initRoleIdRow(JPanel panel, Spring childWidth) {
        // HORIZONTAL_CENTER
        springLayout.putConstraint(
                SpringLayout.WEST,
                courseIdLabel,
                -childWidth.getValue() / 2,
                SpringLayout.HORIZONTAL_CENTER,
                panel);
        springLayout.putConstraint(SpringLayout.NORTH, courseIdLabel, Y_GAP, SpringLayout.NORTH, panel);

        springLayout.putConstraint(
                SpringLayout.NORTH, courseIdTextField, 0, SpringLayout.NORTH, courseIdLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, courseIdTextField, X_GAP, SpringLayout.EAST, courseIdLabel);
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

    /**
     * This function manger all the callback events of the components
     */
    private void initListener() {
        submit.addActionListener(__ -> {
            if (!"".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            Course course = new Course();
            course.setCourseId(courseIdTextField.getText());
            course.setCourseName(courseNameTextField.getText());
            CourseService.createNew(course);
            this.dispose();
        });
        update.addActionListener(__ -> {
            if (!"".equals(this.valid())) {
                JOptionPane.showMessageDialog(
                        this, this.valid(), "Invalid Input", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            course.setCourseId(courseIdTextField.getText());
            course.setCourseName(courseNameTextField.getText());
            CourseService.update(course);
            this.dispose();
        });

        close.addActionListener(__ -> this.dispose());
    }

    @Override
    public String valid() {
        // create a validation map, configure an authentication rule for each field.
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("Course Name", new Validation(courseNameTextField.getText(), ValidationEnum.HAS_LEN));

        // call the valid method, the return list will be empty if all inputs are correct
        List<String> errorMsg = ValidationEnum.valid(map);
        if (errorMsg.isEmpty()) {
            return "";
        }
        // only get the top message need to be displayed on the dialog
        return errorMsg.get(0);
    }
}
