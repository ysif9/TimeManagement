/**
 * The FocusController class manages the focus view of the time management application,
 * which includes timers for focus and break sessions, task selection, and progress indication.
 * It initializes timers, sets up UI elements and event handlers, handles timer logic,
 * updates task time spent, and provides methods to convert timer percentage to minute and second format.
 * This class ensures proper functionality and styling of the focus view to facilitate efficient time management.
 */


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
    // Declare JavaFX elements
    public ToggleButton start_btn;
    public ToggleButton left_btn;
    public ToggleButton right_btn;
    public RingProgressIndicator ring_progress;
    public VBox task_vbox;
    public Label task_lbl;
    public Label time_lbl;
    public CheckBox task_cb;

    // Declare timers and properties
    private Timer focusTimer;
    private Timer breakTimer;
    private final ObjectProperty<CurrentTimer> currentTimer = new SimpleObjectProperty<>();
    private double oldTaskValue = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize timers and set default timer
        initializeTimers();

        // Bind UI elements to data models
        bindDataModels();

        // Set up UI styling and interaction
        setupUI();

        // Initialize task display
        setTask(null);
    }

    // Initialize timers
    private void initializeTimers() {
        focusTimer = new Timer(50);
        breakTimer = new Timer(10);
        currentTimer.set(CurrentTimer.FOCUS);
    }

    // Bind UI elements to data models
    private void bindDataModels() {
        // Bind selected task to UI
        bindSelectedTask();

        // Bind sliders to timers
        bindSlidersToTimers();

        // Bind progress indicators to timers
        bindProgressIndicatorsToTimers();

        // Bind toggle buttons to timer states
        bindToggleButtonsToTimerStates();
    }

    // Bind selected task to UI
    private void bindSelectedTask() {
        Model.getInstance().getViewFactory().getChosenNavItem().addListener((observable, oldValue, newValue) -> {
            if (newValue == ChosenNavItem.FOCUS) {
                Task selectedTask = Model.getInstance().getSelectedTask();
                setTask(selectedTask);
            }
        });
    }

    // Bind sliders to timers
    private void bindSlidersToTimers() {
        Model.getInstance().getFocusSlider().addListener((observable, oldValue, newValue) -> {
            if (oldValue.doubleValue() != 0.0) {
                focusTimer.timeProperty().set(newValue.longValue());
                if (currentTimer.get().equals(CurrentTimer.FOCUS)) {
                    start_btn.setSelected(false);
                    refreshProgressIndicator();
                }
            }
        });

        Model.getInstance().getBreakSlider().addListener((observable, oldValue, newValue) -> {
            if (oldValue.doubleValue() != 0.0) {
                breakTimer.timeProperty().set(newValue.longValue());
                if (currentTimer.get().equals(CurrentTimer.BREAK)) {
                    start_btn.setSelected(false);
                    refreshProgressIndicator();
                }
            }
        });
    }

    // Bind progress indicators to timers
    private void bindProgressIndicatorsToTimers() {
        focusTimer.percentageProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTimer.get().equals(CurrentTimer.FOCUS)) {
                ring_progress.setProgress(Math.clamp(newValue.doubleValue(), 0, 1));
            }
        });

        breakTimer.percentageProperty().addListener((observable, oldValue, newValue) -> {
            if (currentTimer.get().equals(CurrentTimer.BREAK)) {
                ring_progress.setProgress(Math.clamp(newValue.doubleValue(), 0, 1));
            }
        });

        focusTimer.timerDoneFlagProperty().addListener(observable -> {
            currentTimer.set(CurrentTimer.BREAK);
            right_btn.setSelected(true);
        });

        breakTimer.timerDoneFlagProperty().addListener(observable -> {
            currentTimer.set(CurrentTimer.FOCUS);
            left_btn.setSelected(true);
        });

        // Bind ring progress indicator to string converter
        ring_progress.setStringConverter(new StringConverter<>() {
            @Override
            public String toString(Double progress) {
                return percentageToMinuteSecond(progress);
            }

            @Override
            public Double fromString(String progress) {
                return 0d;
            }
        });
    }

    // Bind toggle buttons to timer states
    private void bindToggleButtonsToTimerStates() {
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
    }

    // Set up UI styling and interaction
    private void setupUI() {
        // Initialize styles and toggle group
        setupStylesAndToggleGroup();

        // Initialize task display
        setupTaskBox();

        // Initialize start button listener
        start_btn.selectedProperty().addListener((observable, oldValue, newValue) -> startButtonListener(newValue));
    }

    // Initialize styles and toggle group
    private void setupStylesAndToggleGroup() {
        start_btn.getStyleClass().add(Styles.ACCENT);
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

    // Initialize task display
    private void setupTaskBox() {
        // Initialize task label and checkbox
        task_lbl = new Label("Task name");
        task_lbl.getStyleClass().add(Styles.TITLE_3);
        time_lbl = new Label("00:00");
        time_lbl.setId("time_lbl");
        time_lbl.getStyleClass().addAll(Styles.ACCENT, Styles.TITLE_4);
        VBox.setMargin(time_lbl, new Insets(0, 0, 0, 3));
        task_cb = new CheckBox();
        task_cb.getStyleClass().addAll(Styles.SUCCESS, Styles.TITLE_2);

        // Set up task VBox layout
        AnchorPane aPane = new AnchorPane(task_lbl, task_cb);
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

    // Refresh progress indicator
    private void refreshProgressIndicator() {
        ring_progress.setProgress(0);
        ring_progress.setProgress(1);
    }

    // Set task details
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

    // Handle start button listener
    private void startButtonListener(boolean newValue) {
        Timer timerUsed = currentTimer.get().equals(CurrentTimer.FOCUS) ? focusTimer : breakTimer;
        if (newValue) {
            start_btn.setStyle("-fx-effect: null; -fx-text-fill: -color-accent-fg; -fx-background-color: transparent;");
            start_btn.setText("STOP");
            timerUsed.startTimer();
        } else {
            start_btn.setStyle("-fx-background-color: -color-accent-emphasis; -fx-effect: dropshadow(gaussian, -color-accent-muted, 0, 6, 0, 6);");
            start_btn.setText("START");
            if (Model.getInstance().getSelectedTask() != null) {
                oldTaskValue = Model.getInstance().getSelectedTask().timeSpentInMinutesProperty().get();
            }
            timerUsed.stopTimer();
        }
    }

    // Convert percentage to minute and second format
    private String percentageToMinuteSecond(double progress) {
        Timer timerUsed = currentTimer.get().equals(CurrentTimer.FOCUS) ? focusTimer : breakTimer;

        double timerInMinutes = progress * timerUsed.timeProperty().get();
        double totalTimerInMinutesPassed = timerUsed.timeProperty().get() - timerInMinutes;

        int minutes = (int) timerInMinutes;
        int seconds = (int) ((timerInMinutes - minutes) * 60);

        if (progress != 1 && progress != 0 && currentTimer.get().equals(CurrentTimer.FOCUS)) {
            updateTaskTime(totalTimerInMinutesPassed);
        }

        return String.format("%02d:%02d", minutes, seconds);
    }

    // Update task time
    private void updateTaskTime(double totalTimeInMinutes) {
        if (Model.getInstance().getSelectedTask() != null && totalTimeInMinutes != 0) {
            Model.getInstance().getSelectedTask().timeSpentInMinutesProperty().set(oldTaskValue + totalTimeInMinutes);
        }
    }

    // Enum for timer states
    public enum CurrentTimer {
        FOCUS,
        BREAK
    }
}

