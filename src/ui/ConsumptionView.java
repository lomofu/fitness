package ui;

import component.ConsumptionTable;
import component.MyPanel;
import component.MyTable;

import java.awt.*;

import static constant.UIConstant.CONSUMPTION_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 26/Nov/2021 02:09
 */
public class ConsumptionView extends MyPanel {
  public ConsumptionView(ClubFrameView clubFrameView) {
    super(new BorderLayout());

    String[][] data = {{}};

    MyTable consumptionTable = new ConsumptionTable(clubFrameView, CONSUMPTION_COLUMNS, data);
    this.add(consumptionTable.getjToolBar(), BorderLayout.NORTH);
    this.add(consumptionTable.getjScrollPane(), BorderLayout.CENTER);
  }
}
