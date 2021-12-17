import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

/**
 * @author lomofu
 *
 * This class set the promotion table and implement related functions
 * <p>
 * extends@MyTable: extends abstract table function
 * implements@DataSourceChannel: let this object be observer to observe the data source change
 * and call back the override onchange functions(mainly update the UI).
 * Meanwhile, it will subscrib the data source when table init
 */
public class PromotionTable extends MyTable implements DataSourceChannel<Promotion> {
    // default table model have full functions
    public PromotionTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(Promotion.class);
        initMyEvents();
    }

    /**
     * This method sets remove events and help events
     */
    private void initMyEvents() {
        this.jTable.getSelectionModel().addListSelectionListener(e -> {
            Component[] jToolBarComponents = jToolBar.getComponents();
            Component removeBtn = jToolBarComponents[2];
            if (jTable.getSelectedRowCount() > 0) {
                removeBtn.setEnabled(true);
            }
        });
        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, UIConstant.HELP_INFO[4], "Help", JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[9][1])));
    }

    /**
     * This method adds some buttons into the toolbar
     */
    @Override
    protected void addComponentsToToolBar() {
        JButton addPromotionBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[0][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[0][1]));
        addPromotionBtn.addActionListener(e -> AddPromotionDialogView.showDig(clubFrameView));

        JButton deletePromotionBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[2][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[2][1]));
        deletePromotionBtn.setEnabled(false);
        deletePromotionBtn.addActionListener(__ -> removeProm());

        JButton refreshBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[6][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[6][1]));
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
        Component[] jToolBarComponents = jToolBar.getComponents();
        jToolBarComponents[2].setEnabled(false);
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(PromotionCodeService.findMembersForTableRender(), UIConstant.PROMOTION_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
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

        TablePopMenu popMenu = getTablePopMenu();
        popMenu.show(e.getComponent(), e.getX(), e.getY());
    }

    // create a select menu with remove option
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

    /**
     * This method will remove the selected promotions
     */
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

        if (result == JOptionPane.YES_OPTION) {
            String[] array = Arrays.stream(this.jTable.getSelectedRows())
                    .map(e -> jTable.convertRowIndexToModel(e))
                    .mapToObj(e -> (String) jTable.getModel().getValueAt(e, 0))
                    .toArray(String[]::new);
            PromotionCodeService.remove(array);
        }
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {
        //do nothing
    }

    /**
     * This method override the observer hook function if the corresponding data in
     * data source has mutable.
     *
     * @param promotion promotion object
     * @param flag action see@DataManipulateEnum
     */
    @Override
    public void onDataChange(Promotion promotion, DataManipulateEnum flag) {
        switch (flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    /**
     * This method subscribe data source when the object is init
     *
     * @param promotionClass Promotion.class
     */
    @Override
    public void subscribe(Class<Promotion> promotionClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, promotionClass));
    }
}
