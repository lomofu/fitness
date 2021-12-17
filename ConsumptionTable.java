import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Jiaqi Fu
 * <p>
 * This class set the consumption table and implement related functions
 * <p>
 * extends@MyTable: extends abstract table function
 * implements@DataSourceChannel: let this object be observer to observe the data source change
 * and call back the override onchange functions(mainly update the UI).
 * Meanwhile, it will subscrib the data source when table init
 */
public class ConsumptionTable extends MyTable implements DataSourceChannel<Consumption> {
    private boolean flag = true; // flag the table is a select mode or not, true is default mode, opposite is select mode

    // default mode have full functions
    public ConsumptionTable(
            ClubFrameView clubFrameView, String title, String[] columns, Object[][] data) {
        super(clubFrameView, title, columns, data, UIConstant.CONSUMPTION_SEARCH_FILTER_COLUMNS);
        this.subscribe(Consumption.class);
        initTable();
    }

    // this select mode table only have a checkBox and cannot be edited
    public ConsumptionTable(
            ClubFrameView clubFrameView, String title, String[] columns, Object[][] data, boolean flag) {
        super(clubFrameView, title, columns, data, UIConstant.CONSUMPTION_SEARCH_FILTER_COLUMNS);
        this.flag = flag;
        this.subscribe(Consumption.class);
        initTable();
    }

    private void initTable() {
        TableColumnModel columnModel = this.jTable.getColumnModel();
        DateRender dateRender = new DateRender();
        FeesRender feesRender = new FeesRender();
        // use customize renders
        columnModel.getColumn(3).setCellRenderer(dateRender);
        columnModel.getColumn(4).setCellRenderer(feesRender);
    }

    /**
     * This method defines the buttons with handle actions in the toolbar
     */
    @Override
    protected void addComponentsToToolBar() {
        // help button
        this.helpBtn.addActionListener(e ->
                JOptionPane.showMessageDialog(this.jScrollPane, UIConstant.HELP_INFO[1], "Help", JOptionPane.QUESTION_MESSAGE, MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[9][1])));

        // refresh button
        JButton refreshBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[6][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[6][1]));
        refreshBtn.addActionListener(__ -> {
            Rectangle rect = jTable.getCellRect(0, 0, true);
            jTable.scrollRectToVisible(rect);
            fetchData();
        });

        // filter button
        JButton filterBtn = new TableToolButton("", MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[3][1]));
        filterBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[3][0]);
        filterBtn.addActionListener(
                e -> {
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

        // use flag to decide toolbar render
        if (flag) {
            this.jToolBar.add(refreshBtn);
            this.jToolBar.add(Box.createHorizontalGlue());
            this.jToolBar.add(filterBtn);
            this.jToolBar.addSeparator(new Dimension(10, 0));
        } else {
            this.jToolBar.add(Box.createHorizontalGlue());
        }
        // add the searchBox which implements in the see@MyTable
        this.jToolBar.add(this.searchBox);
    }

    /**
     * This method define the components with handle actions in the filter
     */
    @Override
    protected void addComponentsToFilterBar() {
        // date picker
        Dimension timeComponentMaximumSize = new Dimension(300, 20);
        MyDatePicker createTimeStart = new MyDatePicker(1940, LocalDate.now().getYear());
        createTimeStart.setMaximumSize(timeComponentMaximumSize);
        MyDatePicker createTimeEnd = new MyDatePicker(1940, LocalDate.now().getYear());
        createTimeEnd.setMaximumSize(timeComponentMaximumSize);

        // fees input text field
        Dimension textFieldMaximumSize = new Dimension(100, 20);
        JTextField feesStart = new JTextField(10);
        feesStart.setMaximumSize(textFieldMaximumSize);
        feesStart.setDocument(new NumberDocument());

        // search button for the filters
        JButton searchBtn =
                new TableToolButton(UIConstant.TABLE_TOOL_LIST[5][0], MyImageIcon.build(UIConstant.TABLE_TOOL_LIST[5][1]));
        searchBtn.setToolTipText(UIConstant.TABLE_TOOL_LIST[5][0]);
        searchBtn.addActionListener(
                e -> filterSearchBtnEvent(createTimeStart, createTimeEnd, feesStart));

        // remove filter button
        JButton removeFilterBtn = new TableToolButton("Remove Filter", Color.RED);
        removeFilterBtn.addActionListener(
                e -> {
                    // reset components state
                    feesStart.setText("");
                    createTimeStart.reset();
                    createTimeEnd.reset();

                    // set default row sorter
                    jTable.setRowSorter(new TableRowSorter<TableModel>(this.tableModel));
                });

        Box firstRowBox = initFilterFirstRow(createTimeStart, createTimeEnd);
        Box secondBox = initFilterSecondRow(feesStart, searchBtn, removeFilterBtn);

        this.filterBar.add(firstRowBox);
        this.filterBar.add(Box.createVerticalStrut(10));
        this.filterBar.add(secondBox);
    }

    /**
     * This method handle filter actions
     *
     * @param createTimeStart date picker
     * @param createTimeEnd   date picker
     * @param feesStart       text field
     */
    private void filterSearchBtnEvent(
            MyDatePicker createTimeStart, MyDatePicker createTimeEnd, JTextField feesStart) {
        String createTimeStartDateText = createTimeStart.getDateText();
        String createTimeEndDateText = createTimeEnd.getDateText();

        // make sure start time should be before the end time of Create Time range
        if (!"".equals(createTimeStartDateText)
                && !"".equals(createTimeEndDateText)
                && DateUtil.isAfter(createTimeStartDateText, createTimeEndDateText)) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "Please make sure your 'start time' should be before the 'end time' of Create Time range!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String feesStartText = feesStart.getText();

        // define a collection of row filter
        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
        // add create start time filter only value is not empty
        if (!"".equals(createTimeStartDateText)) {
            filters.add(
                    RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, createTimeStart.getDate(-1), 3));
        }
        // add create end time filter only value is not empty
        if (!"".equals(createTimeEndDateText)) {
            filters.add(
                    RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, createTimeEnd.getDate(1), 3));
        }

        // add fees filter only value is not empty
        if (!"".equals(feesStartText)) {
            filters.add(RowFilter.regexFilter("^(?i)" + feesStartText + "$", 4));
        }

        // filters will not work if is empty
        if (filters.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "You must add at least one condition to the filter!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // add customize filters in table row sorter
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
        sorter.setRowFilter(RowFilter.andFilter(filters));
        jTable.setRowSorter(sorter);
    }

    /**
     * This method draw the components' layout of the first row of the filter
     *
     * @param createTimeStart date picker
     * @param createTimeEnd   date picker
     * @return Box type
     */
    private Box initFilterFirstRow(MyDatePicker createTimeStart, MyDatePicker createTimeEnd) {
        Box box = Box.createHorizontalBox();
        JLabel label = new JLabel("To");
        label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
        box.add(new JLabel("Create Time:"));
        box.add(createTimeStart);
        box.add(Box.createHorizontalStrut(10));
        box.add(label);
        box.add(Box.createHorizontalStrut(10));
        box.add(createTimeEnd);
        box.add(Box.createHorizontalGlue());
        return box;
    }

    /**
     * This method draw the components' layout of the first row of the filter
     *
     * @param feesStar        text field
     * @param removeFilterBtn remove button
     * @param searchBtn       search button
     * @return Box type
     */
    private Box initFilterSecondRow(
            JTextField feesStar, JButton searchBtn, JButton removeFilterBtn) {
        Box box = Box.createHorizontalBox();
        box.add(new JLabel("Fees:"));
        box.add(feesStar);
        box.add(Box.createHorizontalStrut(10));
        box.add(searchBtn);
        box.add(Box.createHorizontalStrut(10));
        box.add(removeFilterBtn);
        box.add(Box.createHorizontalGlue());
        return box;
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
     * This method overrides the observer hook function if the corresponding data in
     * data source has mutable.
     *
     * @param consumption parameter from data source of
     *                    which consumptions object has been changed(only have value if is a update operation)
     * @param flag        operation type
     */
    @Override
    public void onDataChange(Consumption consumption, DataManipulateEnum flag) {
        switch (flag) {
            case INSERT -> insert();
            case UPDATE, DELETE -> SwingUtilities.invokeLater(this::fetchData);
        }
    }

    /**
     * This method is a call back for the insert action
     */
    private void insert() {
        SwingUtilities.invokeLater(() -> {
            fetchData();
            Rectangle rect = jTable.getCellRect(0, 0, true); // scroll to the top of the table
            jTable.scrollRectToVisible(rect);
        });
    }

    /**
     * This method refresh the data from data source
     */
    private void fetchData() {
        // update model
        DefaultTableModel model = (DefaultTableModel) this.jTable.getModel();
        model.setDataVector(ConsumptionService.findConsumptionsForTableRender(), UIConstant.CONSUMPTION_COLUMNS);
        model.fireTableDataChanged(); // notify data change
        this.jTable.setModel(model);
        // repaint style
        super.setTableStyle();
        initTable();
    }

    /**
     * This method subscribe data source when the object is init
     *
     * @param consumptionClass Consumption.class
     */
    @Override
    public void subscribe(Class<Consumption> consumptionClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, consumptionClass));
    }

    /**
     * This class define how to render the date column
     */
    private static class DateRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row % 2 == 0) // set alternate colours
                setBackground(new Color(213, 213, 213));
            else if (row % 2 == 1)
                setBackground(Color.white);
            Component c =
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            original.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            Date date = (Date) value;
            // format the date
            original.setText(Optional.ofNullable(date).map(DateUtil::format).orElse(""));
            return original;
        }
    }

    /**
     * This inner class define fees column
     */
    private static class FeesRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (row % 2 == 0) // set alternate colours
                setBackground(new Color(213, 213, 213));
            else if (row % 2 == 1)
                setBackground(Color.white);
            Component c =
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            original.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            String fees = (String) value;
            // format the fees
            if ("".equals(fees)) {
                fees = "0 GBP";
            } else {
                fees = fees + "　GBP";
            }
            original.setText(fees);
            return original;
        }
    }

    // this inner class limits the input document only should be numbed type
    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            if (this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch (NumberFormatException var3) {
                return false;
            }
        }
    }
}
