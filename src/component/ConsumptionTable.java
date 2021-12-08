package component;

import bean.Consumption;
import bean.DataSourceChannelInfo;
import data.DataSource;
import data.DataSourceChannel;
import ui.ClubFrameView;
import utils.DateUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
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

import static constant.UIConstant.CONSUMPTION_SEARCH_FILTER_COLUMNS;
import static constant.UIConstant.TABLE_TOOL_LIST;

/**
 * @author lomofu
 * @desc
 * @create 26/Nov/2021 02:11
 */
public class ConsumptionTable extends MyTable implements DataSourceChannel<Consumption> {
    public ConsumptionTable(
            ClubFrameView clubFrameView, String title, String[] columns, Object[][] data) {
        super(clubFrameView, title, columns, data, CONSUMPTION_SEARCH_FILTER_COLUMNS);
        this.subscribe(Consumption.class);
        initTable();
    }

    private void initTable() {
        TableColumnModel columnModel = this.jTable.getColumnModel();
        DateRender dateRender = new DateRender();
        FeesRender feesRender = new FeesRender();
        columnModel.getColumn(3).setCellRenderer(dateRender);
        columnModel.getColumn(4).setCellRenderer(feesRender);
    }

    @Override
    protected void addComponentsToToolBar() {
        JButton filterBtn = new TableToolButton("", MyImageIcon.build(TABLE_TOOL_LIST[3][1]));
        filterBtn.setToolTipText(TABLE_TOOL_LIST[3][0]);

        filterBtn.addActionListener(
                e -> {
                    if(this.filterBar.isVisible()) {
                        filterBtn.setIcon(MyImageIcon.build(TABLE_TOOL_LIST[3][1]));
                        filterBtn.setToolTipText(TABLE_TOOL_LIST[3][0]);
                        this.filterBar.setVisible(false);
                        return;
                    }

                    filterBtn.setIcon(MyImageIcon.build(TABLE_TOOL_LIST[4][1]));
                    filterBtn.setToolTipText(TABLE_TOOL_LIST[4][0]);
                    this.filterBar.setVisible(true);
                });

        this.jToolBar.add(Box.createHorizontalGlue());
        this.jToolBar.add(filterBtn);
        this.jToolBar.addSeparator(new Dimension(10, 0));
        this.jToolBar.add(this.searchBox);
    }

    @Override
    protected void addComponentsToFilterBar() {
        Dimension timeComponentMaximumSize = new Dimension(300, 20);
        MyDatePicker createTimeStart = new MyDatePicker(1940, LocalDate.now().getYear());
        createTimeStart.setMaximumSize(timeComponentMaximumSize);
        MyDatePicker createTimeEnd = new MyDatePicker(1940, LocalDate.now().getYear());
        createTimeEnd.setMaximumSize(timeComponentMaximumSize);

        Dimension textFieldMaximumSize = new Dimension(100, 20);
        JTextField feesStart = new JTextField(10);
        feesStart.setMaximumSize(textFieldMaximumSize);
        feesStart.setDocument(new NumberDocument());

        JButton searchBtn =
                new TableToolButton(TABLE_TOOL_LIST[5][0], MyImageIcon.build(TABLE_TOOL_LIST[5][1]));
        searchBtn.setToolTipText(TABLE_TOOL_LIST[5][0]);
        searchBtn.addActionListener(
                e -> filterSearchBtnEvent(createTimeStart, createTimeEnd, feesStart));

        JButton removeFilterBtn = new TableToolButton("Remove Filter", Color.RED);
        removeFilterBtn.addActionListener(
                e -> {
                    feesStart.setText("");
                    createTimeStart.reset();
                    createTimeEnd.reset();

                    jTable.setRowSorter(new TableRowSorter<TableModel>(this.tableModel));
                });

        Box firstRowBox = initFilterFirstRow(createTimeStart, createTimeEnd);
        Box secondBox = initFilterSecondRow(feesStart, searchBtn, removeFilterBtn);

        this.filterBar.add(firstRowBox);
        this.filterBar.add(Box.createVerticalStrut(10));
        this.filterBar.add(secondBox);
    }

    private void filterSearchBtnEvent(
            MyDatePicker createTimeStart, MyDatePicker createTimeEnd, JTextField feesStart) {
        String createTimeStartDateText = createTimeStart.getDateText();
        String createTimeEndDateText = createTimeEnd.getDateText();

        if(! "".equals(createTimeStartDateText)
                && ! "".equals(createTimeEndDateText)
                && DateUtil.isAfter(createTimeStartDateText, createTimeEndDateText)) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "Please make sure your 'start time' should be before the 'end time' of Create Time range!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String feesStartText = feesStart.getText();

        List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
        if(! "".equals(createTimeStartDateText)) {
            filters.add(
                    RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, createTimeStart.getDate(- 1), 3));
        }

        if(! "".equals(createTimeEndDateText)) {
            filters.add(
                    RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, createTimeEnd.getDate(1), 3));
        }

        if(! "".equals(feesStartText)) {
            filters.add(RowFilter.regexFilter("^(?i)" + feesStartText + "$", 4));
        }

        if(filters.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this.clubFrameView,
                    "You must add at least one condition to the filter!",
                    "Warning",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
        sorter.setRowFilter(RowFilter.andFilter(filters));
        jTable.setRowSorter(sorter);
    }

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
    protected void onRightClick(MouseEvent e) {}

    @Override
    protected void onDoubleClick(MouseEvent e) {}

    @Override
    public void onDataChange(Consumption consumption) {}

    @Override
    public void subscribe(Class<Consumption> consumptionClass) {
        DataSource.subscribe(new DataSourceChannelInfo<>(this, consumptionClass));
    }

    private static class DateRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(row % 2 == 0)
                setBackground(new Color(213, 213, 213));
            else if(row % 2 == 1)
                setBackground(Color.white);
            Component c =
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            original.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            Date date = (Date) value;
            original.setText(Optional.ofNullable(date).map(DateUtil::format).orElse(""));
            return original;
        }
    }

    private static class FeesRender extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if(row % 2 == 0)
                setBackground(new Color(213, 213, 213));
            else if(row % 2 == 1)
                setBackground(Color.white);
            Component c =
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel original = (JLabel) c;
            original.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            String fees = (String) value;
            if("".equals(fees)) {
                fees = "0 GBP";
            } else {
                fees = fees + "　GBP";
            }
            original.setText(fees);
            return original;
        }
    }

    private static class NumberDocument extends PlainDocument {
        public void insertString(int var1, String var2, AttributeSet var3)
                throws BadLocationException {
            if(this.isNumeric(var2)) {
                super.insertString(var1, var2, var3);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }

        private boolean isNumeric(String var1) {
            try {
                Long.valueOf(var1);
                return true;
            } catch(NumberFormatException var3) {
                return false;
            }
        }
    }
}
