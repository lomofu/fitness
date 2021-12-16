import javax.swing.*;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 23:48
 */
public class MyLabel extends JLabel {

  public MyLabel(String s, int right) {
    super(s, right);
  }

  public void setMargin(int top, int left, int bottom, int right) {
    this.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
  }
}
