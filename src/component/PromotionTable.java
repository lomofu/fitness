package component;

import data.DataSource;
import ui.AddPromotionDialogView;
import ui.ClubFrameView;
import ui.EditMemberDialogView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

import static constant.UIConstant.TABLE_TOOL_LIST;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 04:02
 */
public class PromotionTable extends MyTable {
  public PromotionTable(
      ClubFrameView clubFrameView,
      String title,
      String[] columns,
      Object[][] data,
      int[] filterColumns) {
    super(clubFrameView, title, columns, data, filterColumns);
  }

  @Override
  protected void addComponentsToToolBar() {
    JButton addPromotionBtn =
        new TableToolButton(TABLE_TOOL_LIST[0][0], MyImageIcon.build(TABLE_TOOL_LIST[0][1]));
    addPromotionBtn.addActionListener(e -> AddPromotionDialogView.showDig(clubFrameView));

    JButton editPromotionBtn =
        new TableToolButton(TABLE_TOOL_LIST[1][0], MyImageIcon.build(TABLE_TOOL_LIST[1][1]));
    editPromotionBtn.setEnabled(false);
    editPromotionBtn.addActionListener(
        e ->
            EditMemberDialogView.showDig(
                clubFrameView, DataSource.getCustomerList().get(jTable.getSelectedRow())));

    // TODO DELETE Promotion
    JButton deletePromotionBtn =
        new TableToolButton(TABLE_TOOL_LIST[2][0], MyImageIcon.build(TABLE_TOOL_LIST[2][1]));
    deletePromotionBtn.setEnabled(false);

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

    this.jToolBar.add(addPromotionBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(editPromotionBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(deletePromotionBtn);
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(filterBtn);
    this.jToolBar.addSeparator(new Dimension(10, 0));
    this.jToolBar.add(this.searchBox);
  }

  @Override
  protected void addComponentsToFilterBar() {}

  @Override
  protected void onRightClick(MouseEvent e) {}

  @Override
  protected void onDoubleClick(MouseEvent e) {}
}
