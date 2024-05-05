/**
 * Represents a task in the time management application.
 * Each task has properties such as its name, deadline, time spent on it, completion status, and deletion flag.
 * This class provides getters for accessing these properties, and it encapsulates the task-related data and functionality.
 */


package com.timemanagement.Models;

import javafx.beans.property.*;
import java.time.LocalDate;

public class Task {
    // Properties
    private final IntegerProperty id;
    private final StringProperty taskName;
    private final DoubleProperty timeSpentInMinutes;
    private final ObjectProperty<LocalDate> deadline;
    private final BooleanProperty completed;
    private final BooleanProperty deleteFlag;

    // Constructor
    public Task(int id, String taskName, LocalDate deadline, Double timeSpentInMinutes, boolean completed) {
        this.taskName = new SimpleStringProperty(this, "task", taskName);
        this.deadline = new SimpleObjectProperty<>(this, "deadline", deadline);
        this.timeSpentInMinutes = new SimpleDoubleProperty(this, "time spent", timeSpentInMinutes);
        this.completed = new SimpleBooleanProperty(this, "completed", completed);
        this.deleteFlag = new SimpleBooleanProperty(this, "deleted", false);
        this.id = new SimpleIntegerProperty(this, "id", id);
    }

    // Getters for properties
    public StringProperty taskNameProperty() {
        return this.taskName;
    }

    public ObjectProperty<LocalDate> deadlineProperty() {
        return this.deadline;
    }

    public DoubleProperty timeSpentInMinutesProperty() {
        return this.timeSpentInMinutes;
    }

    public BooleanProperty completedProperty() {
        return this.completed;
    }

    public BooleanProperty deleteFlagProperty() {
        return this.deleteFlag;
    }

    public IntegerProperty idProperty() {
        return this.id;
    }
}
