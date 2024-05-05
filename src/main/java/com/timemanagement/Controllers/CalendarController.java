/**
 * Controller class for managing tasks displayed in a calendar view.
 * This class is responsible for updating task labels based on the selected date in the calendar.
 * It listens for changes in the selected date and updates the displayed tasks accordingly.
 * If there are fewer than five tasks for the selected date, the remaining task labels are left empty.
 */

package com.timemanagement.Controllers;

import atlantafx.base.controls.Calendar;
import com.timemanagement.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    // Labels representing tasks for the selected date
    public Label task1;
    public Label task2;
    public Label task3;
    public Label task4;
    public Label task5;

    // Calendar control
    public Calendar calendar;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Listener to update tasks when the selected date changes
        calendar.valueProperty().addListener((observableValue, oldVal, newVal) -> updateTasksOnSelectedDate());
    }

    // Method to update tasks displayed based on the selected date
    private void updateTasksOnSelectedDate() {
        // Set selected date in the model
        Model.getInstance().selectedDateProperty().set(calendar.getValue());

        // Update task labels based on tasks for the selected date
        updateTaskLabel(task1, 0);
        updateTaskLabel(task2, 1);
        updateTaskLabel(task3, 2);
        updateTaskLabel(task4, 3);
        updateTaskLabel(task5, 4);
    }

    // Method to update a specific task label based on its index
    private void updateTaskLabel(Label label, int index) {
        try {
            // Retrieve task for the index from the model and set label's text
            label.setText(Model.getInstance().getTasksOnSelectedDate().get(index).taskNameProperty().getValue());
        } catch (Exception ex) {
            // Clear label's text if no task exists for the index
            if (index == 0)
            label.setText("No Tasks to Display...");
            else
                label.setText("");

        }
    }

}