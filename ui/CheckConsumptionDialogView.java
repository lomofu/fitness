package ui;

import component.ConsumptionTable;
import component.MyTable;
import core.ConsumptionService;

import javax.swing.*;
import java.awt.*;

import static constant.UIConstant.CONSUMPTION_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 11/Dec/2021 06:15
 */
public class CheckConsumptionDialogView extends JDialog {
    private CheckConsumptionDialogView(Frame owner, String memberId) {
        initDialog(owner);

        MyTable consumptionTable =
                new ConsumptionTable((ClubFrameView) owner, "Personal Consumption Table", CONSUMPTION_COLUMNS, ConsumptionService.findConsumptionsByMemberIdForRender(memberId), false);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(consumptionTable.getTitle());
        verticalBox.add(consumptionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(consumptionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(consumptionTable.getjScrollPane(), BorderLayout.CENTER);

    }

    public static void showDig(Frame owner, String memberId) {
        CheckConsumptionDialogView dialog = new CheckConsumptionDialogView(owner, memberId);
        dialog.setVisible(true);
    }

    private void initDialog(Frame owner) {
        this.setTitle("Consumption List");
        this.setSize(800, 600);
        this.setPreferredSize(new Dimension(800, 600));
        this.setResizable(true);
        this.setLocationRelativeTo(owner);
    }
}
