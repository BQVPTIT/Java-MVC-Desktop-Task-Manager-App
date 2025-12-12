package taskmanagement;

import taskmanagement.Models.Calendar;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void init() {
        AppManager.calendar = new Calendar();
        // Khởi động dịch vụ chạy ngầm ngay khi ứng dụng bắt đầu
        AppManager.startStatusService();
    }

    @Override
    public void start(Stage stage) throws IOException {
        AppManager.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/Fxml/main-window.fxml"));
        AppManager.mainWindow = new Scene(fxmlLoader.load());
        AppManager.switchToMainWindow();
    }

    @Override
    public void stop() throws IOException {
        // Tắt dịch vụ chạy ngầm trước khi đóng ứng dụng
        AppManager.stopStatusService();
        // Lưu dữ liệu tuần sau khi đã tắt service
        AppManager.calendar.saveWeeksToFile();
    }

    public static void main(String[] args) {
        launch();
    }
}
