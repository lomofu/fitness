import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author lomofu
 * @desc
 * @create 22/Nov/2021 12:44
 */
public class ClubMembership {
    public static void main(String[] args) throws InterruptedException {
        try {
            SplashView.run();
            SwingUtilities.invokeAndWait(DataSource::init);
        } catch(InterruptedException | InvocationTargetException | Error e) {
            SplashView.dispose();
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.error(e.getMessage());
            System.exit(- 1);
        }

        SwingUtilities.invokeLater(() -> {
            new DataSource();
            try {
                new ClubFrameView();
            } catch(Exception | Error e) {
                try {
                    SplashView.dispose();
                } catch(InterruptedException ex) {
                    Logger.error(e.getMessage());
                }
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                Logger.error(e.getMessage());
                System.exit(- 1);
            }
        });
    }
}
