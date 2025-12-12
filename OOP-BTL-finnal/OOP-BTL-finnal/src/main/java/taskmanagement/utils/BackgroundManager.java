package taskmanagement.utils;

import javafx.scene.layout.Pane;
import java.util.prefs.Preferences;

public class BackgroundManager {
    private static final String KEY = "backgroundPath";

    public static void applyBackground(Pane pane) {
        Preferences prefs = Preferences.userNodeForPackage(BackgroundManager.class);
        String path = prefs.get(KEY, null);

        if (path != null) {
            pane.setStyle(
                    "-fx-background-image: url('" + path + "');" +
                            "-fx-background-size: cover;" +
                            "-fx-background-position: center;" +
                            "-fx-background-repeat: no-repeat;"
            );
        }
    }

    public static void saveBackground(String imagePath) {
        Preferences prefs = Preferences.userNodeForPackage(BackgroundManager.class);
        prefs.put(KEY, imagePath);
    }
}
