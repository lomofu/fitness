import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lomofu
 * <p>
 * This class set the course table and implement related functions
 * <p>
 * extends@MyTable: extends abstract table function
 * implements@DataSourceChannel: let this object be observer to observe the data source change
 * and call back the override onchange functions(mainly update the UI).
 * Meanwhile, it will subscrib the data source when table init
 */
public class CourseTable extends MyTable implements DataSourceChannel<Course> {
    private RoleDialogView roleDialogView;
    private AddCourseListDialogView parent;
    private List<Course> courseList;

    // default table model have full functions
    public CourseTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(Course.class);
        initMyEvents();
    }

    // this select model table only have a checkBox and cannot be edited
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

    /**
     * This method set edit events and help events
     */
    private void initMyEvents() {
        this.jTable.getSelectionModel().addListSelectionListener(e -> {
            Component[] jToolBarComponents = jToolBar.getComponents();
            Component editBtn = jToolBarComponents[2];

            // If a course is selected, the Edit button is available
            if (jTable.getSelectedRowCount() > 0) {
                editBtn.setEnabled(false);
            }

            // If one course is selected, the Edit button is available
            if (jTable.getSelectedRowCount() == 1) {
                editBtn.setEnabled(true);
            }
        });
        // Click the help button to view the guidance info
        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, UIConstant.HELP_INFO[3], "Help", JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[9][1])));
    }

    /**
     * This method define two mode: edit course mode or select course mode
     */
    @Override
    protected void addComponentsToToolBar() {
        if (selectMode) {
            initSelectMode();
            return;
        }
        initCanEditToolBar();
    }

    /**
     * define the select mode of the course table
     */
    private void initSelectMode() {
        JButton confirmCourseBtn =
                new TableToolButton("Confirm", MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[0][1]));
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

    /**
     * if the mode is edit mode, init the edit tool bar
     */
    private void initCanEditToolBar() {
        JButton addCourseBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[0][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[0][1]));
        addCourseBtn.addActionListener(e -> CourseDialogView.showDig(clubFrameView));

        JButton editCourseBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[1][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[1][1]));
        editCourseBtn.setEnabled(false);
        editCourseBtn.addActionListener(e ->
                CourseDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));

        JButton refreshBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[6][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[6][1]));
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
    protected void addComponentsToFilterBar() {
        // do nothing
    }

    /**
     * This method is used to monitor right mouse click events
     *
     * @param e mouse event
     */
    @Override
    protected void onRightClick(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int[] selectedRows = table.getSelectedRows();

        // If the mouse is right-clicked without a row selected, no action is taken
        if (selectedRows.length == 0) {
            return;
        }

        Point point = e.getPoint();
        int row = table.rowAtPoint(point);

        // If no right mouse click is made on the selected row, no action is taken.
        if (Arrays.stream(selectedRows).filter(v -> v == row).findAny().isEmpty()) {
            return;
        }

        // If the right mouse click is made with individual rows selected, the Edit menu option is displayed
        if (selectedRows.length == 1) {
            table.setRowSelectionInterval(row, row);
            TablePopMenu popMenu = getSingleTablePopMenu();
            popMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    // create a select menu with edit option
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

    // create a course dialog when double-click the mouse on the selected row
    @Override
    protected void onDoubleClick(MouseEvent e) {
        CourseDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0));
    }

    /**
     * This method is a call back for the insert action
     */
    private void insert() {
        SwingUtilities.invokeLater(() -> {
            fetchData();
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
        });
    }

    /**
     * This method refresh the data from data source
     */
    private void fetchData() {
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(CourseService.findCoursesForTableRender(), UIConstant.COURSE_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
    }

    /**
     * This method override the observer hook function if the corresponding data in
     * data source has mutable.
     *
     * @param course parameter from data source of
     *               which course object has been changed(only have value if is a update operation)
     * @param flag   operation type
     */
    @Override
    public void onDataChange(Course course, DataManipulateEnum flag) {
        switch (flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    /**
     * This method subscribe data source when the object is init
     *
     * @param courseClass Course.class
     */
    @Override
    public void subscribe(Class<Course> courseClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, courseClass));
    }
}
