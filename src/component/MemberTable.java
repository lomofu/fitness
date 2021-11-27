package component;

import bean.Customer;
import data.DataSource;
import data.DataSourceChannel;
import ui.AddMemberDialogView;
import ui.ClubFrameView;
import ui.EditMemberDialogView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Arrays;

import static constant.UIConstant.*;

/**
 * @author lomofu
 * @desc
 * @create 26/Nov/2021 01:05
 */
public class MemberTable extends MyTable implements DataSourceChannel {
  public MemberTable(ClubFrameView clubFrameView, String[] columns, String[][] data) {
    super(clubFrameView, columns, data, MEMBER_SEARCH_FILTER_COLUMNS);
    this.subscribe();
    initMyEvents();
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
    JButton addMemberBtn =
        new TableToolButton(MEMBER_TOOL_LIST[0][0], MyImageIcon.build(MEMBER_TOOL_LIST[0][1]));
    addMemberBtn.addActionListener(e -> AddMemberDialogView.showDig(clubFrameView));

    JButton editMemberBtn =
        new TableToolButton(MEMBER_TOOL_LIST[1][0], MyImageIcon.build(MEMBER_TOOL_LIST[1][1]));
    editMemberBtn.setEnabled(false);
    editMemberBtn.addActionListener(
        e ->
            EditMemberDialogView.showDig(
                clubFrameView, DataSource.getData().get(jTable.getSelectedRow())));

    JButton deleteMemberBtn =
        new TableToolButton(MEMBER_TOOL_LIST[2][0], MyImageIcon.build(MEMBER_TOOL_LIST[2][1]));
    deleteMemberBtn.setEnabled(false);

    JButton filterBtn = new TableToolButton("", MyImageIcon.build(MEMBER_TOOL_LIST[3][1]));
    filterBtn.setToolTipText(MEMBER_TOOL_LIST[3][0]);
    filterBtn.addActionListener(
        e -> {
          if (this.filterBar.isVisible()) {
            filterBtn.setIcon(MyImageIcon.build(MEMBER_TOOL_LIST[3][1]));
            filterBtn.setToolTipText(MEMBER_TOOL_LIST[3][0]);
            this.filterBar.setVisible(false);
            return;
          }

          filterBtn.setIcon(MyImageIcon.build(MEMBER_TOOL_LIST[4][1]));
          filterBtn.setToolTipText(MEMBER_TOOL_LIST[4][0]);
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

    JComboBox<String> memberTypeComboBox = new JComboBox<>(DEFAULT_MEMBERS);
    JComboBox<String> genderComboBox = new JComboBox<>(GENDER_LIST);

    MyDatePicker startTime = new MyDatePicker(1940, LocalDate.now().getYear());
    startTime.setMaximumSize(new Dimension(300, 20));
    MyDatePicker endTime = new MyDatePicker(1940, LocalDate.now().getYear());
    endTime.setMaximumSize(new Dimension(300, 20));

    JButton searchBtn =
        new TableToolButton(MEMBER_TOOL_LIST[5][0], MyImageIcon.build(MEMBER_TOOL_LIST[5][1]));
    searchBtn.setToolTipText(MEMBER_TOOL_LIST[5][0]);
    searchBtn.addActionListener(e->{

      jTable.getRowSorter()




    });



    Box firstRowBox = initFilterFirstRow(comboBoxDim, memberTypeComboBox, genderComboBox);
    Box secondRowBox = initFilterSecondRow(startTime, endTime, searchBtn);

    this.filterBar.addSeparator();
    this.filterBar.add(firstRowBox);
    this.filterBar.add(Box.createVerticalStrut(10));
    this.filterBar.add(secondRowBox);
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

  private Box initFilterSecondRow(MyDatePicker startTime, MyDatePicker endTime, JButton jButton) {
    Box box2 = Box.createHorizontalBox();
    JLabel label = new JLabel("To");
    label.setFont(new Font(label.getFont().getFontName(), Font.BOLD, label.getFont().getSize()));
    box2.add(new JLabel("Start time:"));
    box2.add(startTime);
    box2.add(Box.createHorizontalStrut(10));
    box2.add(label);
    box2.add(Box.createHorizontalStrut(10));
    box2.add(endTime);
    box2.add(Box.createHorizontalStrut(10));
    box2.add(jButton);
    box2.add(Box.createHorizontalGlue());
    return box2;
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
      Customer data = DataSource.getData().get(row);
      TablePopMenu popMenu = new TablePopMenu(clubFrameView, data);
      popMenu.show(e.getComponent(), e.getX(), e.getY());

      return;
    }

    TablePopMenu popMenu = new TablePopMenu(clubFrameView, selectedRows);
    popMenu.show(e.getComponent(), e.getX(), e.getY());
  }

  @Override
  protected void onDoubleClick(MouseEvent e) {
    JTable table = (JTable) e.getSource();
    Point point = e.getPoint();
    int row = table.rowAtPoint(point);
    Customer data = DataSource.getData().get(row);
    if (table.getSelectedRow() != -1) {
      Customer customer = EditMemberDialogView.showDig(clubFrameView, data);
    }
  }

  @Override
  public void onDataChange(Customer e) {
    this.tableModel.addRow(
        new String[] {
          e.getId(),
          e.getFirstName(),
          e.getLastName(),
          e.getDateOfBirth(),
          e.getGender(),
          e.getHomeAddress(),
          e.getPhoneNumber(),
          e.getType(),
          e.getHealthCondition(),
          e.getStartDate(),
          e.getExpireTime()
        });
    this.jTable.validate();
    this.jTable.updateUI();
  }

  @Override
  public void subscribe() {
    DataSource.subscribe(this);
  }
}
