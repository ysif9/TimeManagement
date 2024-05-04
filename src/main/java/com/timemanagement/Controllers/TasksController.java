package com.timemanagement.Controllers;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Tweaks;
import com.timemanagement.Models.DatabaseDriver;
import com.timemanagement.Models.Model;
import com.timemanagement.Models.NoSelectionModel;
import com.timemanagement.Models.Task;
import com.timemanagement.Styles;
import com.timemanagement.Views.TaskCellFactory;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TasksController implements Initializable {
    private final ObservableList<Task> allTasks = FXCollections.observableArrayList();
    public ListView<Task> today_listview;
    public ListView<Task> completed_listview;
    public ListView<Task> upcoming_listview;
    public Accordion accordion;
    public TitledPane today_pane;
    public VBox parent_vbox;
    private final ObservableList<Task> todayTasks = FXCollections.observableArrayList();
    private final ObservableList<Task> upcomingTasks = FXCollections.observableArrayList();
    private final ObservableList<Task> completedTasks = FXCollections.observableArrayList();
    public HBox toolbar_newtask;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            allTasks.addAll(DatabaseDriver.loadAllTasks());
            populateTaskLists();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


        newTaskSetup();
        listviewSetup();

        Model.getInstance().getExitingFlagProperty().addListener(observable -> {
            try {
                saveAllTasks();
            } catch (SQLException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void populateTaskLists() {
        for (Task task : allTasks) {
            if (task.completedProperty().get()) {
                completedTasks.add(task);
            } else if (task.deadlineProperty().get().isAfter(LocalDate.now())) {
                upcomingTasks.add(task);
            } else if (task.deadlineProperty().get().isEqual(LocalDate.now())) {
                todayTasks.add(task);
            }
            setTaskListeners(task);
        }
    }

    private void listviewSetup() {

        bindHeight(today_listview, todayTasks);
        bindHeight(upcoming_listview, upcomingTasks);
        bindHeight(completed_listview, completedTasks);

        completed_listview.setSelectionModel(new NoSelectionModel<>());
        today_listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Model.getInstance().setSelectedTask(newValue);
                upcoming_listview.getSelectionModel().clearSelection();
            } else  {
                Model.getInstance().setSelectedTask(null);
            }
        });
        upcoming_listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Model.getInstance().setSelectedTask(newValue);
                today_listview.getSelectionModel().clearSelection();
            } else  {
                Model.getInstance().setSelectedTask(null);
            }
        });

        setCell(today_listview, todayTasks);
        setCell(upcoming_listview, upcomingTasks);
        setCell(completed_listview, completedTasks);

    }

    private void setCell(ListView<Task> listView, ObservableList<Task> tasks) {
        listView.setItems(tasks);
        listView.setCellFactory(e -> new TaskCellFactory());
    }

    private void bindHeight(ListView<Task> listView, ObservableList<Task> tasks) {
        double maxHeight = 500;
        listView.prefHeightProperty().bind(
                Bindings.when(Bindings.size(tasks).multiply(50).add(20).lessThan(maxHeight))
                        .then(Bindings.size(tasks).multiply(50).add(20))
                        .otherwise(maxHeight)
        );
    }

    private void newTaskSetup() {

        toolbar_newtask.setStyle("-fx-border-width: 1px; -fx-border-color: -color-border-default;");

        var textField = new CustomTextField();
        textField.setPromptText("What are you working on?");
        textField.setPrefWidth(500);
        textField.setFocusTraversable(false);

        var cal = new Calendar(LocalDate.now());
        cal.setDayCellFactory(c -> new FutureDateCell());
        cal.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        cal.setValue(null);

        var pop2 = new Popover(cal);
        pop2.setHeaderAlwaysVisible(false);
        pop2.setDetachable(false);
        pop2.setArrowLocation(Popover.ArrowLocation.TOP_LEFT);

        var datePickerButton = new Button(null, new FontIcon(Feather.CALENDAR));
        datePickerButton.setOnMouseClicked(e -> pop2.show(datePickerButton));
        datePickerButton.getStyleClass().add(Styles.FLAT);
        var newTaskBtn = new Button("Add Task", new FontIcon(Feather.PLUS));
        newTaskBtn.getStyleClass().add(Styles.ACCENT);

        toolbar_newtask.setAlignment(Pos.CENTER_LEFT);
        toolbar_newtask.getChildren().addAll(textField, new Spacer(120), datePickerButton, new Spacer(10), newTaskBtn);

        newTaskBtn.setOnMouseClicked(e -> {
            if (!textField.getText().isEmpty()) {
                LocalDate calVal;
                if (cal.getValue() != null) {
                    calVal = cal.getValue();
                } else {
                    calVal = LocalDate.now();
                }
                Task task = new Task(allTasks.size(), textField.getText(), calVal, 0.0, false);

                addTask(task);

                textField.setText("");
                cal.setValue(null);
            }
        });
    }

    private void addTask(Task task) {
        if (task.deadlineProperty().get().isEqual(LocalDate.now())) {
            todayTasks.add(task);
        } else if (task.deadlineProperty().get().isAfter(LocalDate.now())) {
            upcomingTasks.add(task);
        }

        setTaskListeners(task);

        try {
            DatabaseDriver.saveTask(task);
            allTasks.add(task);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void setTaskListeners(Task task) {
        task.completedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                todayTasks.removeIf(t -> t.equals(task));
                upcomingTasks.removeIf(t -> t.equals(task));
                completedTasks.add(task);
            }
        });
        task.deleteFlagProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                todayTasks.removeIf(t -> t.equals(task));
                upcomingTasks.removeIf(t -> t.equals(task));
                completedTasks.removeIf(t -> t.equals(task));
                try {
                    DatabaseDriver.deleteTask(task.idProperty().get());
                    allTasks.remove(task);
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void saveAllTasks() throws SQLException, ClassNotFoundException {
        for (Task task : allTasks) {
            DatabaseDriver.saveTask(task);
        }
    }

    static class FutureDateCell extends DateCell {

        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.isBefore(LocalDate.now()));
        }
    }
}
