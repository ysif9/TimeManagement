/**
 * The TaskCellFactory class is responsible for creating and managing custom ListCell instances
 * to represent Task objects within a ListView. Each TaskCell contains a TaskCellController
 * which controls the behavior and appearance of the Task cell. This class handles the loading
 * of FXML files to define the Task cell's layout and content, as well as clearing the cell
 * when it's empty. It extends the ListCell class provided by JavaFX to customize the rendering
 * of individual cells within the ListView based on the associated Task objects.
 */

package com.timemanagement.Views;

import com.timemanagement.Controllers.TaskCellController;
import com.timemanagement.Models.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;

import java.io.IOException;

public class TaskCellFactory extends ListCell<Task> {

    @Override
    protected void updateItem(Task task, boolean empty) {
        super.updateItem(task, empty);
        if (empty) {
            clearCell();
        } else {
            loadTaskCell(task);
        }
    }

    // Clears the cell's text and graphic when it's empty
    private void clearCell() {
        setText(null);
        setGraphic(null);
    }

    // Loads the task cell with the provided task
    private void loadTaskCell(Task task) {
        FXMLLoader fxmlLoader = createFXMLLoader();
        TaskCellController taskCellController = new TaskCellController(task);
        fxmlLoader.setController(taskCellController);
        setText(null);
        try {
            setGraphic(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creates and returns a new FXMLLoader instance for loading FXML files
    private FXMLLoader createFXMLLoader() {
        return new FXMLLoader(getClass().getResource("/Fxml/TaskCell.fxml"));
    }
}
