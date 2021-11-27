import data.DataSource;
import ui.ClubFrameView;

import javax.swing.*;
import java.io.IOException;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 12:44
 */
public class ClubMembership {
  public static void main(String[] args) throws IOException {
    DataSource.init();
    SwingUtilities.invokeLater(
        () -> {
          try {
            new ClubFrameView();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
  }
}
