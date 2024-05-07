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

import java.sql.SQLException;
import java.time.LocalDate;

public class Model {
    private final ViewFactory viewFactory;
    private static Model model;

    // Properties
    private final ObservableList<Task> allTasks = FXCollections.observableArrayList();
    private final ObjectProperty<Task> selectedTask;
    private final DoubleProperty focusSliderValue;
    private final DoubleProperty breakSliderValue;
    private final ObjectProperty<Theme> theme;
    private final ObjectProperty<LocalDate> selectedDate;
    private final ObservableList<Task> tasksOnSelectedDate;
    private final BooleanProperty notificationOn;

    // Private constructor to enforce singleton pattern
    private Model() {
        this.viewFactory = new ViewFactory();
        this.theme = new SimpleObjectProperty<>(this, "theme", Theme.DARK);
        this.focusSliderValue = new SimpleDoubleProperty();
        this.breakSliderValue = new SimpleDoubleProperty();
        this.selectedTask = new SimpleObjectProperty<>();
        this.selectedDate = new SimpleObjectProperty<>();
        this.tasksOnSelectedDate = FXCollections.observableArrayList();
        this.notificationOn = new SimpleBooleanProperty();
        loadTasksFromDatabase();
        // Listen for changes in selected date
        selectedDateProperty().addListener((observable) -> updateTasksOnSelectedDate());
    }

    // Singleton getInstance method
    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    /**
     * Task Logic
     */

    public ObservableList<Task> getAllTasks() {
        return allTasks;
    }

    private void loadTasksFromDatabase() {
        try {
            allTasks.addAll(DatabaseDriver.loadAllTasks());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public void createNewTask(Task task) {
        try {
            DatabaseDriver.saveTask(task);
            allTasks.add(task);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateTask(Task task) {
        try {
            DatabaseDriver.saveTask(task);
            allTasks.get(task.idProperty().get()).completedProperty().set(task.completedProperty().get());
            allTasks.get(task.idProperty().get()).deadlineProperty().set(task.deadlineProperty().get());
            var oldValue = selectedDateProperty().get();
            selectedDateProperty().set(LocalDate.EPOCH);
            selectedDateProperty().set(oldValue);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteTask (Task task) {
        try {
            DatabaseDriver.deleteTask(task.idProperty().get());
            allTasks.remove(task);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    // Utility method to format task time for display in UI
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
    public ObjectProperty<Task> getSelectedTask() {
        return selectedTask;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask.set(selectedTask);
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

    public ObjectProperty<LocalDate> selectedDateProperty() {
        return selectedDate;
    }

    public ObservableList<Task> getTasksOnSelectedDate() {
        return tasksOnSelectedDate;
    }

    public BooleanProperty getNotificationOnProperty() {
        return notificationOn;
    }

    private void updateTasksOnSelectedDate() {
        getTasksOnSelectedDate().clear();
        for (Task allTask : getAllTasks()) {
            if (allTask.deadlineProperty().getValue().equals(selectedDateProperty().get()) && !allTask.completedProperty().get()) {
                Model.getInstance().getTasksOnSelectedDate().add(allTask);
            }
        }
    }

    public void saveAllTasks() {
        for (Task task : allTasks) {
            updateTask(task);
        }
    }

}
