/**
 * The TaskCellController class is responsible for controlling the behavior and appearance of a single task cell
 * within a list view in the time management application. It initializes and manages UI components such as checkboxes,
 * labels, and buttons for displaying task information, deadline, completion status, and deletion functionality.
 * This class facilitates the binding of task properties to UI elements and provides event handling for user interactions
 * such as task completion and deletion.
 */

package com.timemanagement.Controllers;

import com.timemanagement.Models.Model;
import com.timemanagement.Models.Task;
import com.timemanagement.Styles;
import javafx.beans.binding.Bindings;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.material2.Material2OutlinedMZ;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TaskCellController implements Initializable {

    // UI elements
    public CheckBox task_checkbox;
    public Label deadline_lbl;
    public Label task_lbl;
    public Label time_lbl;
    public HBox info_box;

    // Task associated with the cell
    private final Task task;

    // Constructor
    public TaskCellController(Task task) {
        this.task = task;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize time icon
        var timeIcon = new FontIcon(Material2OutlinedMZ.TIMER);
        timeIcon.getStyleClass().add(Styles.LARGE);
        time_lbl.setGraphic(timeIcon);

        // Initialize delete button and icon
        var deleteIcon = new FontIcon(Feather.TRASH);
        var deleteBtn = createDeleteButton(deleteIcon);

        // Initialize date formatter
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        // String converter for date
        StringConverter<LocalDate> stringConverter = createStringConverter(formatter);

        // Add delete button to info box
        info_box.getChildren().add(deleteBtn);

        // Bind task properties to UI elements
        task_lbl.textProperty().bind(task.taskNameProperty());
        deadline_lbl.textProperty().bind(Bindings.createStringBinding(() -> stringConverter.toString(task.deadlineProperty().get())));
        Model.formatTaskTime(task, time_lbl);
        task_checkbox.selectedProperty().bindBidirectional(task.completedProperty());
    }

    // Create delete button with styling and event handling
    private Button createDeleteButton(FontIcon deleteIcon) {
        var deleteBtn = new Button();
        deleteBtn.getStyleClass().addAll(
                Styles.BUTTON_CIRCLE, Styles.BUTTON_OUTLINED, Styles.DANGER, Styles.SMALL
        );
        deleteBtn.setGraphic(deleteIcon);
        deleteBtn.setPadding(new Insets(8));
        deleteBtn.setOnMouseClicked(e -> task.deleteFlagProperty().set(true));
        return deleteBtn;
    }

    // Create string converter for date
    private StringConverter<LocalDate> createStringConverter(DateTimeFormatter formatter) {
        return new StringConverter<>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return LocalDate.parse(string, formatter);
            }
        };
    }
}
