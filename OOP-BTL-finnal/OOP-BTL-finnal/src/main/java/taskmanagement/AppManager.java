package taskmanagement;

import taskmanagement.Controllers.StatusUpdateService;
import taskmanagement.Models.Calendar;
import taskmanagement.Models.Day;
import taskmanagement.Models.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class AppManager {
    public static Stage stage;
    public static Scene mainWindow;
    public static Calendar calendar;
    public static Day selectedDay;
    public static Task selectedTask;

    // Giữ duy nhất instance service
    private static final StatusUpdateService statusUpdateService = new StatusUpdateService();

    // Khởi động service MỘT LẦN
    public static void startStatusService() {
        if (statusUpdateService.getState() == Worker.State.READY) {
            statusUpdateService.start();
        } else if (statusUpdateService.getState() == Worker.State.CANCELLED) {
            statusUpdateService.reset();
            statusUpdateService.start();
        }
    }

    // Dừng service MỘT LẦN
    public static void stopStatusService() {
        if (statusUpdateService.isRunning()) {
            statusUpdateService.cancel();
        }
    }

    public static void switchToDayWindow() throws IOException {
        startStatusService();
        loadAndSetScene("/Fxml/day-window.fxml");
    }

    public static void switchToPomodoroWindow() throws IOException {
        stopStatusService();
        loadAndSetScene("/Fxml/pomodoro-window.fxml");
    }

    public static void switchToMainWindow() {
        stopStatusService();

        // Lưu lại kích thước hiện tại của stage
        double width = stage.getWidth();
        double height = stage.getHeight();
        boolean isMaximized = stage.isMaximized();

        // Gán lại scene chính
        stage.setScene(mainWindow);

        // Giữ nguyên kích thước / trạng thái phóng to
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setMaximized(isMaximized);

        stage.show();
    }

    private static void loadAndSetScene(String fxmlPath) throws IOException {
        double width = 800;  // fallback mặc định
        double height = 600;
        boolean isMaximized = stage.isMaximized();

        // Nếu đang có scene hiện tại, lấy kích thước thực tế từ đó
        if (stage.getScene() != null) {
            width = stage.getScene().getWidth();
            height = stage.getScene().getHeight();
        }

        FXMLLoader loader = new FXMLLoader(AppManager.class.getResource(fxmlPath));
        Parent root = loader.load();

        Scene scene = new Scene(root, width, height);
        stage.setScene(scene);

        // Giữ trạng thái phóng to nếu trước đó có
        stage.setMaximized(isMaximized);

        stage.show();
    }
}
