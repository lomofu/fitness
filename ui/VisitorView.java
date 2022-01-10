package ui;

import component.MyTable;
import component.VisitorTable;
import core.VisitorService;

import javax.swing.*;
import java.awt.*;

import static constant.UIConstant.VISITOR_COLUMNS;
import static constant.UIConstant.VISITOR_SEARCH_FILTER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 12/Dec/2021 18:56
 */
public class VisitorView extends JPanel {
    public VisitorView(ClubFrameView clubFrameView) {
        super(new BorderLayout());
        MyTable visitorTable =
                new VisitorTable(clubFrameView, "Visitor Table", VISITOR_COLUMNS, VisitorService.findVisitorsForTableRender(), VISITOR_SEARCH_FILTER_COLUMNS);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(visitorTable.getTitle());
        verticalBox.add(visitorTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(visitorTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(visitorTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
