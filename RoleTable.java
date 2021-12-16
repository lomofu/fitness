import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * @author lomofu
 * @desc
 * @create 28/Nov/2021 23:35
 */
public class RoleTable extends MyTable implements DataSourceChannel<RoleDto> {
    public RoleTable(
            ClubFrameView clubFrameView,
            String title,
            String[] columns,
            Object[][] data,
            int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(RoleDto.class);
        initMyEvents();
    }

    @Override
    protected void addComponentsToToolBar() {
        JButton addRoleBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[0][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[0][1]));
        addRoleBtn.addActionListener(e -> RoleDialogView.showDig(clubFrameView));

        JButton editRoleBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[1][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[1][1]));
        editRoleBtn.setEnabled(false);
        editRoleBtn.addActionListener(e -> RoleDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));

        JButton refreshBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[6][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[6][1]));
        refreshBtn.addActionListener(__ -> {
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
            editRoleBtn.setEnabled(false);
            fetchData();
        });

        JButton filterBtn = new TableToolButton("", MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[3][1]));
        filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[3][0]);
        filterBtn.addActionListener(e -> {
            if (this.filterBar.isVisible()) {
                filterBtn.setIcon(MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[3][1]));
                filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[3][0]);
                this.filterBar.setVisible(false);
                return;
            }

            filterBtn.setIcon(MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[4][1]));
            filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[4][0]);
            this.filterBar.setVisible(true);
        });

        this.jToolBar.add(addRoleBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(editRoleBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(refreshBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(this.searchBox);
    }

    private void initMyEvents() {
        this.jTable.getSelectionModel().addListSelectionListener(e -> {
            Component[] jToolBarComponents = jToolBar.getComponents();
            Component editBtn = jToolBarComponents[2];
            Component removeBtn = jToolBarComponents[4];

            if (jTable.getSelectedRowCount() > 0) {
                editBtn.setEnabled(false);
                removeBtn.setEnabled(true);
            }

            if (jTable.getSelectedRowCount() == 1) {
                editBtn.setEnabled(true);
            }
        });

        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, UIConstant.HELP_INFO[2], "Help", JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[9][1])));
    }

    @Override
    protected void addComponentsToFilterBar() {
    }

    @Override
    protected void onRightClick(MouseEvent e) {
        JTable table = (JTable) e.getSource();
        int[] selectedRows = table.getSelectedRows();

        if (selectedRows.length == 0) {
            return;
        }

        Point point = e.getPoint();
        int row = table.rowAtPoint(point);

        if (Arrays.stream(selectedRows).filter(v -> v == row).findAny().isEmpty()) {
            return;
        }

        if (selectedRows.length == 1) {
            table.setRowSelectionInterval(row, row);
            TablePopMenu popMenu = getSingleTablePopMenu();
            popMenu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private TablePopMenu getSingleTablePopMenu() {
        return new TablePopMenu() {
            @Override
            void create() {
                JMenuItem check = new JMenuItem("Role Info");
                JMenuItem edit = new JMenuItem("Edit");

                check.addActionListener(__ -> CheckRoleDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                edit.addActionListener(__ -> RoleDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0)));
                this.add(check);
                this.add(edit);
            }
        };
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {
        CheckRoleDialogView.showDig(clubFrameView, (String) jTable.getModel().getValueAt(jTable.convertRowIndexToModel(jTable.getSelectedRow()), 0));
    }

    @Override
    public void onDataChange(RoleDto e, DataManipulateEnum flag) {
        switch (flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    private void fetchData() {
        Component[] jToolBarComponents = jToolBar.getComponents();
        Component editBtn = jToolBarComponents[2];
        editBtn.setEnabled(false);

        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(RoleService.findRoles(), UIConstant.ROLE_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
    }

    private void insert() {
        SwingUtilities.invokeLater(() -> {
            fetchData();
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
        });
    }

    @Override
    public void subscribe(Class<RoleDto> roleDtoClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, roleDtoClass));
    }
}
