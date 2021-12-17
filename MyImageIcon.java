import javax.swing.*;
import java.awt.*;
import java.util.Base64;
//todo
/**
 * @author lomofu
 *
 * This class
 */
public final class MyImageIcon {
    public static final int DEFAULT_ICON_WIDTH = 18;
    public static final int DEFAULT_ICON_HEIGHT = 18;

    /**
     *
     * @param iconSrc
     * @return
     */
    public static ImageIcon build(String iconSrc) {
        byte[] imageSrc = Base64.getDecoder().decode(iconSrc);
        ImageIcon imageIcon = new ImageIcon(imageSrc);
        Image image = imageIcon.getImage();
        Image newImg =
                image.getScaledInstance(
                        DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    /**
     *
     *
     * @param iconSrc
     * @param width
     * @param height
     * @return
     */
    public static ImageIcon build(String iconSrc, int width, int height) {
        byte[] imageSrc = Base64.getDecoder().decode(iconSrc);
        ImageIcon imageIcon = new ImageIcon(imageSrc);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}
