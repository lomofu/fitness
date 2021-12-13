package component;

import bean.DataSourceChannelInfo;
import bean.Promotion;
import constant.DataManipulateEnum;
import core.PromotionCodeService;
import data.DataSource;
import data.DataSourceChannel;
import ui.AddPromotionDialogView;
import ui.ClubFrameView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 04:02
 */
public class PromotionTable extends MyTable implements DataSourceChannel<Promotion> {
    public PromotionTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(Promotion.class);
        initMyEvents();
    }

    private void initMyEvents() {
        this.jTable.getSelectionModel().addListSelectionListener(e -> {
            Component[] jToolBarComponents = jToolBar.getComponents();
            Component removeBtn = jToolBarComponents[2];
            if(jTable.getSelectedRowCount() > 0) {
                removeBtn.setEnabled(true);
            }
        });
        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, HELP_INFO[4], "Help", JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(TABLE_TOOL_LIST[9][1])));
    }

    @Override
    protected void addComponentsToToolBar() {
        JButton addPromotionBtn =
                new TableToolButton(TABLE_TOOL_LIST[0][0], MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
        addPromotionBtn.addActionListener(e -> AddPromotionDialogView.showDig(clubFrameView));

        JButton deletePromotionBtn =
                new TableToolButton(TABLE_TOOL_LIST[2][0], MyImageIcon.build(TABLE_TOOL_LIST[2][1]));
        deletePromotionBtn.setEnabled(false);
        deletePromotionBtn.addActionListener(__ -> removeProm());

        JButton refreshBtn =
                new TableToolButton(TABLE_TOOL_LIST[6][0], MyImageIcon.build(TABLE_TOOL_LIST[6][1]));
        refreshBtn.addActionListener(__ -> {
            deletePromotionBtn.setEnabled(false);
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
            fetchData();
        });

        this.jToolBar.add(addPromotionBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(deletePromotionBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(refreshBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(this.searchBox);
    }

    private void insert() {
        SwingUtilities.invokeLater(() -> {
            fetchData();
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
        });
    }

    private void fetchData() {
        Component[] jToolBarComponents = jToolBar.getComponents();
        jToolBarComponents[2].setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(PromotionCodeService.findMembersForTableRender(), PROMOTION_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
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

        TablePopMenu popMenu = getTablePopMenu();
        popMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    private TablePopMenu getTablePopMenu() {
        return new TablePopMenu() {
            @Override
            void create() {
                JMenuItem remove = new JMenuItem("Remove");
                remove.addActionListener(__ -> removeProm());
                this.add(remove);
            }
        };
    }

    private void removeProm() {
        int result = JOptionPane.showConfirmDialog(
                null,
                """
                        Are you sure to remove?
                                                
                        This operation will also:
                        - remove the consumption records of the account.
                        - remove all family members if it is the main account.
                        """,
                "Remove Warning",
                JOptionPane.YES_NO_CANCEL_OPTION
        );

        if(result == JOptionPane.YES_OPTION) {
            String[] array = Arrays.stream(this.jTable.getSelectedRows())
                    .map(e -> jTable.convertRowIndexToModel(e))
                    .mapToObj(e -> (String) jTable.getModel().getValueAt(e, 0))
                    .toArray(String[]::new);
            PromotionCodeService.remove(array);
        }
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {}

    @Override
    public void onDataChange(Promotion promotion, DataManipulateEnum flag) {
        switch(flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    @Override
    public void subscribe(Class<Promotion> promotionClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, promotionClass));
    }
}
