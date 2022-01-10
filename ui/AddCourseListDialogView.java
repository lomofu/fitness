package ui;

import component.CourseTable;
import constant.UIConstant;
import core.CourseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author lomofu
 * <p>
 * This class is used to assign the different role(membership type) with virus choice
 * It is related to the class see @Course and see @Role
 */
public class AddCourseListDialogView extends JDialog {

    private AddCourseListDialogView(Frame owner, RoleDialogView roleDialogView) {
        initDialog(owner);
        CourseTable courseTable = initCourseTable(roleDialogView);

        // Initialize the layout of the table
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(courseTable.getTitle());
        verticalBox.add(courseTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(courseTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(courseTable.getjScrollPane(), BorderLayout.CENTER);
    }

    /**
     * This factory method will create a new dialog when click the "Add Course" Button on the
     * see@RoleDialogView
     *
     * @param owner          the parent component
     * @param roleDialogView it will use the callback function 'setCourseList' to set the selections of course table(select mode)
     */
    public static void showDig(Frame owner, RoleDialogView roleDialogView) {
        AddCourseListDialogView dialog = new AddCourseListDialogView(owner, roleDialogView);
        dialog.setVisible(true);
    }

    private CourseTable initCourseTable(RoleDialogView roleDialogView) {
        // get the selections of the course if we selected before
        List<String> courseListId = roleDialogView.getCourseListId();

        // convert the bean of see@Course to table model
        Object[][] data = CourseService.findSelectCoursesForTableRender(courseListId);

        return new CourseTable(
                roleDialogView,
                this,
                "Courses List",
                UIConstant.COURSE_COLUMNS_SELECTED_MODE,
                data,
                UIConstant.COURSE_SEARCH_FILTER_COLUMNS,
                true,
                2);
    }

    private void initDialog(Frame owner) {
        this.setTitle("Add Courses");
        this.setSize(800, 600);
        this.setPreferredSize(new Dimension(800, 600));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.setAlwaysOnTop(true); // make sure the dialog is on the top of all windows
        this.setModal(true);
    }
}
