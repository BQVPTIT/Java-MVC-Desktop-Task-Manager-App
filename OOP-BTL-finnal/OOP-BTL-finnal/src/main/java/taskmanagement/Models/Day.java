package taskmanagement.Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.Serial;
import java.io.Serializable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Day implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final LocalDate date;

    private transient ObservableList<Task> taskObservableList;
    private List<Task> serializableList;

    public Day(LocalDate date) {
        this.date = date;
        this.taskObservableList = FXCollections.observableArrayList();
    }

    public LocalDate getDate() {
        return date;
    }

    public void addTask(Task task) {
        taskObservableList.add(task);
        sortTasksByTime();
    }

    private void sortTasksByTime() {
        taskObservableList.sort(Comparator.comparing(taskmanagement.Models.Task::getStartTime));
    }

    public void removeTask(Task task) {
        taskObservableList.remove(task);
    }

    public ObservableList<Task> getTaskObservableList() {
        return taskObservableList;
    }

    // Copy các task trong observable list để lưu lại
    @Serial
    private void writeObject(ObjectOutputStream oos) throws IOException {
        serializableList = new ArrayList<>(taskObservableList);
        oos.defaultWriteObject();
    }

    // Tạo lại observable list sau khi deserialize
    @Serial
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        taskObservableList = FXCollections.observableArrayList(serializableList);
    }
}
