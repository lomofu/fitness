package component;

import bean.DataSourceChannelInfo;
import data.DataSource;
import data.DataSourceChannel;
import dto.RoleDto;
import ui.AddRoleDialogView;
import ui.ClubFrameView;
import ui.EditMemberDialogView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static constant.UIConstant.TABLE_TOOL_LIST;

/**
 * @author lomofu
 * @desc
 * @create 28/Nov/2021 23:35
 */
public class RoleTable extends MyTable implements DataSourceChannel<RoleDto> {
  public RoleTable(
      ClubFrameView clubFrameView,
      String title,
      String[] columns,
      Object[][] data,
      int[] filterColumns) {
    super(clubFrameView, title, columns, data, filterColumns);
    this.subscribe(RoleDto.class);
  }

  @Override
  protected void addComponentsToToolBar() {
    JButton addRoleBtn =
        new TableToolButton(TABLE_TOOL_LIST[0][0], MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
    addRoleBtn.addActionListener(e -> AddRoleDialogView.showDig(clubFrameView));

    JButton editRoleBtn =
        new TableToolButton(TABLE_TOOL_LIST[1][0], MyImageIcon.build(TABLE_TOOL_LIST[1][1]));
    editRoleBtn.setEnabled(false);
    editRoleBtn.addActionListener(
        e ->
            EditMemberDialogView.showDig(
                clubFrameView, DataSource.getCustomerList().get(jTable.getSelectedRow())));

    // TODO DELETE ROLE
    JButton deleteRoleBtn =
        new TableToolButton(TABLE_TOOL_LIST[2][0], MyImageIcon.build(TABLE_TOOL_LIST[2][1]));
    deleteRoleBtn.setEnabled(false);

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

    this.jToolBar.add(addRoleBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(editRoleBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(deleteRoleBtn);
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(filterBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(this.searchBox);
  }

  @Override
  protected void addComponentsToFilterBar() {
    // TODO addComponentsToFilterBar
  }

  @Override
  protected void onRightClick(MouseEvent e) {}

  @Override
  protected void onDoubleClick(MouseEvent e) {}

  @Override
  public void onDataChange(RoleDto e) {
    this.tableModel.addRow(
        new String[] {
          e.getRoleId(),
          e.getRoleName(),
          e.getOneMonth().toString(),
          e.getThreeMonth().toString(),
          e.getHalfYear().toString(),
          e.getFullYear().toString(),
          Boolean.toString(e.isGym()),
          Boolean.toString(e.isSwimmingPool()),
          e.getCourseNameList()
        });
    this.jTable.validate();
    this.jTable.updateUI();
  }

  @Override
  public void subscribe(Class<RoleDto> roleDtoClass) {
    DataSource.subscribe(new DataSourceChannelInfo<>(this, roleDtoClass));
  }
}
