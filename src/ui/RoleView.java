package ui;

import component.MyPanel;
import component.MyTable;
import component.RoleTable;
import core.RoleService;

import javax.swing.*;
import java.awt.*;

import static constant.UIConstant.ROLE_COLUMNS;
import static constant.UIConstant.ROLE_SEARCH_FILTER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 28/Nov/2021 23:06
 */
public class RoleView extends MyPanel {
    public RoleView(ClubFrameView clubFrameView) {
        super(new BorderLayout());
        MyTable roleTable =
                new RoleTable(clubFrameView, "Role Table", ROLE_COLUMNS, RoleService.findRoles(), ROLE_SEARCH_FILTER_COLUMNS);
        Box verticalBox = Box.createVerticalBox();

        verticalBox.add(roleTable.getTitle());
        verticalBox.add(roleTable.getjToolBar());
        verticalBox.add(Box.createVerticalStrut(10));
        verticalBox.add(roleTable.getFilterBar());

        this.add(verticalBox, BorderLayout.NORTH);
        this.add(roleTable.getjScrollPane(), BorderLayout.CENTER);
    }
}
