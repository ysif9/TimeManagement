package com.timemanagement.Controllers;

import atlantafx.base.controls.*;
import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.StringConverter;

import java.net.URL;
import java.util.ResourceBundle;


public class FocusController implements Initializable {
    public ToggleButton start_btn;
    public ToggleButton left_btn;
    public ToggleButton right_btn;
    public RingProgressIndicator ring_progress;
    private final static int totalTimeInSeconds = 3000;
    public VBox task_vbox;
    private Thread timerThread;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //turn progress to string
        ring_progress.setStringConverter(new StringConverter<>() {
            @Override
            public String toString(Double progress) {return percentageToMinuteSecond(progress);}

            @Override
            public Double fromString(String progress) {return 0d;}
        });

        setupToggleGroup();

        start_btn.getStyleClass().add(Styles.ACCENT);
        start_btn.setOnMouseClicked(e -> onStartTimer());
        setupTaskBox();
    }

    /*
    * Timer logic
    * */

    private void onStartTimer() {
        if (start_btn.isSelected()) {
            start_btn.setStyle(
                    "-fx-effect: null;" +
                            "-fx-text-fill: -color-accent-fg;" +
                            "-fx-background-color: transparent;");
            start_btn.setText("STOP");
            startTimer();
        } else if (!start_btn.isSelected()) {
            start_btn.setStyle(
                    "-fx-background-color: -color-accent-emphasis;" +
                            "-fx-effect: dropshadow(gaussian, -color-accent-muted, 0, 6, 0, 6);");
            start_btn.setText("START");
            stopTimer();
        }
    }

    private String percentageToMinuteSecond(double progress) {
        double timerInSeconds = progress * totalTimeInSeconds;

        int minutes = (int) (timerInSeconds / 60);
        int seconds = (int) (timerInSeconds % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }

    private void startTimer() {
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

    /*
    * Setting up styles
    * */

    private void setupToggleGroup() {
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
    }



    private void setupTaskBox() {
        var taskName = new Label("Task name");
        taskName.getStyleClass().add(Styles.TITLE_3);
        var timeLabel = new Label("00:00");
        timeLabel.getStyleClass().addAll(Styles.ACCENT, Styles.TITLE_4);
        VBox.setMargin(timeLabel, new Insets(0, 0 , 0 ,3));
        var cb = new CheckBox();
        cb.getStyleClass().addAll(Styles.SUCCESS, Styles.TITLE_2);
        var aPane = new AnchorPane(
                taskName,
                cb
        );
        AnchorPane.setRightAnchor(cb, 14.0);
        AnchorPane.setTopAnchor(cb, 2.0);
        AnchorPane.setTopAnchor(taskName, 2.0);
        task_vbox.setStyle("-fx-border-width: 1px");
        task_vbox.setMaxWidth(600);
        task_vbox.setSpacing(0);
        task_vbox.setMaxHeight(20);
        task_vbox.setAlignment(Pos.CENTER_LEFT);
        task_vbox.getChildren().addAll(aPane, timeLabel);
    }
}
