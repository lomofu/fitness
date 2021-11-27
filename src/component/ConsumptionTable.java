package component;

import ui.ClubFrameView;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static constant.UIConstant.MEMBER_SEARCH_FILTER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 26/Nov/2021 02:11
 */
public class ConsumptionTable extends MyTable {
  public ConsumptionTable(ClubFrameView clubFrameView, String[] columns, String[][] data) {
    super(clubFrameView, columns, data, MEMBER_SEARCH_FILTER_COLUMNS);
  }

  @Override
  protected void addComponentsToToolBar() {
    this.jToolBar.add(Box.createHorizontalGlue());
    this.jToolBar.add(searchTextField);
  }

  @Override
  protected void addComponentsToFilterBar() {}

  @Override
  protected void onRightClick(MouseEvent e) {}

  @Override
  protected void onDoubleClick(MouseEvent e) {}
}
