import javax.swing.*;
import java.awt.*;

/**
 * @author Jiaqi Fu
 * <p>
 * This class sets the layout and components of the promotion panel
 */
public class PromotionView extends MyPanel {
    public PromotionView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        // init the promotion table
        MyTable promotionTable =
                new PromotionTable(clubFrameView, "Promotion Table", UIConstant.PROMOTION_COLUMNS, PromotionCodeService.findMembersForTableRender(), UIConstant.PROMOTION_SEARCH_FILTER_COLUMNS);

        // set the layout of the panel
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(promotionTable.getTitle());
        verticalBox.add(promotionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(promotionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(promotionTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
