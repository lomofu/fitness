package ui;

import component.CourseTable;
import data.DataSource;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 06/Dec/2021 16:28
 */
public class AddCourseListDialogView extends JDialog {
  private AddCourseListDialogView(Frame owner, AddRoleDialogView addRoleDialogView) {
    initDialog(owner);
    CourseTable courseTable = initCourseTable(addRoleDialogView);

    Box verticalBox = Box.createVerticalBox();
    verticalBox.add(courseTable.getTitle());
    verticalBox.add(courseTable.getjToolBar());
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(courseTable.getFilterBar());

    this.add(verticalBox, BorderLayout.NORTH);
    this.add(courseTable.getjScrollPane(), BorderLayout.CENTER);
  }

  public static void showDig(Frame owner, AddRoleDialogView addRoleDialogView) {
    AddCourseListDialogView dialog = new AddCourseListDialogView(owner, addRoleDialogView);
    dialog.setVisible(true);
  }

  private CourseTable initCourseTable(AddRoleDialogView addRoleDialogView) {
    List<String> courseListId = addRoleDialogView.getCourseListId();

    Object[][] data =
        DataSource.getCourseList().stream()
            .map(
                e ->
                    new Object[] {
                      e.getCourseId(), e.getCourseName(), courseListId.contains(e.getCourseId())
                    })
            .toArray(size -> new Object[size][COURSE_COLUMNS.length]);

    return new CourseTable(
        addRoleDialogView,
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
  }
}
