import javax.swing.*;
import java.awt.*;

/**
 * @author Jiaqi Fu
 * <p>
 * This class set the layout and the components of consumption panel
 */
public class ConsumptionView extends MyPanel {
    public ConsumptionView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        // init the consumption table
        MyTable consumptionTable =
                new ConsumptionTable(clubFrameView, "Consumption Table", UIConstant.CONSUMPTION_COLUMNS, ConsumptionService.findConsumptionsForTableRender());

        // set the layout of the panel
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(consumptionTable.getTitle());
        verticalBox.add(consumptionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(consumptionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(consumptionTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
