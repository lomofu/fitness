package component;

import bean.Course;
import bean.DataSourceChannelInfo;
import constant.DataManipulateEnum;
import core.CourseService;
import data.DataSource;
import data.DataSourceChannel;
import ui.AddCourseListDialogView;
import ui.ClubFrameView;
import ui.CourseDialogView;
import ui.RoleDialogView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 04:12
 */
public class CourseTable extends MyTable implements DataSourceChannel<Course> {
    private RoleDialogView roleDialogView;
    private AddCourseListDialogView parent;
    private List<Course> courseList;

    public CourseTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(Course.class);
        initMyEvents();
    }

    public CourseTable(
            RoleDialogView roleDialogView,
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
        this.roleDialogView = roleDialogView;
        this.parent = parent;
    }

    private void initMyEvents() {
        this.jTable.getSelectionModel().addListSelectionListener(e -> {
            Component[] jToolBarComponents = jToolBar.getComponents();
            Component editBtn = jToolBarComponents[2];

            if(jTable.getSelectedRowCount() > 0) {
                editBtn.setEnabled(false);
            }

            if(jTable.getSelectedRowCount() == 1) {
                editBtn.setEnabled(true);
            }
        });
        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, HELP_INFO[3], "Help", JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(TABLE_TOOL_LIST[9][1])));
    }

    @Override
    protected void addComponentsToToolBar() {
        if(selectMode) {
            initSelectMode();
            return;
        }
        initCanEditToolBar();
    }

    private void initSelectMode() {
        JButton confirmCourseBtn =
                new TableToolButton("Confirm", MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
        confirmCourseBtn.addActionListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) this.jTable.getModel();
            courseList = tableModel.getDataVector().stream()
                    .filter(v -> v.get(2) != null)
                    .filter(v -> (Boolean) v.get(2))
                    .map(v -> {
                        Course course = new Course();
                        course.setCourseId((String) v.get(0));
                        course.setCourseName((String) v.get(1));
                        return course;
                    })
                    .collect(Collectors.toList());
            this.parent.dispose();
            this.roleDialogView.setCourseList(courseList);
        });
        this.jToolBar.add(confirmCourseBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(this.searchBox);
    }

    private void initCanEditToolBar() {
        JButton addCourseBtn =
                new TableToolButton(TABLE_TOOL_LIST[0][0], MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
        addCourseBtn.addActionListener(e -> CourseDialogView.showDig(clubFrameView));

        JButton editCourseBtn =
                new TableToolButton(TABLE_TOOL_LIST[1][0], MyImageIcon.build(TABLE_TOOL_LIST[1][1]));
        editCourseBtn.setEnabled(false);
        editCourseBtn.addActionListener(e ->
                CourseDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));

        JButton refreshBtn =
                new TableToolButton(TABLE_TOOL_LIST[6][0], MyImageIcon.build(TABLE_TOOL_LIST[6][1]));
        refreshBtn.addActionListener(__ -> {
            editCourseBtn.setEnabled(false);
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
            fetchData();
        });

        this.jToolBar.add(addCourseBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(editCourseBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(refreshBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(this.searchBox);
    }

    @Override
    protected void addComponentsToFilterBar() {}

    @Override
    protected void onRightClick(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int[] selectedRows = table.getSelectedRows();

        if(selectedRows.length == 0) {
            return;
        }

        Point point = e.getPoint();
        int row = table.rowAtPoint(point);

        if(Arrays.stream(selectedRows).filter(v -> v == row).findAny().isEmpty()) {
            return;
        }

        if(selectedRows.length == 1) {
            table.setRowSelectionInterval(row, row);
            TablePopMenu popMenu = getSingleTablePopMenu();
            popMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private TablePopMenu getSingleTablePopMenu() {
        return new TablePopMenu() {
            @Override
            void create() {
                JMenuItem edit = new JMenuItem("Edit");
                edit.addActionListener(__ ->
                        CourseDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                this.add(edit);
            }
        };
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {
        CourseDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0));
    }

    private void insert() {
        SwingUtilities.invokeLater(() -> {
            fetchData();
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
        });
    }

    private void fetchData() {
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(CourseService.findCoursesForTableRender(), COURSE_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
    }

    @Override
    public void onDataChange(Course course, DataManipulateEnum flag) {
        switch(flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    @Override
    public void subscribe(Class<Course> courseClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, courseClass));
    }
}
