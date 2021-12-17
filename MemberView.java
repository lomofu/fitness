import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class set the layout and the components of membership panel
 */
public class MemberView extends MyPanel {

    public MemberView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        // init the member table
        MyTable memberTable =
                new MemberTable(
                        clubFrameView,
                        "Membership Table",
                        UIConstant.MEMBER_COLUMNS,
                        MembershipService.findMembersForTableRender());

        // set the layout of the panel
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(memberTable.getTitle());
        verticalBox.add(memberTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(memberTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(memberTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
