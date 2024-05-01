package com.timemanagement.Controllers;

import atlantafx.base.controls.RingProgressIndicator;
import com.timemanagement.Models.Task;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;

public class FocusController implements Initializable {
    public ToggleButton start_btn;
    public ToggleButton left_btn;
    public ToggleButton right_btn;
    public RingProgressIndicator ring_progress;
    private final static int totalTimeInSeconds = 3000;
    public ListView<Task> tasks_listview;
    private Thread timerThread;
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

//        final ObservableList<Task> tasks = FXCollections.observableArrayList();
//        tasks.add(new Task("Task1", LocalDate.of(2024, 6, 23)));
//        tasks.add(new Task("Task2", LocalDate.of(2024, 6, 23)));
//        tasks_listview.setItems(tasks);
//        tasks_listview.setCellFactory(e -> new TaskCellFactory());

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
                startTimer();
            } else if (!start_btn.isSelected()) {
                start_btn.setStyle(
                        "-fx-background-color: #0A84FF;" +
                        "-fx-effect: dropshadow(gaussian, #0F4B89, 0, 6, 0, 6);");
                start_btn.setText("START");
                stopTimer();
            }
        });
    }

    private String percentageToMinuteSecond(double progress) {
        double timerInSeconds = progress * totalTimeInSeconds;

        int minutes = (int) (timerInSeconds / 60);
        int seconds = (int) (timerInSeconds % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    private void startTimer() {
        if (stage == null) {
            stage = (Stage) start_btn.getScene().getWindow();
            stage.setOnCloseRequest(e -> stopTimer());
        }
        timerThread = new Thread(this::countdownTimer);
        timerThread.start();
    }

    public void stopTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
            ring_progress.setProgress(1);
        }

    }

    private void countdownTimer() {
        for (int i = totalTimeInSeconds; i >= 0; i--) {
            double percentage = (double) i / totalTimeInSeconds;
            try {
                Platform.runLater(() -> ring_progress.setProgress(percentage));
            }catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000); // Sleep for 1 second
            } catch (InterruptedException e) {
                return;
            }
        }
    }

}
