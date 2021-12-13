package ui;

import component.ConsumptionTable;
import component.MyPanel;
import component.MyTable;
import core.ConsumptionService;

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

        MyTable consumptionTable =
                new ConsumptionTable(clubFrameView, "Consumption Table", CONSUMPTION_COLUMNS, ConsumptionService.findConsumptionsForTableRender());
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(consumptionTable.getTitle());
        verticalBox.add(consumptionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(consumptionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(consumptionTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
