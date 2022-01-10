package component;

import utils.DateUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Date;
import java.util.stream.IntStream;

/**
 * @author lomofu
 * <p>
 * This class defines a common date picker for date choose
 */
public class MyDatePicker extends JPanel {
    // set the month list with %02d format
    private static final String[] MONTH_LIST = new String[13];

    static {
        MONTH_LIST[0] = "";
        for (int i = 1; i < 13; i++) {
            MONTH_LIST[i] = String.format("%02d", i);
        }
    }

    private final JComboBox<String> yearCombobox = new JComboBox<>();
    private final JComboBox<String> monthCombobox = new JComboBox<>(MONTH_LIST);
    private final JComboBox<String> dayCombobox = new JComboBox<>();

    public MyDatePicker(int... range) {
        this.setLayout(new GridLayout(1, 3));
        this.setBackground(null);
        initYear(range[0], range[1]);
        initComponents();
        initListeners();

        this.add(getBox("Year", yearCombobox));
        this.add(getBox("Month", monthCombobox));
        this.add(getBox("Day", dayCombobox));
    }

    private Box getBox(String label, Component component) {
        Box box = Box.createHorizontalBox();
        box.add(component);
        box.add(new JLabel(label));
        return box;
    }

    /**
     * This method sets year comboBox based on the given start and end year.
     *
     * @param start start year
     * @param end   end year
     */
    private void initYear(int start, int end) {
        yearCombobox.addItem("");
        IntStream.range(start - 1, end)
                .map(i -> end - i + start - 1)
                .forEachOrdered(i -> yearCombobox.addItem(String.valueOf(i)));
    }

    // set the size of each component
    private void initComponents() {
        yearCombobox.setMaximumSize(new Dimension(100, 20));
        monthCombobox.setMaximumSize(new Dimension(80, 20));
        dayCombobox.setMaximumSize(new Dimension(80, 20));
        dayCombobox.setEnabled(false);
    }

    // set the action listener
    private void initListeners() {
        yearCombobox.addActionListener(e -> renderDays());
        monthCombobox.addActionListener(e -> renderDays());
    }

    /**
     * This method sets day comboBox based on the selected year and month
     */
    private void renderDays() {
        String year = (String) yearCombobox.getSelectedItem();
        String month = (String) monthCombobox.getSelectedItem();
        if (year == null || month == null || "".equals(year) || "".equals(month)) {
            dayCombobox.removeAllItems();
            dayCombobox.setEnabled(false);
            return;
        }
        LocalDate endDate = LocalDate.parse(year + "-" + month + "-" + "01");
        dayCombobox.removeAllItems();
        IntStream.range(0, endDate.lengthOfMonth())
                .forEachOrdered(e -> dayCombobox.addItem(String.format("%02d", e + 1)));
        dayCombobox.setEnabled(true);
    }

    /**
     * This method gets the whole date with special format
     *
     * @return string
     */
    public String getDateText() {
        String year = (String) yearCombobox.getSelectedItem();
        String month = (String) monthCombobox.getSelectedItem();
        String day = (String) dayCombobox.getSelectedItem();
        if (year == null
                || month == null
                || day == null
                || "".equals(year)
                || "".equals(month)
                || "".equals(day)) {
            return "";
        }
        return DateUtil.format(year, month, day);
    }

    /**
     * This method gets the whole date with Date format
     *
     * @return Date format
     */
    public Date getDate() {
        String year = (String) yearCombobox.getSelectedItem();
        String month = (String) monthCombobox.getSelectedItem();
        String day = (String) dayCombobox.getSelectedItem();
        if (year == null
                || month == null
                || day == null
                || "".equals(year)
                || "".equals(month)
                || "".equals(day)) {
            return null;
        }
        return DateUtil.toDate(year, month, day);
    }

    /**
     * This method sets date picker items to display given date
     *
     * @param date Date format
     */
    public void setDate(Date date) {
        if (date == null) {
            return;
        }
        String[] split = DateUtil.split(date);
        dayCombobox.setSelectedItem(split[0]);
        monthCombobox.setSelectedItem(split[1]);
        yearCombobox.setSelectedItem(split[2]);
    }

    /**
     * This method sets date picker items to display now date
     */
    public void setNow() {
        String[] split = DateUtil.split(new Date());
        dayCombobox.setSelectedItem(split[0]);
        monthCombobox.setSelectedItem(split[1]);
        yearCombobox.setSelectedItem(split[2]);
    }

    /**
     * This method sets the edit ability of each comboBox of the date picker
     */
    public void setUnLock() {
        String day = (String) dayCombobox.getSelectedItem();
        this.setEnabled(true);
        yearCombobox.setEnabled(true);
        monthCombobox.setEnabled(true);
        dayCombobox.setEnabled(day != null && !"".equals(day));
    }

    /**
     * This method lock all comboBox to cannot be select
     */
    public void setLock() {
        this.setEnabled(false);
        dayCombobox.setEnabled(false);
        monthCombobox.setEnabled(false);
        yearCombobox.setEnabled(false);
    }

    /**
     * JTable's default row sorter only allows greater than or less than when comparing date types,
     * but does not contain this date.
     * Therefore, if you want to include the date entered within the filter range,
     * you can manually offset both ranges to the left and right by the specified number of days.
     * see@DateUtil
     *
     * @param offset date number need offset
     * @return offset date
     */
    public Date getDate(int offset) {
        String year = (String) yearCombobox.getSelectedItem();
        String month = (String) monthCombobox.getSelectedItem();
        String day = (String) dayCombobox.getSelectedItem();
        if (year == null
                || month == null
                || day == null
                || "".equals(year)
                || "".equals(month)
                || "".equals(day)) {
            return null;
        }
        return DateUtil.str2Date(year, month, day, offset);
    }

    /**
     * This method reset the date picker with default items
     */
    public void reset() {
        yearCombobox.setSelectedIndex(0);
        monthCombobox.setSelectedIndex(0);
        dayCombobox.removeAllItems();
        dayCombobox.setEnabled(false);
    }

    // use action listener to listen for day comboBox condition
    public void onSelect(ActionListener actionListener) {
        dayCombobox.addActionListener(actionListener);
    }
}
