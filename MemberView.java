import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:56
 */
public class MemberView extends MyPanel {

    public MemberView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        MyTable memberTable =
                new MemberTable(
                        clubFrameView,
                        "Membership Table",
                        UIConstant.MEMBER_COLUMNS,
                        MembershipService.findMembersForTableRender());
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(memberTable.getTitle());
        verticalBox.add(memberTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(memberTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(memberTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
