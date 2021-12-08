package ui;

import component.ConsumptionTable;
import component.MyPanel;
import component.MyTable;
import data.DataSource;
import utils.DateUtil;

import javax.swing.*;
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

    Object[][] data =
        DataSource.getConsumptionList().stream()
            .map(
                e ->
                    new Object[] {
                      e.orderId(),
                      e.consumer(),
                      e.memberId(),
                      DateUtil.str2Date(e.createTime()),
                      e.fees()
                    })
            .toArray(size -> new Object[size][CONSUMPTION_COLUMNS.length]);

    MyTable consumptionTable =
        new ConsumptionTable(clubFrameView, "Consumption Table", CONSUMPTION_COLUMNS, data);
    Box verticalBox = Box.createVerticalBox();

    verticalBox.add(consumptionTable.getTitle());
    verticalBox.add(consumptionTable.getjToolBar());
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(consumptionTable.getFilterBar());

    this.add(verticalBox, BorderLayout.NORTH);
    this.add(consumptionTable.getjScrollPane(), BorderLayout.CENTER);
  }
}
