package taskmanagement.Controllers;

import taskmanagement.Models.Task;
import taskmanagement.AppManager;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.paint.Color;
import javafx.geometry.Insets;
import javafx.scene.effect.ColorAdjust;

public class TaskCellDayWindow extends ListCell<Task> {
    @Override
    protected void updateItem(Task item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setText(null);
            setGraphic(null);
        } else {
            HBox cell = new HBox();
            cell.setSpacing(10);

            GridPane taskInfo = new GridPane();
            taskInfo.setVgap(5);
            taskInfo.setHgap(10);

            addTaskInfo(taskInfo, item.getTaskName(), 0, 0, FontWeight.BOLD);
            addTaskInfo(taskInfo, "Thời gian bắt đầu: " + item.getStartTime(), 0, 1, FontWeight.NORMAL);
            addTaskInfo(taskInfo, "Thời gian tối thiểu: " + item.getMandatoryTime().toMinutes() + " phút", 1, 1, FontWeight.NORMAL);
            addTaskInfo(taskInfo, "Quãng tập trung: " + item.getFocusTime().toMinutes() + " phút", 0, 2, FontWeight.NORMAL);
            addTaskInfo(taskInfo, "Quãng nghỉ: " + item.getBreakTime().toMinutes() + " phút", 1, 2, FontWeight.NORMAL);

            // Màu nền dựa trên mức độ quan trọng
            BackgroundFill backgroundFill = switch (item.getImportanceLevel()) {
                case Thấp -> new BackgroundFill(Color.web("#A8E6CF"), new CornerRadii(5), Insets.EMPTY);
                case Trung -> new BackgroundFill(Color.web("#FFD54F"), new CornerRadii(5), Insets.EMPTY);
                case Cao -> new BackgroundFill(Color.web("#FFAB91"), new CornerRadii(5), Insets.EMPTY);
            };
            cell.setBackground(new Background(backgroundFill));

            // Hiệu ứng khi chọn
            if (isSelected()) {
                ColorAdjust colorAdjust = new ColorAdjust();
                colorAdjust.setBrightness(-0.2);
                cell.setEffect(colorAdjust);
                AppManager.selectedTask = item;
            } else {
                cell.setEffect(null);
            }

            Label statusLabel = new Label();
            statusLabel.setFont(Font.font("System", FontWeight.BOLD, 12));
            statusLabel.setBackground(new Background(new BackgroundFill(Color.web("#F0F0F0"), new CornerRadii(5), Insets.EMPTY)));
            statusLabel.setPadding(new Insets(5));
            statusLabel.setTextFill(Color.BLACK);

            StringBinding statusBinding = Bindings.createStringBinding(() -> {
                Task.State state = item.currentStateProperty().get();
                return switch (state) {
                    case DONE -> "HOÀN THÀNH";
                    case FAIL -> "THẤT BẠI";
                    case READY -> "SẴN SÀNG";
                    case FOCUS -> "TẬP TRUNG";
                    case BREAK -> "NGHỈ NGƠI";
                    case STOPPED -> "DỪNG";
                };
            }, item.currentStateProperty());
            statusLabel.textProperty().bind(statusBinding);

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            cell.getChildren().addAll(taskInfo, spacer, statusLabel);
            cell.setPrefHeight(80);
            cell.setAlignment(Pos.CENTER_LEFT);
            cell.setPadding(new Insets(10));

            setGraphic(cell);
            setText(null);
        }
    }

    private void addTaskInfo(GridPane taskInfo, String text, int colIndex, int rowIndex, FontWeight fontWeight) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font("System", fontWeight, 12));
        textNode.setFill(Color.BLACK);
        taskInfo.add(textNode, colIndex, rowIndex);
    }
}
