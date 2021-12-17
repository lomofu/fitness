import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class is used to display a particular membership consumption record
 */
public class CheckConsumptionDialogView extends JDialog {
    private CheckConsumptionDialogView(Frame owner, String memberId) {
        initDialog(owner);

        MyTable consumptionTable =
                new ConsumptionTable((ClubFrameView) owner, "Personal Consumption Table", UIConstant.CONSUMPTION_COLUMNS, ConsumptionService.findConsumptionsByMemberIdForRender(memberId), false);

        // Initialize the layout of the table
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(consumptionTable.getTitle());
        verticalBox.add(consumptionTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(consumptionTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(consumptionTable.getjScrollPane(), BorderLayout.CENTER);

    }

    /**
     * This factory method will create a new dialog when select one membership row and click the consumption button on the
     * see@MemberTable
     * The same option is also support the right click of one membership row
     *
     * @param owner          the parent component
     * @param memberId
     */
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
