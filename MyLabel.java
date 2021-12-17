import javax.swing.*;

/**
 * @author Jiaqi Fu
 * <p>
 * This class defines a common JLabel for use
 */
public class MyLabel extends JLabel {

    public MyLabel(String s, int right) {
        super(s, right);
    }

    // set empty border with given number
    public void setMargin(int top, int left, int bottom, int right) {
        this.setBorder(BorderFactory.createEmptyBorder(top, left, bottom, right));
    }
}
