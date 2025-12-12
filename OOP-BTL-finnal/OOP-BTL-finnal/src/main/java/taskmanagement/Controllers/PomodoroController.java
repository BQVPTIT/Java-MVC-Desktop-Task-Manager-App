package taskmanagement.Controllers;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import taskmanagement.Models.Music;
import taskmanagement.Models.Task;
import taskmanagement.AppManager;
import taskmanagement.utils.BackgroundManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class PomodoroController implements Initializable {

    private static final String NOTIFICATION_SOUND_PATH = "/Notification/notification.mp3";
    private AudioClip notificationSound;
    private Task task;
    private Timeline timeline;
    private Music music;
    private int songNumber;
    private MediaPlayer mediaPlayer;

    @FXML
    private Label countdownLabel, modeLabel, totalTimeLabel;
    @FXML
    private ComboBox<String> songPicker;
    @FXML
    private Button playPauseButton, exitButton;
    @FXML
    private AnchorPane rootPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.task = AppManager.selectedTask;
        if (this.task == null) {
            System.err.println("Lỗi: PomodoroController được tải mà không có task nào được chọn.");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi");
            alert.setHeaderText("Không thể bắt đầu Pomodoro");
            alert.setContentText("Không có công việc nào được chọn. Vui lòng quay lại và chọn một công việc.");
            alert.showAndWait();
            return;
        }

        countdownLabel.setText(formatTime((int) task.getFocusTime().toSeconds()));

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), _ -> updateCountdown()));
        timeline.setCycleCount(Timeline.INDEFINITE);

        notificationSound = new AudioClip(
                Objects.requireNonNull(getClass().getResource(NOTIFICATION_SOUND_PATH)).toString()
        );

        this.music = new Music();
        if (!music.getSongs().isEmpty()) {
            setUpListSong();
        }

        AppManager.stage.setOnCloseRequest(event -> {
            event.consume();
            try {
                handleExit();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        BackgroundManager.applyBackground(rootPane);
    }

    private void updateCountdown() {
        task.updateTime(Duration.seconds(1));
        if (task.isFinishedSession()) {
            notificationSound.play();
            if (task.isBreak()) {
                task.startFocus();
                modeLabel.setText("Tập trung");
            } else {
                task.startBreak();
                modeLabel.setText("Nghỉ");
            }
        }
        updateTimeLabel();
    }

    @FXML
    public void startPomodoroButton() {
        if (!task.isRunning()) {
            task.startFocus();
            updateTimeLabel();
            modeLabel.setText("Tập trung");
            timeline.play();
        }
    }

    @FXML
    public void stopPomodoroButton() {
        if (task.isRunning()) {
            task.stop();
            timeline.stop();
            modeLabel.setText("Nghỉ");
        }
    }

    private void updateTimeLabel() {
        countdownLabel.setText(formatTime((int) task.getSessionRemainingTime().toSeconds()));
        totalTimeLabel.setText("Thời gian tập trung: " + formatTime((int) task.getTotalFocusTime().toSeconds()));
    }

    private String formatTime(int seconds) {
        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
        if (hours > 0) return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        else return String.format("%02d:%02d", minutes, seconds);
    }

    @FXML
    private void handleExit() throws IOException {
        if (task.isDone()) {
            exitToDayWindow();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Xác nhận thoát");
            int totalRemainTime = (int) task.getMandatoryTime().subtract(task.getTotalFocusTime()).toSeconds();
            int minutes = totalRemainTime / 60, seconds = totalRemainTime % 60;
            alert.setContentText(String.format(
                    "Còn %d phút %d giây nữa để hoàn thành\nTask sẽ tính là không hoàn thành nếu bạn thoát bây giờ",
                    minutes, seconds
            ));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                task.setTaskFailed();
                exitToDayWindow();
            }
        }
    }

    private void exitToDayWindow() throws IOException {
        if (mediaPlayer != null) mediaPlayer.stop();
        AppManager.switchToDayWindow();
        AppManager.stage.setOnCloseRequest(null);
        if (mediaPlayer != null) mediaPlayer.stop();
    }

    private void setUpListSong() {
        for (File song : music.getSongs()) {
            songPicker.getItems().add(song.getName().substring(0, song.getName().lastIndexOf('.')));
        }
        songPicker.getSelectionModel().selectFirst();
        loadSelectedSong();

        songPicker.getSelectionModel().selectedIndexProperty().addListener((_, _, newVal) -> {
            songNumber = newVal.intValue();
            loadSelectedSong();
            playPauseButton.setText("Bắt đầu");
        });
    }

    private void loadSelectedSong() {
        if (mediaPlayer != null && music.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            music.stopPlaying();
        }
        File selectedSong = music.getSongs().get(songNumber);
        Media media = new Media(selectedSong.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    }

    @FXML
    public void handlePlayPause() {
        if (music.getSongs().isEmpty()) return;

        if (music.isPlaying()) {
            mediaPlayer.pause();
            music.stopPlaying();
            playPauseButton.setText("Bắt đầu");
        } else {
            mediaPlayer.play();
            music.startPlaying();
            playPauseButton.setText("Dừng");
        }
    }
}
