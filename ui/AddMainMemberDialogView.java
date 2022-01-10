package ui;

import component.MemberTable;
import constant.UIConstant;
import core.MembershipService;

import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class is used to assign the parent membership of the Family membership.
 */
public class AddMainMemberDialogView extends JDialog {
    public AddMainMemberDialogView(AddMemberDialogView addMemberDialogView) {
        initDialog(addMemberDialogView);
        MemberTable memberTable = initMemberTable(addMemberDialogView);

        // Initialize the layout of the table.
        Box verticalBox = Box.createVerticalBox();
        verticalBox.add(memberTable.getTitle());
        verticalBox.add(memberTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(memberTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(memberTable.getjScrollPane(), BorderLayout.CENTER);
    }

    /**
     * This factory method will create a new dialog when click the "Choose Main" Button on the see @AddMemberDialogView.
     *
     * @param addMemberDialogView it will use the callback function 'syncParentInfo' to sync the parent(main family member)
     *                            info (including : membership start date, duration and expired date)
     */
    public static void showDig(AddMemberDialogView addMemberDialogView) {
        AddMainMemberDialogView dialog = new AddMainMemberDialogView(addMemberDialogView);
        dialog.setVisible(true);
    }

    private MemberTable initMemberTable(AddMemberDialogView addMemberDialogView) {
        // get the selections of the main family member ID if we selected before
        String parentId = addMemberDialogView.getParentId();

        // create a select mode membership table in the view
        return new MemberTable(
                addMemberDialogView,
                this,
                "Membership List",
                UIConstant.MEMBER_COLUMNS_SELECTED_MODE,
                MembershipService.findMembersForMainTableRender(parentId),
                UIConstant.MEMBER_COLUMNS_SELECTED_MODE_FILTER_COLUMNS,
                true,
                3);
    }

    private void initDialog(AddMemberDialogView addMemberDialogView) {
        this.setTitle("Add Main Member");
        this.setSize(800, 600);
        this.setPreferredSize(new Dimension(800, 600));
        this.setResizable(true);
        this.setLocationRelativeTo(addMemberDialogView);
        this.setModal(true);
        this.setAlwaysOnTop(true); // make sure the dialog is on the top of all windows
    }
}
