package component;

import javax.swing.*;
import java.awt.*;
import java.util.Base64;

/**
 * @author lomofu
 * <p>
 * This class define the icon factory method in the whole system
 */
public final class MyImageIcon {
    // default icon setting
    public static final int DEFAULT_ICON_WIDTH = 18;
    public static final int DEFAULT_ICON_HEIGHT = 18;

    /**
     * It will decode the base64 string and convert to byte array for image icon to read
     *
     * @param iconSrc the iconSrc depends on the base64 encoder
     *
     * @return image icon object
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
     * Same to the above method, but can customize the width and height
     *
     * @param iconSrc he iconSrc depends on the base64 encoder
     * @param width   customize the icon width
     * @param height  customize the icon height
     *
     * @return image icon object
     */
    public static ImageIcon build(String iconSrc, int width, int height) {
        byte[] imageSrc = Base64.getDecoder().decode(iconSrc);
        ImageIcon imageIcon = new ImageIcon(imageSrc);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }
}
