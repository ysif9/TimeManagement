package com.timemanagement.Controllers;

import com.timemanagement.Models.Task;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskCellController implements Initializable {

    public CheckBox task_checkbox;
    public Label deadline_lbl;

    private final Task task;

    public TaskCellController(Task task) {
        this.task = task;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        task_checkbox.textProperty().bind(task.taskNameProperty());
        deadline_lbl.textProperty().bind(task.deadlineProperty().asString());
        task.completedProperty().bind(task_checkbox.selectedProperty());
    }


}
