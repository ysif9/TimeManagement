package com.timemanagement.Controllers;

import atlantafx.base.controls.*;
import com.timemanagement.ChosenNavItem;
import com.timemanagement.Models.Model;
import com.timemanagement.Models.Task;
import com.timemanagement.Models.Timer;
import com.timemanagement.Styles;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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
    public VBox task_vbox;
    public Label task_lbl;
    public Label time_lbl;
    public  CheckBox task_cb;
    private Timer focusTimer;
    private Timer breakTimer;
    private final ObjectProperty<CurrentTimer> currentTimer = new SimpleObjectProperty<>();
    private double oldTaskValue = 0;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // init values
        focusTimer = new Timer(50);
        breakTimer = new Timer(10);
        currentTimer.set(CurrentTimer.FOCUS);
        start_btn.getStyleClass().add(Styles.ACCENT);
        start_btn.selectedProperty().addListener((observable, oldValue, newValue) -> startButtonListener(newValue));

        // set task to currently selected task
        Model.getInstance().getViewFactory().getChosenNavItem().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue == ChosenNavItem.FOCUS) {
                if (Model.getInstance().getSelectedTask() != null) {
                    setTask(Model.getInstance().getSelectedTask());
                } else {
                    setTask(null);
                }
            }
        } ));

        // bind focus slider to focus timer
        Model.getInstance().getFocusSlider().addListener(((observableValue, oldValue, newValue) -> {
            if (oldValue.doubleValue() != 0.0) {
                focusTimer.timeProperty().set(newValue.longValue());
                if (currentTimer.get().equals(CurrentTimer.FOCUS)) {
                    start_btn.setSelected(false);
                    refreshProgressIndicator();
                }
            }
        }));

        // bind break slider to break timer
        Model.getInstance().getBreakSlider().addListener(((observableValue, oldValue, newValue) -> {
            if (oldValue.doubleValue() != 0.0) {
                breakTimer.timeProperty().set(newValue.longValue());
                if (currentTimer.get().equals(CurrentTimer.BREAK)) {
                    start_btn.setSelected(false);
                    refreshProgressIndicator();
                }
            }

        }));

        //turn progress to string
        ring_progress.setStringConverter(new StringConverter<>() {
            @Override
            public String toString(Double progress) {return percentageToMinuteSecond(progress);}

            @Override
            public Double fromString(String progress) {return 0d;}
        });

        // bind percentage to ring progress indicator
        focusTimer.percentageProperty().addListener((observableValue, oldValue, newValue) -> {
            if (currentTimer.get().equals(CurrentTimer.FOCUS)) {
                ring_progress.setProgress(Math.clamp(newValue.doubleValue(), 0, 1));
            }
        });
        breakTimer.percentageProperty().addListener((observableValue, oldValue, newValue) -> {
            if (currentTimer.get().equals(CurrentTimer.BREAK)) {
                ring_progress.setProgress(Math.clamp(newValue.doubleValue(), 0, 1));
            }
        });
        focusTimer.timerDoneFlagProperty().addListener(observableValue -> {
            currentTimer.set(CurrentTimer.BREAK);
            right_btn.setSelected(true);
        });
        breakTimer.timerDoneFlagProperty().addListener(observableValue -> {
            currentTimer.set(CurrentTimer.FOCUS);
            left_btn.setSelected(true);
        });

        // setting up styles
        setupToggleGroup();
        setupTaskBox();

        // setting logic for toggle buttons
        currentTimer.addListener((observableValue, oldValue, newValue) -> {
            if (oldValue.equals(CurrentTimer.FOCUS)) {
                focusTimer.stopTimer();
                start_btn.setSelected(false);
                refreshProgressIndicator();
            } else if (oldValue.equals(CurrentTimer.BREAK)) {
                breakTimer.stopTimer();
                start_btn.setSelected(false);
                refreshProgressIndicator();
            }
        });
        left_btn.setOnMouseClicked(e -> currentTimer.set(CurrentTimer.FOCUS));
        right_btn.setOnMouseClicked(e -> currentTimer.set(CurrentTimer.BREAK));
        setTask(null);
    }

    private void refreshProgressIndicator() {
        ring_progress.setProgress(0);
        ring_progress.setProgress(1);
    }

    private void setTask(Task task) {
        if (task == null) {
            task_lbl.setText("No task selected.");
            time_lbl.textProperty().unbind();
            time_lbl.setText("");
            task_cb.setSelected(false);
        } else {
            task_lbl.setText(task.taskNameProperty().get());
            Model.formatTaskTime(task, time_lbl);
            task.completedProperty().bindBidirectional(task_cb.selectedProperty());
            task.completedProperty().addListener((observableValue, oldValue, newValue) -> {
                if (newValue) {
                    setTask(null);
                }
            });
            oldTaskValue = task.timeSpentInMinutesProperty().get();
        }
    }

    /*
     * Timer logic
     * */

    private void startButtonListener(boolean newValue) {
        Timer timerUsed;
        if (currentTimer.get().equals(CurrentTimer.FOCUS)) {
            timerUsed = focusTimer;
        } else {
            timerUsed = breakTimer;
        }
        if (newValue) {
            start_btn.setStyle(
                    "-fx-effect: null;" +
                            "-fx-text-fill: -color-accent-fg;" +
                            "-fx-background-color: transparent;");
            start_btn.setText("STOP");
            timerUsed.startTimer();
        } else {
            start_btn.setStyle(
                    "-fx-background-color: -color-accent-emphasis;" +
                            "-fx-effect: dropshadow(gaussian, -color-accent-muted, 0, 6, 0, 6);");
            start_btn.setText("START");
            if (Model.getInstance().getSelectedTask() != null) {
                oldTaskValue = Model.getInstance().getSelectedTask().timeSpentInMinutesProperty().get();
            }
            timerUsed.stopTimer();
        }
    }

    private String percentageToMinuteSecond(double progress) {
        Timer timerUsed;
        if (currentTimer.get().equals(CurrentTimer.FOCUS)) {
            timerUsed = focusTimer;
        } else {
            timerUsed = breakTimer;
        }

        double timerInMinutes = progress * timerUsed.timeProperty().get();
        double totalTimerInMinutesPassed = timerUsed.timeProperty().get() - timerInMinutes;

        int minutes = (int) timerInMinutes;
        int seconds = (int) ((timerInMinutes - minutes) * 60);

        if (progress != 1 && progress != 0 && currentTimer.get().equals(CurrentTimer.FOCUS)) {
            updateTaskTime(totalTimerInMinutesPassed);
        }

        return String.format("%02d:%02d", minutes, seconds);
    }

    private void updateTaskTime(double totalTimeInMinutes) {

       if (Model.getInstance().getSelectedTask() != null && totalTimeInMinutes != 0) {
           Model.getInstance().getSelectedTask().timeSpentInMinutesProperty().set(oldTaskValue + totalTimeInMinutes);
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
        task_lbl = new Label("Task name");

        task_lbl.getStyleClass().add(Styles.TITLE_3);
        time_lbl = new Label("00:00");
        time_lbl.setId("time_lbl");
        time_lbl.getStyleClass().addAll(Styles.ACCENT, Styles.TITLE_4);
        VBox.setMargin(time_lbl, new Insets(0, 0 , 0 ,3));
        task_cb = new CheckBox();
        task_cb.getStyleClass().addAll(Styles.SUCCESS, Styles.TITLE_2);
        var aPane = new AnchorPane(
                task_lbl,
                task_cb
        );
        AnchorPane.setRightAnchor(task_cb, 14.0);
        AnchorPane.setTopAnchor(task_cb, 2.0);
        AnchorPane.setTopAnchor(task_lbl, 2.0);
        task_vbox.setStyle("-fx-border-width: 1px");
        task_vbox.setMaxWidth(600);
        task_vbox.setSpacing(0);
        task_vbox.setMaxHeight(20);
        task_vbox.setAlignment(Pos.CENTER_LEFT);
        task_vbox.getChildren().addAll(aPane, time_lbl);
    }

    public enum CurrentTimer {
        FOCUS,
        BREAK
    }

}
