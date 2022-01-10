package component;

import javax.swing.*;
import java.awt.*;

/**
 * @author lomofu
 * <p>
 * This class define the icon factory method in the whole system
 */
public final class MyImageIcon {
    // default icon setting
    public static final int DEFAULT_ICON_WIDTH = 18;
    public static final int DEFAULT_ICON_HEIGHT = 18;

    public static ImageIcon build(String iconName) {
        ImageIcon imageIcon = new ImageIcon(iconName);
        Image image = imageIcon.getImage();
        Image newImg =
                image.getScaledInstance(
                        DEFAULT_ICON_WIDTH, DEFAULT_ICON_HEIGHT, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);

    }
    
    public static ImageIcon build(String iconName, int width, int height) {
        ImageIcon imageIcon = new ImageIcon(iconName);
        Image image = imageIcon.getImage();
        Image newImg = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);

    }
}
