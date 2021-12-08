package component;

import bean.CustomerSate;
import bean.DataSourceChannelInfo;
import bean.Role;
import data.DataSource;
import data.DataSourceChannel;
import dto.CustomerDto;
import ui.AddMainMemberDialogView;
import ui.AddMemberDialogView;
import ui.ClubFrameView;
import ui.EditMemberDialogView;
import utils.DateUtil;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.*;
import java.util.stream.Stream;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 26/Nov/2021 01:05
 */
public class MemberTable extends MyTable implements DataSourceChannel<CustomerDto> {
  private AddMemberDialogView addMemberDialogView;
  private AddMainMemberDialogView parent;
  private String parentId;

  public MemberTable(ClubFrameView clubFrameView, String title, String[] columns, Object[][] data) {
    super(clubFrameView, title, columns, data, MEMBER_SEARCH_FILTER_COLUMNS);
    this.subscribe(CustomerDto.class);
    initTable();
    initMyEvents();
  }

  public MemberTable(
      AddMemberDialogView addMemberDialogView,
      AddMainMemberDialogView parent,
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
    this.addMemberDialogView = addMemberDialogView;
    this.parent = parent;
  }

  private void initTable() {
    TableColumnModel columnModel = this.jTable.getColumnModel();
    DateRender dateRender = new DateRender();
    StateRender stateRender = new StateRender();
    columnModel.getColumn(3).setCellRenderer(dateRender);
    columnModel.getColumn(9).setCellRenderer(dateRender);
    columnModel.getColumn(10).setCellRenderer(dateRender);
    columnModel.getColumn(12).setCellRenderer(stateRender);
  }

  private void initMyEvents() {
    this.jTable
        .getSelectionModel()
        .addListSelectionListener(
            e -> {
              Component editBtn = jToolBar.getComponents()[2];
              Component removeBtn = jToolBar.getComponents()[4];
              if (jTable.getSelectedRowCount() > 0) {
                editBtn.setEnabled(false);
                removeBtn.setEnabled(true);
              }
              if (jTable.getSelectedRowCount() == 1) {
                editBtn.setEnabled(true);
              }
            });
  }

  @Override
  protected void addComponentsToToolBar() {
    if (selectMode) {
      initSelectMode();
      return;
    }
    initCanEditToolBar();
  }

  private void initSelectMode() {
    JButton confirmCourseBtn =
        new TableToolButton("Confirm", MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
    confirmCourseBtn.addActionListener(
        __ -> {
          DefaultTableModel tableModel = (DefaultTableModel) this.jTable.getModel();
          Optional<String> optional =
              tableModel.getDataVector().stream()
                  .filter(v -> v.get(3) != null)
                  .filter(v -> (Boolean) v.get(3))
                  .map(v -> (String) v.get(0))
                  .findFirst();

          if (optional.isEmpty()) {
            JOptionPane.showMessageDialog(
                this.parent,
                "You must choose least one member!",
                "Warning",
                JOptionPane.INFORMATION_MESSAGE);
            return;
          }

          String parentId = optional.get();
          this.parent.dispose();
          this.addMemberDialogView.setParentId(parentId);
        });

    this.jToolBar.add(confirmCourseBtn);
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(this.searchBox);
  }

  private void initCanEditToolBar() {
    JButton addMemberBtn =
        new TableToolButton(TABLE_TOOL_LIST[0][0], MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
    addMemberBtn.addActionListener(e -> AddMemberDialogView.showDig(clubFrameView));

    JButton editMemberBtn =
        new TableToolButton(TABLE_TOOL_LIST[1][0], MyImageIcon.build(TABLE_TOOL_LIST[1][1]));
    editMemberBtn.setEnabled(false);
    editMemberBtn.addActionListener(
        e ->
            EditMemberDialogView.showDig(
                clubFrameView, DataSource.getCustomerList().get(jTable.getSelectedRow())));

    // TODO DELETE MEMBER
    JButton deleteMemberBtn =
        new TableToolButton(TABLE_TOOL_LIST[2][0], MyImageIcon.build(TABLE_TOOL_LIST[2][1]));
    deleteMemberBtn.setEnabled(false);

    JButton filterBtn = new TableToolButton("", MyImageIcon.build(TABLE_TOOL_LIST[3][1]));
    filterBtn.setToolTipText(TABLE_TOOL_LIST[3][0]);
    filterBtn.addActionListener(
        e -> {
          if (this.filterBar.isVisible()) {
            filterBtn.setIcon(MyImageIcon.build(TABLE_TOOL_LIST[3][1]));
            filterBtn.setToolTipText(TABLE_TOOL_LIST[3][0]);
            this.filterBar.setVisible(false);
            return;
          }

          filterBtn.setIcon(MyImageIcon.build(TABLE_TOOL_LIST[4][1]));
          filterBtn.setToolTipText(TABLE_TOOL_LIST[4][0]);
          this.filterBar.setVisible(true);
        });

    this.jToolBar.add(addMemberBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(editMemberBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(deleteMemberBtn);
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(filterBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(this.searchBox);
  }

  @Override
  protected void addComponentsToFilterBar() {
    Dimension comboBoxDim = new Dimension(100, 40);

    JComboBox<String> memberTypeComboBox =
        new JComboBox<>(
            Stream.concat(
                    Stream.of(""),
                    Arrays.stream(Arrays.stream(DEFAULT_MEMBERS).map(Role::getRoleName).toArray()))
                .toArray(String[]::new));
    JComboBox<String> genderComboBox =
        new JComboBox<>(
            Stream.concat(Stream.of(""), Arrays.stream(MEMBER_GENDER_LIST)).toArray(String[]::new));

    MyDatePicker dateOfBirthStart = new MyDatePicker(1940, LocalDate.now().getYear());
    dateOfBirthStart.setMaximumSize(new Dimension(300, 20));
    MyDatePicker dateOfBirthEnd = new MyDatePicker(1940, LocalDate.now().getYear());
    dateOfBirthEnd.setMaximumSize(new Dimension(300, 20));

    MyDatePicker startTime = new MyDatePicker(1940, LocalDate.now().getYear());
    startTime.setMaximumSize(new Dimension(300, 20));
    MyDatePicker endTime = new MyDatePicker(1940, LocalDate.now().getYear());
    endTime.setMaximumSize(new Dimension(300, 20));

    JButton searchBtn =
        new TableToolButton(TABLE_TOOL_LIST[5][0], MyImageIcon.build(TABLE_TOOL_LIST[5][1]));
    searchBtn.setToolTipText(TABLE_TOOL_LIST[5][0]);
    searchBtn.addActionListener(
        e ->
            filterSearchBtnEvent(
                memberTypeComboBox,
                genderComboBox,
                dateOfBirthStart,
                dateOfBirthEnd,
                startTime,
                endTime));

    JButton removeFilterBtn = new TableToolButton("Remove Filter", Color.RED);
    removeFilterBtn.addActionListener(
        e -> {
          memberTypeComboBox.setSelectedIndex(0);
          genderComboBox.setSelectedIndex(0);
          startTime.reset();
          endTime.reset();
          dateOfBirthStart.reset();
          dateOfBirthEnd.reset();

          jTable.setRowSorter(new TableRowSorter<TableModel>(this.tableModel));
        });

    Box firstRowBox = initFilterFirstRow(comboBoxDim, memberTypeComboBox, genderComboBox);
    Box secondRowBox = initFilterSecondRow(dateOfBirthStart, dateOfBirthEnd);
    Box thirdRowBox = initFilterThirdRow(startTime, endTime, searchBtn, removeFilterBtn);

    this.filterBar.add(firstRowBox);
    this.filterBar.add(Box.createVerticalStrut(10));
    this.filterBar.add(secondRowBox);
    this.filterBar.add(Box.createVerticalStrut(10));
    this.filterBar.add(thirdRowBox);
  }

  private void filterSearchBtnEvent(
      JComboBox<String> memberTypeComboBox,
      JComboBox<String> genderComboBox,
      MyDatePicker dateOfBirthStart,
      MyDatePicker dateOfBirthEnd,
      MyDatePicker startTime,
      MyDatePicker endTime) {
    List<RowFilter<Object, Object>> filters = new ArrayList<>(2);
    String genderItem = (String) genderComboBox.getSelectedItem();
    String memberTypeItem = (String) memberTypeComboBox.getSelectedItem();

    String dateBirthStart = dateOfBirthStart.getDateText();
    String dateBirthEnd = dateOfBirthEnd.getDateText();
    if (!"".equals(dateBirthStart)
        && !"".equals(dateBirthEnd)
        && DateUtil.isAfter(dateBirthStart, dateBirthEnd)) {
      JOptionPane.showMessageDialog(
          this.clubFrameView,
          "Please make sure your 'start time' should be before the 'end time' of Date Of Birth range!",
          "Warning",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    String startTimeDateText = startTime.getDateText();
    String endTimeDateText = endTime.getDateText();
    if (!"".equals(startTimeDateText)
        && !"".equals(endTimeDateText)
        && DateUtil.isAfter(startTimeDateText, endTimeDateText)) {
      JOptionPane.showMessageDialog(
          this.clubFrameView,
          "Please make sure your 'start time' should be before the 'end time' of Start Time range!",
          "Warning",
          JOptionPane.INFORMATION_MESSAGE);
      return;
    }

    if (!"".equals(genderItem)) {
      if (MEMBER_GENDER_LIST[2].equals(genderItem)) {
        filters.add(RowFilter.regexFilter("", 4));
      } else {
        filters.add(RowFilter.regexFilter("^(?i)" + genderItem + "$", 4));
      }
    }
    if (!"".equals(memberTypeItem)) {
      filters.add(RowFilter.regexFilter("^(?i)" + memberTypeItem + "$", 7));
    }

    if (!"".equals(dateOfBirthStart.getDateText())) {
      filters.add(
          RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, dateOfBirthStart.getDate(-1), 3));
    }

    if (!"".equals(dateOfBirthEnd.getDateText())) {
      filters.add(
          RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, dateOfBirthEnd.getDate(1), 3));
    }

    if (!"".equals(startTimeDateText)) {
      filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.AFTER, startTime.getDate(-1), 9));
    }

    if (!"".equals(endTimeDateText)) {
      filters.add(RowFilter.dateFilter(RowFilter.ComparisonType.BEFORE, endTime.getDate(1), 9));
    }

    if (filters.isEmpty()) {
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

  private Box initFilterFirstRow(
      Dimension comboBoxDim,
      JComboBox<String> memberTypeComboBox,
      JComboBox<String> genderComboBox) {
    Box box = getBox("Member Type:", memberTypeComboBox, comboBoxDim);
    Box box1 = getBox("Gender:", genderComboBox, comboBoxDim);

    Box comboBox = Box.createHorizontalBox();
    comboBox.add(box);
    comboBox.add(Box.createHorizontalStrut(10));
    comboBox.add(box1);
    comboBox.add(Box.createHorizontalGlue());
    return comboBox;
  }

  private Box getBox(String label, Component component, Dimension dimension) {
    Box box = Box.createHorizontalBox();
    component.setMaximumSize(dimension);
    box.add(new JLabel(label));
    box.add(component);
    return box;
  }

  private Box initFilterSecondRow(MyDatePicker startTime, MyDatePicker endTime) {
    Box box = Box.createHorizontalBox();
    JLabel label = new JLabel("To");
    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
    box.add(new JLabel("Date Of Birth:"));
    box.add(startTime);
    box.add(Box.createHorizontalStrut(10));
    box.add(label);
    box.add(Box.createHorizontalStrut(10));
    box.add(endTime);
    box.add(Box.createHorizontalGlue());
    return box;
  }

  private Box initFilterThirdRow(
      MyDatePicker startTime, MyDatePicker endTime, JButton searchBtn, JButton removeFilterBtn) {
    Box box = Box.createHorizontalBox();
    JLabel label = new JLabel("To");
    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
    box.add(new JLabel("Start time:"));
    box.add(startTime);
    box.add(Box.createHorizontalStrut(10));
    box.add(label);
    box.add(Box.createHorizontalStrut(10));
    box.add(endTime);
    box.add(Box.createHorizontalStrut(10));
    box.add(searchBtn);
    box.add(Box.createHorizontalStrut(10));
    box.add(removeFilterBtn);
    box.add(Box.createHorizontalGlue());
    return box;
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
      CustomerDto data = DataSource.getCustomerList().get(row);
      TablePopMenu popMenu = getSingleTablePopMenu(data);
      popMenu.show(e.getComponent(), e.getX(), e.getY());

      return;
    }

    TablePopMenu popMenu = getMultiTablePopMenu(selectedRows);
    popMenu.show(e.getComponent(), e.getX(), e.getY());
  }

  private TablePopMenu getSingleTablePopMenu(CustomerDto data) {
    return new TablePopMenu() {
      @Override
      void create() {
        JMenuItem edit = new JMenuItem("Edit");
        JMenuItem remove = new JMenuItem("Remove");

        edit.addActionListener(e -> EditMemberDialogView.showDig(clubFrameView, data));
        // todo remove function
        this.add(edit);
        this.add(remove);
      }
    };
  }

  private TablePopMenu getMultiTablePopMenu(int[] selectedRows) {

    // todo MultiTablePopMenu delete
    return new TablePopMenu() {
      @Override
      void create() {
        JMenuItem remove = new JMenuItem("Remove");
        this.add(remove);
      }
    };
  }

  @Override
  protected void onDoubleClick(MouseEvent e) {
    JTable table = (JTable) e.getSource();
    Point point = e.getPoint();
    int row = table.rowAtPoint(point);
    CustomerDto data = DataSource.getCustomerList().get(row);
    // todo double click
    if (table.getSelectedRow() != -1) {
      EditMemberDialogView.showDig(clubFrameView, data);
    }
  }

  @Override
  public void onDataChange(CustomerDto e) {
    this.tableModel.addRow(
        new Object[] {
          e.getId(),
          e.getFirstName(),
          e.getLastName(),
          e.getDateOfBirth(),
          e.getGender(),
          e.getHomeAddress(),
          e.getPhoneNumber(),
          e.getRole().getRoleName(),
          e.getHealthCondition(),
          e.getStartDate(),
          e.getExpireTime()
        });
    this.jTable.validate();
    this.jTable.updateUI();
  }

  @Override
  public void subscribe(Class<CustomerDto> customerClass) {
    DataSource.subscribe(new DataSourceChannelInfo<>(this, customerClass));
  }

  private static class DateRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      if (row % 2 == 0) setBackground(new Color(213, 213, 213));
      else if (row % 2 == 1) setBackground(Color.white);
      Component c =
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      JLabel original = (JLabel) c;
      original.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
      original.setText(
          Optional.ofNullable(value)
              .filter(Date.class::isInstance)
              .map(Date.class::cast)
              .map(DateUtil::format)
              .orElse(""));
      return original;
    }
  }

  private static class StateRender extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(
        JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
      Component c =
          super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
      JLabel original = (JLabel) c;
      if (original.getText().equals(CustomerSate.EXPIRED.getName())) {
        original.setBackground(Color.RED);
      } else {
        original.setBackground(Color.GREEN);
      }
      original.setForeground(Color.WHITE);
      return original;
    }
  }
}
