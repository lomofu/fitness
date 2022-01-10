package ui;

import bean.Course;
import bean.Validation;
import core.CourseService;
import utils.IDUtil;
import validator.ValidationEnum;
import validator.Validator;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author lomofu
 * @desc
 * @create 07/Dec/2021 03:56
 */
public class CourseDialogView extends JDialog implements Validator {
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
    private boolean isEdit;
    private Course course;

    public CourseDialogView(Frame owner) {
        initDialogSetting(owner);
        initFormElements();
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }


    public CourseDialogView(Frame owner, String courseId) {
        initDialogSetting(owner);
        initFormElements();
        bindData(courseId);
        this.isEdit = true;
        JPanel panel = initPanel();
        initListener();
        this.add(panel);
    }

    public static void showDig(Frame owner) {
        CourseDialogView courseDialogView = new CourseDialogView(owner);
        courseDialogView.setVisible(true);
    }

    public static void showDig(Frame owner, String courseId) {
        if(checkData(courseId))
            return;
        CourseDialogView courseDialogView = new CourseDialogView(owner, courseId);
        courseDialogView.setVisible(true);
    }

    private static boolean checkData(String courseId) {
        Optional<Course> courseOptional = CourseService.findCourseById(courseId);
        if(courseOptional.isEmpty()) {
            JOptionPane.showMessageDialog(
                    null, "Can not find this course!", "Error", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }

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
        if(isEdit) {
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
        Spring childWidth =
                Spring.sum(
                        Spring.sum(Spring.width(courseIdLabel), Spring.width(courseIdTextField)),
                        Spring.constant(X_GAP));

        initRoleIdRow(panel, childWidth);
        initChildRow(courseIdLabel, courseNameLabel, courseNameTextField);

        springLayout.putConstraint(SpringLayout.WEST, box, 0, SpringLayout.WEST, courseNameLabel);
        springLayout.putConstraint(SpringLayout.NORTH, box, Y_GAP, SpringLayout.SOUTH, courseNameTextField);
    }

    private void initRoleIdRow(JPanel panel, Spring childWidth) {
        springLayout.putConstraint(
                SpringLayout.WEST,
                courseIdLabel,
                - childWidth.getValue() / 2,
                SpringLayout.HORIZONTAL_CENTER,
                panel);
        springLayout.putConstraint(SpringLayout.NORTH, courseIdLabel, Y_GAP, SpringLayout.NORTH, panel);

        springLayout.putConstraint(
                SpringLayout.NORTH, courseIdTextField, 0, SpringLayout.NORTH, courseIdLabel);
        springLayout.putConstraint(
                SpringLayout.WEST, courseIdTextField, X_GAP, SpringLayout.EAST, courseIdLabel);
    }

    private void initChildRow(JLabel refer, JLabel label, Component component) {
        springLayout.putConstraint(SpringLayout.EAST, label, 0, SpringLayout.EAST, refer);
        springLayout.putConstraint(SpringLayout.NORTH, label, Y_GAP, SpringLayout.SOUTH, refer);

        springLayout.putConstraint(SpringLayout.NORTH, component, 0, SpringLayout.NORTH, label);
        springLayout.putConstraint(SpringLayout.WEST, component, X_GAP, SpringLayout.EAST, label);
    }

    private void initListener() {
        submit.addActionListener(__ -> {
            if(! "".equals(this.valid())) {
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
            if(! "".equals(this.valid())) {
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
        Map<String, Validation> map = new LinkedHashMap<>();
        map.put("Course Name", new Validation(courseNameTextField.getText(), ValidationEnum.HAS_LEN));

        List<String> errorMsg = ValidationEnum.valid(map);
        if(errorMsg.isEmpty()) {
            return "";
        }
        return errorMsg.get(0);
    }
}
