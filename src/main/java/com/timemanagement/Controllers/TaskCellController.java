package com.timemanagement.Controllers;

import com.timemanagement.Models.Task;
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

    public CheckBox task_checkbox;
    public Label deadline_lbl;
    public Label task_lbl;
    public Label time_lbl;
    public HBox info_box;

    private final Task task;

    public TaskCellController(Task task) {
        this.task = task;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        var timeIcon = new FontIcon(Material2OutlinedMZ.TIMER);
        timeIcon.getStyleClass().add(Styles.LARGE);
        time_lbl.setGraphic(timeIcon);

        var deleteIcon = new FontIcon(Feather.TRASH);
        var deleteBtn = new Button();

        deleteBtn.getStyleClass().addAll(
                        Styles.BUTTON_CIRCLE, Styles.BUTTON_OUTLINED, Styles.DANGER, Styles.SMALL
        );
        deleteBtn.setGraphic(deleteIcon);
        deleteBtn.setPadding(new Insets(8));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        StringConverter<LocalDate> stringConverter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return date != null ? formatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return LocalDate.parse(string, formatter);
            }
        };

        info_box.getChildren().add(deleteBtn);
        task_lbl.textProperty().bind(task.taskNameProperty());
        deadline_lbl.textProperty().bind(Bindings.createStringBinding(() -> stringConverter.toString(task.deadlineProperty().get())));
        time_lbl.textProperty().bind(task.timeSpentProperty());
        task.completedProperty().bind(task_checkbox.selectedProperty());

    }



}
