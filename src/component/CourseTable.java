package component;

import bean.Course;
import data.DataSource;
import ui.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static constant.UIConstant.TABLE_TOOL_LIST;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 04:12
 */
public class CourseTable extends MyTable {
  private AddRoleDialogView addRoleDialogView;
  private AddCourseListDialogView parent;
  private List<Course> courseList;

  public CourseTable(
      ClubFrameView clubFrameView,
      String title,
      String[] columns,
      Object[][] data,
      int[] filterColumns) {
    super(clubFrameView, title, columns, data, filterColumns);
  }

  public CourseTable(
      AddRoleDialogView addRoleDialogView,
      AddCourseListDialogView parent,
      String title,
      String[] columns,
      Object[][] data,
      int[] filterColumns,
      boolean selectMode,
      int selectIndex) {
    super(title, columns, data, filterColumns, selectMode, selectIndex);

    TableColumn column = this.jTable.getColumnModel().getColumn(selectIndex);
    column.setMinWidth(60);
    column.setMaxWidth(60);
    column.setResizable(false);
    this.courseList = new ArrayList<>();
    this.addRoleDialogView = addRoleDialogView;
    this.parent = parent;
  }

  @Override
  protected void addComponentsToToolBar() {
    if (selectMode) {
      initSelectMode();
      return;
    }
    initCanEditToolBar();
  }

  private void initSelectMode() {
    JButton confirmCourseBtn =
        new TableToolButton("Confirm", MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
    confirmCourseBtn.addActionListener(
        e -> {
          DefaultTableModel tableModel = (DefaultTableModel) this.jTable.getModel();
          courseList =
              tableModel.getDataVector().stream()
                  .filter(v -> v.get(2) != null)
                  .filter(v -> (Boolean) v.get(2))
                  .map(
                      v -> {
                        Course course = new Course();
                        course.setCourseId((String) v.get(0));
                        course.setCourseName((String) v.get(1));
                        return course;
                      })
                  .collect(Collectors.toList());
          this.parent.dispose();
          this.addRoleDialogView.setCourseList(courseList);
        });
    this.jToolBar.add(confirmCourseBtn);
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(this.searchBox);
  }

  private void initCanEditToolBar() {
    JButton addCourseBtn =
        new TableToolButton(TABLE_TOOL_LIST[0][0], MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
    addCourseBtn.addActionListener(e -> AddCourseDialogView.showDig(clubFrameView));

    JButton editCourseBtn =
        new TableToolButton(TABLE_TOOL_LIST[1][0], MyImageIcon.build(TABLE_TOOL_LIST[1][1]));
    editCourseBtn.setEnabled(false);
    editCourseBtn.addActionListener(
        e ->
            EditMemberDialogView.showDig(
                clubFrameView, DataSource.getCustomerList().get(jTable.getSelectedRow())));

    // TODO DELETE COURSE
    JButton deleteCourseBtn =
        new TableToolButton(TABLE_TOOL_LIST[2][0], MyImageIcon.build(TABLE_TOOL_LIST[2][1]));
    deleteCourseBtn.setEnabled(false);

    this.jToolBar.add(addCourseBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(editCourseBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(deleteCourseBtn);
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(this.searchBox);
  }

  @Override
  protected void addComponentsToFilterBar() {}

  @Override
  protected void onRightClick(MouseEvent e) {}

  @Override
  protected void onDoubleClick(MouseEvent e) {}
}
