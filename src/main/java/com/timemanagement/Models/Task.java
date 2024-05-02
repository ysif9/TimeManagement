package com.timemanagement.Models;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Task {
    private final StringProperty taskName;
    private final StringProperty timeSpent;
    private final ObjectProperty<LocalDate> deadline;
    private final BooleanProperty completed;

    public Task(String taskName, LocalDate deadline, String timeSpent) {
        this.taskName = new SimpleStringProperty(this, "task", taskName);
        this.deadline = new SimpleObjectProperty<>(this, "deadline", deadline);
        this.timeSpent = new SimpleStringProperty(this, "time spent", timeSpent);
        this.completed = new SimpleBooleanProperty(this, "completed", false);
    }


    public StringProperty taskNameProperty() {
        return this.taskName;
    }

    public ObjectProperty<LocalDate> deadlineProperty() {
        return this.deadline;
    }

    public StringProperty timeSpentProperty() {
        return this.timeSpent;
    }

    public BooleanProperty completedProperty() {
        return this.completed;
    }

}
