import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 29/Nov/2021 03:57
 */
public class PromotionView extends MyPanel {
    public PromotionView(ClubFrameView clubFrameView) {
        super(new BorderLayout());
        MyTable promotionTable =
                new PromotionTable(clubFrameView, "Promotion Table", UIConstant.PROMOTION_COLUMNS, PromotionCodeService.findMembersForTableRender(), UIConstant.PROMOTION_SEARCH_FILTER_COLUMNS);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(promotionTable.getTitle());
        verticalBox.add(promotionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(promotionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(promotionTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
