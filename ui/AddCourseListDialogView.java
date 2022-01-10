package ui;

import component.CourseTable;
import core.CourseService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static constant.UIConstant.COURSE_COLUMNS_SELECTED_MODE;
import static constant.UIConstant.COURSE_SEARCH_FILTER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 06/Dec/2021 16:28
 */
public class AddCourseListDialogView extends JDialog {
    private AddCourseListDialogView(Frame owner, RoleDialogView roleDialogView) {
        initDialog(owner);
        CourseTable courseTable = initCourseTable(roleDialogView);

        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(courseTable.getTitle());
        verticalBox.add(courseTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(courseTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(courseTable.getjScrollPane(), BorderLayout.CENTER);
    }

    public static void showDig(Frame owner, RoleDialogView roleDialogView) {
        AddCourseListDialogView dialog = new AddCourseListDialogView(owner, roleDialogView);
        dialog.setVisible(true);
    }

    private CourseTable initCourseTable(RoleDialogView roleDialogView) {
        List<String> courseListId = roleDialogView.getCourseListId();

        Object[][] data = CourseService.findSelectCoursesForTableRender(courseListId);

        return new CourseTable(
                roleDialogView,
                this,
                "Courses List",
                COURSE_COLUMNS_SELECTED_MODE,
                data,
                COURSE_SEARCH_FILTER_COLUMNS,
                true,
                2);
    }

    private void initDialog(Frame owner) {
        this.setTitle("Add Courses");
        this.setSize(800, 600);
        this.setPreferredSize(new Dimension(800, 600));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
        this.setAlwaysOnTop(true);
        this.setModal(true);
    }
}
