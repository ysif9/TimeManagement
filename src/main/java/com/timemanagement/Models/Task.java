package com.timemanagement.Models;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Task {
    private final StringProperty taskName;
    private final ObjectProperty<LocalDate> deadline;
    private final BooleanProperty completed;

    public Task(String taskName, LocalDate deadline) {
        this.taskName = new SimpleStringProperty(this, "task", taskName);
        this.deadline = new SimpleObjectProperty<>(this, "deadline", deadline);
        this.completed = new SimpleBooleanProperty(this, "completed", false);

    }

    public StringProperty taskNameProperty() {
        return this.taskName;
    }

    public ObjectProperty<LocalDate> deadlineProperty() {
        return this.deadline;
    }

    public BooleanProperty completedProperty() {
        return this.completed;
    }

}
