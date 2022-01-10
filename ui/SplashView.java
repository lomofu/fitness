package ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

/**
 * @author lomofu
 * <p>
 * This class sets the welcome panel
 */
public class SplashView {
    private static final JWindow window = new JWindow();

    public static void run() {
        // set the window size
        Dimension screen = window.getToolkit().getScreenSize();
        ImageIcon imageIcon = new ImageIcon("assets/splash.jpg");
        window.getContentPane().add(
                new JLabel("", imageIcon, SwingConstants.CENTER));
        window.setSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
        window.pack();
        window.setLocation((screen.width - window.getSize().width) / 2,
                (screen.height - window.getSize().height) / 2);
        window.setVisible(true);
        window.setAlwaysOnTop(true);
    }

    // close this window after one second
    public static void dispose() throws InterruptedException {
        TimeUnit.SECONDS.sleep(1);
        window.setVisible(false);
        window.dispose();
    }
}