package com.timemanagement.Views;

import com.timemanagement.Controllers.TaskCellController;
import com.timemanagement.Models.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

public class TaskCellFactory extends ListCell<Task> {

    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/TaskCell.fxml"));
            TaskCellController taskCellController = new TaskCellController(task);
            fxmlLoader.setController(taskCellController);
            setText(null);
            try {
                setGraphic(fxmlLoader.load());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
