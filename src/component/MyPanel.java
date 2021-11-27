package component;

import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 22:52
 */
public class MyPanel extends JPanel {
  public MyPanel() {}
  public MyPanel(LayoutManager layoutManager) {
    super(layoutManager);
  }

  public void setMargin(int top, int left, int bottom, int right) {
    this.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
  }
}
