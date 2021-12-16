import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * @author lomofu
 * @desc
 * @create 12/Dec/2021 19:01
 */
public class VisitorTable extends MyTable implements DataSourceChannel<VisitorDto> {
    public VisitorTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, int[] filterColumns) {
        super(clubFrameView, title, columns, data, filterColumns);
        this.subscribe(VisitorDto.class);
    }

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
    }

    @Override
    protected void onRightClick(MouseEvent e) {
    }

    @Override
    protected void onDoubleClick(MouseEvent e) {
    }

    @Override
    public void onDataChange(VisitorDto visitorDto, DataManipulateEnum flag) {
        SwingUtilities.invokeLater(this::fetchData);
    }

    private void fetchData() {
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(VisitorService.findVisitorsForTableRender(), UIConstant.VISITOR_COLUMNS);
        model.fireTableDataChanged();
        this.jTable.setModel(model);
        super.setTableStyle();
    }

    @Override
    public void subscribe(Class<VisitorDto> visitorDtoClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, visitorDtoClass));
    }
}
