package taskmanagement.Controllers;

import taskmanagement.Models.Day;
import taskmanagement.AppManager;

import javafx.application.Platform;
import javafx.concurrent.ScheduledService;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.time.LocalDate;
import java.time.LocalTime;

public class StatusUpdateService extends ScheduledService<Void> {

    // Thời gian quét định kỳ (giây)
    private static final long CHECK_INTERVAL_SECONDS = 10;
    // Thời gian thông báo trước khi task bắt đầu (phút)
    private static final long NOTIFY_BEFORE_MINUTES = 5;

    public StatusUpdateService() {
        setPeriod(Duration.seconds(CHECK_INTERVAL_SECONDS));
    }

    @Override
    protected javafx.concurrent.Task<Void> createTask() {
        return new javafx.concurrent.Task<>() {
            @Override
            protected Void call() {
                updateFailedStatus(AppManager.selectedDay);
                checkForNotifications();
                return null;
            }
        };
    }

    /** Cập nhật trạng thái FAIL cho các task đã quá hạn (ngày đang xem) */
    private void updateFailedStatus(Day selectedDay) {
        if (selectedDay == null) return;

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        selectedDay.getTaskObservableList().stream()
                .filter(modelTask -> modelTask.isReady())
                .forEach(modelTask -> {
                    boolean isBeforeToday = selectedDay.getDate().isBefore(currentDate);
                    boolean isTodayAndExpired = selectedDay.getDate().isEqual(currentDate)
                            && modelTask.getStartTime().plusMinutes(5).isBefore(currentTime);

                    if (isBeforeToday || isTodayAndExpired) {
                        Platform.runLater(modelTask::setTaskFailed);
                    }
                });
    }

    /** Gửi thông báo pop-up cho các task sắp bắt đầu (dựa trên ngày hôm nay) */
    private void checkForNotifications() {
        if (AppManager.calendar == null) return;

        LocalDate todayDate = LocalDate.now();
        LocalTime now = LocalTime.now();

        AppManager.calendar.setToAnotherWeek(todayDate); // đảm bảo tuần hiện tại được tải
        Day today = AppManager.calendar.getCurrentWeek().getDayList().stream()
                .filter(d -> d.getDate().isEqual(todayDate))
                .findFirst().orElse(null);

        if (today == null) return;

        today.getTaskObservableList().stream()
                .filter(modelTask -> modelTask.isReady() && !modelTask.hasBeenNotified())
                .forEach(modelTask -> {
                    long secondsUntilStart = java.time.Duration.between(now, modelTask.getStartTime()).getSeconds();
                    if (secondsUntilStart > 0 && secondsUntilStart <= (NOTIFY_BEFORE_MINUTES * 60)) {
                        modelTask.setNotificationSent();
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Công việc sắp bắt đầu!")
                                    .text(String.format("Công việc '%s' sẽ bắt đầu lúc %s.",
                                            modelTask.getTaskName(), modelTask.getStartTime().toString()))
                                    .position(Pos.BOTTOM_RIGHT)
                                    .hideAfter(Duration.seconds(10))
                                    .showInformation();
                        });
                    }
                });
    }
}
