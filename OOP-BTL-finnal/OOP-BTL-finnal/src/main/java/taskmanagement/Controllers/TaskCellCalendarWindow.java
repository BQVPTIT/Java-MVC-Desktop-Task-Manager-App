package taskmanagement.Controllers;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import taskmanagement.Models.Task;

public class TaskCellCalendarWindow extends ListCell<Task> {

    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);

        if (empty || task == null) {
            setGraphic(null);
        } else {
            // Tạo nhãn và container
            Label taskNameLabel = new Label(task.getTaskName());
            taskNameLabel.setWrapText(true);
            taskNameLabel.setTextAlignment(TextAlignment.CENTER);
            taskNameLabel.setMaxWidth(getListView().getWidth() - 40);

            HBox container = new HBox(taskNameLabel);
            container.setAlignment(Pos.CENTER);
            container.setPadding(new Insets(5));

            // Màu nền và viền dựa trên mức độ ưu tiên
            Color backgroundColor;
            Color borderColor;

            switch (task.getImportanceLevel()) {
                case Thấp -> {
                    backgroundColor = Color.web("#A8E6CF"); // xanh nhạt
                    borderColor = Color.web("#00FF00");    // viền xanh
                }
                case Trung -> {
                    backgroundColor = Color.web("#FFFF99"); // vàng nhạt
                    borderColor = Color.web("#FF9800");    // viền cam
                }
                case Cao -> {
                    backgroundColor = Color.web("#FFAB91"); // cam đỏ
                    borderColor = Color.web("#FF0000");    // viền đỏ
                }
                default -> {
                    backgroundColor = Color.LIGHTGRAY;
                    borderColor = Color.GRAY;
                }
            }

            // Set background và border
            container.setBackground(new Background(
                    new BackgroundFill(backgroundColor, new CornerRadii(5), Insets.EMPTY)
            ));
            container.setBorder(new Border(
                    new BorderStroke(borderColor, BorderStrokeStyle.SOLID, new CornerRadii(5), new BorderWidths(2))
            ));

            setGraphic(container);
        }
    }
}
