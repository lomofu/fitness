package component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static constant.UIConstant.DATE_FORMAT;

/**
 * @author lomofu
 * @desc
 * @create 26/Nov/2021 17:24
 */
public class MyDatePicker extends JPanel {
  private static final String[] MONTH_LIST = new String[13];
  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT);

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

  private void initYear(int start, int end) {
    yearCombobox.addItem("");
    for (int i = end; i > start - 1; i--) {
      yearCombobox.addItem(String.valueOf(i));
    }
  }

  private void initComponents() {
    yearCombobox.setMaximumSize(new Dimension(100, 20));
    monthCombobox.setMaximumSize(new Dimension(80, 20));
    dayCombobox.setMaximumSize(new Dimension(80, 20));
    dayCombobox.setEnabled(false);
  }

  private void initListeners() {
    yearCombobox.addActionListener(e -> renderDays());
    monthCombobox.addActionListener(e -> renderDays());
  }

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
    for (int i = 0; i < endDate.lengthOfMonth(); i++) {
      dayCombobox.addItem(String.format("%02d", i + 1));
    }
    dayCombobox.setEnabled(true);
  }

  public String getDate() {
    String year = (String) yearCombobox.getSelectedItem();
    String month = (String) monthCombobox.getSelectedItem();
    String day = (String) dayCombobox.getSelectedItem();
    if (year == null
        || month == null
        || day == null
        || "".equals(year)
        || "".equals(month)
        || "".equals(day)) {
      throw new RuntimeException("Date cannot be empty");
    }
    return LocalDate.parse(year + "-" + month + "-" + day).format(DATE_FORMATTER);
  }
}
