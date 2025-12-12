package taskmanagement.Controllers;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import taskmanagement.Models.Calendar;
import taskmanagement.Models.Day;
import taskmanagement.Models.Task;
import taskmanagement.AppManager;
import taskmanagement.utils.BackgroundManager;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class CalendarWindowController implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private ListView<Task> mondayListView, tuesdayListView, wednesdayListView,
            thursdayListView, fridayListView, saturdayListView, sundayListView;
    @FXML
    private Label mondayLabel, tuesdayLabel, wednesdayLabel, thursdayLabel,
            fridayLabel, saturdayLabel, sundayLabel;
    @FXML
    private Button nextButton, previousButton, deleteAllButton;
    @FXML
    private DatePicker datePicker;

    private Calendar calendar;
    private boolean isUpdatingDatePicker = false;

    private List<ListView<Task>> listViews;
    private List<Label> labels;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calendar = AppManager.calendar;
        datePicker.setValue(calendar.getStartOfCurrentWeek());

        listViews = List.of(mondayListView, tuesdayListView, wednesdayListView,
                thursdayListView, fridayListView, saturdayListView, sundayListView);
        labels = List.of(mondayLabel, tuesdayLabel, wednesdayLabel,
                thursdayLabel, fridayLabel, saturdayLabel, sundayLabel);

        setupListViewCellFactories();
        updateListViews();
        setupListViewWidths();

        // Cập nhật khi thay đổi kích thước cửa sổ
        rootPane.widthProperty().addListener((obs, oldWidth, newWidth) -> setupListViewWidths());

        // Áp dụng nền
        BackgroundManager.applyBackground(rootPane);
    }

    private void setupListViewCellFactories() {
        listViews.forEach(listView -> listView.setCellFactory(_ -> new TaskCellCalendarWindow()));
    }

    public void updateListViews() {
        List<Day> dayList = calendar.getCurrentWeek().getDayList();
        IntStream.range(0, listViews.size()).forEach(i ->
                listViews.get(i).setItems(dayList.get(i).getTaskObservableList()));

        updateDayLabels();
        highlightToday();
    }

    private void updateDayLabels() {
        List<LocalDate> days = calendar.getCurrentWeek().getDayList()
                .stream().map(Day::getDate).toList();

        java.time.format.DateTimeFormatter formatter =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM");

        String[] weekdays = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ Nhật"};

        IntStream.range(0, labels.size()).forEach(i -> {
            labels.get(i).setText(weekdays[i] + " - " + days.get(i).format(formatter));
        });
    }

    private void highlightToday() {
        LocalDate today = LocalDate.now();
        List<Day> dayList = calendar.getCurrentWeek().getDayList();

        IntStream.range(0, labels.size()).forEach(i -> {
            LocalDate date = dayList.get(i).getDate();
            if (date.equals(today)) {
                labels.get(i).setStyle("-fx-background-color: #4CAF50; -fx-font-weight: bold; -fx-border-color: black;");
            } else {
                labels.get(i).setStyle("-fx-background-color: #FFFFFF; -fx-font-weight: bold; -fx-border-color: black;");
            }
        });
    }

    private void setupListViewWidths() {
        double width = rootPane.getWidth() / listViews.size();
        IntStream.range(0, listViews.size()).forEach(i -> {
            double x = i * width;
            labels.get(i).setPrefWidth(width);
            labels.get(i).setLayoutX(x);
            labels.get(i).setLayoutY(80);

            listViews.get(i).setPrefWidth(width);
            listViews.get(i).setLayoutX(x);
            listViews.get(i).setLayoutY(110);
        });
    }

    @FXML
    public void mondayClicked() throws IOException { handleDayClick(0); }
    @FXML
    public void tuesdayClicked() throws IOException { handleDayClick(1); }
    @FXML
    public void wednesdayClicked() throws IOException { handleDayClick(2); }
    @FXML
    public void thursdayClicked() throws IOException { handleDayClick(3); }
    @FXML
    public void fridayClicked() throws IOException { handleDayClick(4); }
    @FXML
    public void saturdayClicked() throws IOException { handleDayClick(5); }
    @FXML
    public void sundayClicked() throws IOException { handleDayClick(6); }

    private void handleDayClick(int dayIndex) throws IOException {
        AppManager.selectedDay = calendar.getCurrentWeek().getDayList().get(dayIndex);
        AppManager.switchToDayWindow();
        listViews.get(dayIndex).getSelectionModel().clearSelection();
    }

    @FXML
    private void handleChangeDate() {
        if (!isUpdatingDatePicker && datePicker.getValue() != null) {
            calendar.setToAnotherWeek(datePicker.getValue());
            updateListViews();
        }
    }

    @FXML
    private void handleNextButtonAction() {
        calendar.setToNextWeek();
        updateDatePicker();
        updateListViews();
    }

    @FXML
    private void handlePreviousButtonAction() {
        calendar.setToPreviousWeek();
        updateDatePicker();
        updateListViews();
    }

    private void updateDatePicker() {
        isUpdatingDatePicker = true;
        datePicker.setValue(calendar.getStartOfCurrentWeek());
        isUpdatingDatePicker = false;
    }

    @FXML
    private void handleUploadBackground() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh làm nền");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String imagePath = file.toURI().toString();
                BackgroundManager.saveBackground(imagePath);
                BackgroundManager.applyBackground(rootPane);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
