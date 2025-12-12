package taskmanagement.Models;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalTime;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.Duration;

public class Task implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    public enum State { READY, FOCUS, BREAK, STOPPED, FAIL, DONE }
    public enum Priority { Thấp, Trung, Cao }

    private final String taskName;
    private final LocalTime startTime;
    private final Duration focusTime;
    private final Duration breakTime;
    private final Priority importanceLevel;
    private final Duration mandatoryTime; // Thời gian bắt buộc thực hiện task
    private State currentState;
    private transient ObjectProperty<State> currentStateProperty;
    private Duration sessionElapsedTime = Duration.ZERO; // Thời gian trôi qua trong 1 session
    private Duration totalFocusTime = Duration.ZERO; // Tổng thời gian đã focus

    // Cờ để theo dõi thông báo
    private boolean notificationSent = false;

    public Task(String taskName, LocalTime startTime, Duration focusTime,
                Duration breakTime, Priority importanceLevel, Duration mandatoryTime) {
        this.taskName = taskName;
        this.startTime = startTime;
        this.focusTime = focusTime;
        this.breakTime = breakTime;
        this.importanceLevel = importanceLevel;
        this.mandatoryTime = mandatoryTime;
        this.currentState = State.READY;
        this.currentStateProperty = new SimpleObjectProperty<>(currentState);
    }

    public ObjectProperty<State> currentStateProperty() {
        if (currentStateProperty == null) currentStateProperty = new SimpleObjectProperty<>(currentState);
        return currentStateProperty;
    }

    public void setCurrentState(State state) {
        this.currentState = state;
        currentStateProperty().set(state);
    }

    public void startFocus() { resetSession(State.FOCUS); }
    public void startBreak() { resetSession(State.BREAK); }

    private void resetSession(State state) {
        setCurrentState(state);
        sessionElapsedTime = Duration.ZERO;
    }

    public void stop() {
        setCurrentState(State.STOPPED);
        sessionElapsedTime = Duration.ZERO;
        totalFocusTime = Duration.ZERO;
    }

    public void updateTime(Duration duration) {
        if (isRunning()) {
            sessionElapsedTime = sessionElapsedTime.add(duration);
            if (isFocus()) totalFocusTime = totalFocusTime.add(duration);
            if (totalFocusTime.greaterThanOrEqualTo(mandatoryTime)) setTaskDone();
        }
    }

    public Duration getSessionRemainingTime() {
        Duration time = isBreak() ? breakTime : focusTime;
        return time.subtract(sessionElapsedTime);
    }

    public boolean isFinishedSession() { return getSessionRemainingTime().lessThanOrEqualTo(Duration.ZERO); }
    public boolean isDone() { return currentState == State.DONE; }
    public boolean isReady() { return currentState == State.READY; }
    public boolean isRunning() {
        return currentState == State.FOCUS || currentState == State.BREAK || currentState == State.DONE;
    }
    public boolean isFocus() { return currentState == State.FOCUS; }
    public boolean isBreak() { return currentState == State.BREAK; }
    public boolean isFail() { return currentState == State.FAIL; }

    public void setTaskDone() { setCurrentState(State.DONE); }
    public void setTaskFailed() { setCurrentState(State.FAIL); }

    // Getter/setter cờ thông báo
    public boolean hasBeenNotified() { return notificationSent; }
    public void setNotificationSent() { this.notificationSent = true; }

    public Priority getImportanceLevel() { return importanceLevel; }
    public LocalTime getStartTime() { return startTime; }
    public Duration getBreakTime() { return breakTime; }
    public String getTaskName() { return taskName; }
    public Duration getTotalFocusTime() { return totalFocusTime; }
    public Duration getMandatoryTime() { return mandatoryTime; }
    public Duration getFocusTime() { return focusTime; }
}
