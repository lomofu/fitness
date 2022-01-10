import data.DataSource;
import ui.ClubFrameView;
import ui.SplashView;
import utils.Logger;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @author lomofu
 * <p>
 * This class will boot the whole application whith some steps
 * <p>
 * 1. load the splash view
 * 2. init the datasource
 * 3. dispose the splash view (if any error)
 * 4. create a background task
 * 5. load JFrame
 */
public class ClubMembership {
    public static void main(String[] args) throws InterruptedException {
        try {
            SplashView.run();
            SwingUtilities.invokeAndWait(DataSource::init);
            //cover the exceptions and errors
        } catch (InterruptedException | InvocationTargetException | Error e) {
            SplashView.dispose();
            JOptionPane.showMessageDialog(null, e.getCause(), "Error", JOptionPane.ERROR_MESSAGE);
            Logger.error(e.getMessage());
            System.exit(-1);
        }

        SwingUtilities.invokeLater(() -> {
            new DataSource();
            try {
                new ClubFrameView();
            } catch (Exception | Error e) {
                try {
                    SplashView.dispose();
                } catch (InterruptedException ex) {
                    Logger.error(e.getMessage());
                }
                JOptionPane.showMessageDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                Logger.error(e.getMessage());
                System.exit(-1);
            }
        });
    }
}
