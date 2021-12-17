import javax.swing.*;
import java.awt.*;

/**
 * @author Jiaqi Fu
 * <p>
 * This class sets the layout and the components of the role panel
 */
public class RoleView extends MyPanel {
    public RoleView(ClubFrameView clubFrameView) {
        super(new BorderLayout());

        // init the role table
        MyTable roleTable =
                new RoleTable(clubFrameView, "Role Table", UIConstant.ROLE_COLUMNS, RoleService.findRoles(), UIConstant.ROLE_SEARCH_FILTER_COLUMNS);

        // set the layout of the panel
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(roleTable.getTitle());
        verticalBox.add(roleTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(roleTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(roleTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
