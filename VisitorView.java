import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class sets the layout and the components of the visitor panel
 */
public class VisitorView extends JPanel {
    public VisitorView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        // init the visitor table
        MyTable visitorTable =
                new VisitorTable(clubFrameView, "Visitor Table", UIConstant.VISITOR_COLUMNS,
                        VisitorService.findVisitorsForTableRender(), UIConstant.VISITOR_SEARCH_FILTER_COLUMNS);

        // set the layout of the panel
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(visitorTable.getTitle());
        verticalBox.add(visitorTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(visitorTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(visitorTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
