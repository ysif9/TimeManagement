/**
 * Singleton class representing the application's model.
 * This class provides access to various properties and functionalities used throughout the application.
 */

package com.timemanagement.Models;

import com.timemanagement.Theme;
import com.timemanagement.Views.ViewFactory;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;

import java.time.LocalDate;

public class Model {
    private final ViewFactory viewFactory;
    private static Model model;

    // Properties
    private Task selectedTask;
    private final DoubleProperty focusSliderValue;
    private final DoubleProperty breakSliderValue;
    private final ObjectProperty<Theme> theme;
    private final BooleanProperty exitingFlag;
    private final ObjectProperty<LocalDate> selectedDate;
    private final ObservableList<Task> tasksOnSelectedDate;

    // Private constructor to enforce singleton pattern
    private Model() {
        this.viewFactory = new ViewFactory();
        this.theme = new SimpleObjectProperty<>(this, "theme", Theme.DARK);
        this.focusSliderValue = new SimpleDoubleProperty();
        this.breakSliderValue = new SimpleDoubleProperty();
        this.exitingFlag = new SimpleBooleanProperty(false);
        this.selectedDate = new SimpleObjectProperty<>();
        this.tasksOnSelectedDate = FXCollections.observableArrayList();
    }

    // Singleton getInstance method
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    // Utility method to format task time for display
    public static void formatTaskTime(Task task, Label timeLbl) {
        timeLbl.textProperty().bind(Bindings.createStringBinding(
                () -> {
                    double totalSeconds = task.timeSpentInMinutesProperty().get() * 60;
                    int minutes = (int) Math.floor(totalSeconds / 60);
                    int seconds = (int) (totalSeconds - minutes * 60);
                    return String.format("%02d:%02d", minutes, seconds);
                },
                task.timeSpentInMinutesProperty()
        ));
    }

    // Getters and setters for properties
    public Task getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public DoubleProperty getFocusSlider() {
        return focusSliderValue;
    }

    public DoubleProperty getBreakSlider() {
        return breakSliderValue;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public ObjectProperty<Theme> getThemeProperty() {
        return theme;
    }

    public BooleanProperty getExitingFlagProperty() {
        return exitingFlag;
    }

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public ObservableList<Task> getTasksOnSelectedDate() {
        return tasksOnSelectedDate;
    }
}
