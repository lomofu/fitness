import data.DataSource;
import ui.ClubFrameView;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 12:44
 */
public class ClubMembership {
  public static void main(String[] args) throws InterruptedException, InvocationTargetException {
    SwingUtilities.invokeAndWait(
        () -> {
          try {
            DataSource.init();
          } catch (IOException e) {
            e.printStackTrace();
          }
        });
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
