package com.timemanagement.Controllers;

import com.timemanagement.Models.Task;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.ResourceBundle;

public class TaskCellController implements Initializable {

    public CheckBox task_checkbox;
    public Label deadline_lbl;
    public Label task_lbl;
    public HBox info_box;

    private final Task task;

    public TaskCellController(Task task) {
        this.task = task;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var icon = new FontIcon(Feather.TRASH);
        var dangerBtn = new Button();

        dangerBtn.getStyleClass().addAll(
                        Styles.BUTTON_CIRCLE, Styles.BUTTON_OUTLINED, Styles.DANGER, Styles.SMALL
        );
        dangerBtn.setGraphic(icon);
        dangerBtn.setPadding(new Insets(8));

        info_box.getChildren().add(dangerBtn);
        task_lbl.textProperty().bind(task.taskNameProperty());
        deadline_lbl.textProperty().bind(task.deadlineProperty().asString());
        task.completedProperty().bind(task_checkbox.selectedProperty());

    }


}
