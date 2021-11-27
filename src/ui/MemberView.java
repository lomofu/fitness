package ui;

import component.MemberTable;
import component.MyPanel;
import component.MyTable;
import data.DataSource;

import javax.swing.*;
import java.awt.*;

import static constant.UIConstant.MEMBER_COLUMNS;

/**
 * @author lomofu
 * @desc
 * @create 23/Nov/2021 11:56
 */
public class MemberView extends MyPanel {

  public MemberView(ClubFrameView clubFrameView) {
    super(new BorderLayout());

    String[][] data =
        DataSource.getData().stream()
            .map(
                e ->
                    new String[] {
                      e.getId(),
                      e.getFirstName(),
                      e.getLastName(),
                      e.getDateOfBirth(),
                      e.getGender(),
                      e.getHomeAddress(),
                      e.getPhoneNumber(),
                      e.getType(),
                      e.getStartDate(),
                      e.getExpireTime()
                    })
            .toArray(size -> new String[size][MEMBER_COLUMNS.length]);

    MyTable memberTable = new MemberTable(clubFrameView, MEMBER_COLUMNS, data);

    Box verticalBox = Box.createVerticalBox();
    verticalBox.add(memberTable.getjToolBar());
    verticalBox.add(Box.createVerticalStrut(10));
    verticalBox.add(memberTable.getFilterBar());

    this.add(verticalBox, BorderLayout.NORTH);
    this.add(memberTable.getjScrollPane(), BorderLayout.CENTER);
  }
}
