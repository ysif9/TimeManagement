package com.timemanagement.Controllers;

import atlantafx.base.controls.RingProgressIndicator;
import com.timemanagement.Models.Task;
import com.timemanagement.Views.TaskCellFactory;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.util.StringConverter;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class FocusController implements Initializable {
    public ToggleButton start_btn;
    public ToggleButton left_btn;
    public ToggleButton right_btn;
    public RingProgressIndicator ring_progress;
    private final static int totalTimeInSeconds = 3000;
    public ListView<Task> tasks_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        final ObservableList<Task> tasks = FXCollections.observableArrayList();
        tasks.add(new Task("Task1", LocalDate.of(2024, 6, 23)));
        tasks_listview.setItems(tasks);
        tasks_listview.setCellFactory(e -> new TaskCellFactory());

        //turn progress to string
        ring_progress.setStringConverter(new StringConverter<>() {
            @Override
            public String toString(Double progress) {return percentageToMinuteSecond(progress);}

            @Override
            public Double fromString(String progress) {return 0d;}
        });

        left_btn.getStyleClass().add(Styles.LEFT_PILL);
        right_btn.getStyleClass().add(Styles.RIGHT_PILL);

        ToggleGroup toggleGroup = new ToggleGroup();
        left_btn.setToggleGroup(toggleGroup);
        right_btn.setToggleGroup(toggleGroup);
        toggleGroup.selectedToggleProperty().addListener((obs, old, val) -> {
            if (val == null) {
                old.setSelected(true);
            }
        });

        start_btn.getStyleClass().add(Styles.ACCENT);

        start_btn.setOnAction(e -> {
            if (start_btn.isSelected()) {
                start_btn.setStyle(
                        "-fx-effect: null;" +
                        "-fx-text-fill: #2F96FF;" +
                        "-fx-background-color: transparent;");
                start_btn.setText("STOP");
            } else if (!start_btn.isSelected()) {
                start_btn.setStyle(
                        "-fx-background-color: #0A84FF;" +
                        "-fx-effect: dropshadow(gaussian, #0F4B89, 0, 6, 0, 6);");
                start_btn.setText("START");
            }
        });
    }

    private String percentageToMinuteSecond(double progress) {
        double timerInSeconds = progress * totalTimeInSeconds;

        int minutes = (int) (timerInSeconds / 60);
        int seconds = (int) (timerInSeconds % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

}
