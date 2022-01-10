package ui;

import component.MyPanel;
import component.MyTable;
import component.PromotionTable;
import core.PromotionCodeService;

import javax.swing.*;
import java.awt.*;

import static constant.UIConstant.PROMOTION_COLUMNS;
import static constant.UIConstant.PROMOTION_SEARCH_FILTER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 03:57
 */
public class PromotionView extends MyPanel {
    public PromotionView(ClubFrameView clubFrameView) {
        super(new BorderLayout());
        MyTable promotionTable =
                new PromotionTable(clubFrameView, "Promotion Table", PROMOTION_COLUMNS, PromotionCodeService.findMembersForTableRender(), PROMOTION_SEARCH_FILTER_COLUMNS);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(promotionTable.getTitle());
        verticalBox.add(promotionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(promotionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(promotionTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
