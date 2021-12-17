import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author Jiaqi Fu
 * <p>
 * This class set the visitor table and implement related functions
 * <p>
 * extends@MyTable: extends abstract table function
 * implements@DataSourceChannel: let this object be observer to observe the data source change
 * and call back the override onchange functions(mainly update the UI).
 * Meanwhile, it will subscrib the data source when table init
 */
public class VisitorTable extends MyTable implements DataSourceChannel<VisitorDto> {
    // default table model have full functions
    public VisitorTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(VisitorDto.class);
    }

    /**
     * This method add some buttons into the toolbar
     */
    @Override
    protected void addComponentsToToolBar() {
        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, UIConstant.HELP_INFO[5], "Help",
                        JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[9][1])));
        JButton refreshBtn = new TableToolButton(UIConstant.TABLE_TOOL_LIST[6][0],
                MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[6][1]));
        refreshBtn.addActionListener(__ -> {
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
            fetchData();
        });
        this.jToolBar.add(refreshBtn);
        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(this.searchBox);
    }

    @Override
    protected void addComponentsToFilterBar() {
        // do nothing
    }

    @Override
    protected void onRightClick(MouseEvent e) {
        // do nothing
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {
        // do nothing
    }

    /**
     * This method override the observer hook function if the corresponding data in
     * data source has mutable.
     *
     * @param visitorDto visitor details
     * @param flag       action see@DataManipulateEnum
     */
    @Override
    public void onDataChange(VisitorDto visitorDto, DataManipulateEnum flag) {
        SwingUtilities.invokeLater(this::fetchData);
    }

    /**
     * This method refresh the data from data source
     */
    private void fetchData() {
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(VisitorService.findVisitorsForTableRender(), UIConstant.VISITOR_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
    }

    /**
     * This method subscribe data source when the object is init
     *
     * @param visitorDtoClass Visitor.class
     */
    @Override
    public void subscribe(Class<VisitorDto> visitorDtoClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, visitorDtoClass));
    }
}
