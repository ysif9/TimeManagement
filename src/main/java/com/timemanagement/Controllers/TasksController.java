/**
 * The TasksController class serves as the controller for managing tasks within the time management application.
 * It handles the initialization of UI components, loading tasks from the database, populating task lists based on categories,
 * adding new tasks, updating task lists based on selected dates, and managing task properties such as completion and deletion.
 */

package com.timemanagement.Controllers;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Tweaks;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class TasksController implements Initializable {

    // ObservableLists for different task categories
    private final ObservableList<Task> todayTasks = FXCollections.observableArrayList();
    private final ObservableList<Task> upcomingTasks = FXCollections.observableArrayList();
    private final ObservableList<Task> completedTasks = FXCollections.observableArrayList();

    // UI elements
    public ListView<Task> today_listview;
    public ListView<Task> completed_listview;
    public ListView<Task> upcoming_listview;
    public Accordion accordion;
    public TitledPane today_pane;
    public VBox parent_vbox;
    public HBox toolbar_newtask;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateTaskLists();

        // Setup new task UI components
        setupNewTask();
        // Setup list views
        setupListViews();

        //CSS styling
        toolbar_newtask.setStyle("-fx-border-width: 1px; -fx-border-color: -color-border-default;");
    }

    // Populate task lists based on task categories
    private void populateTaskLists() {
        for (Task task : Model.getInstance().getAllTasks()) {
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

    // Setup list views
    private void setupListViews() {
        bindHeight(today_listview, todayTasks);
        bindHeight(upcoming_listview, upcomingTasks);
        bindHeight(completed_listview, completedTasks);

        completed_listview.setSelectionModel(new NoSelectionModel<>());
        setupListViewSelectionListeners();
        setCell(today_listview, todayTasks);
        setCell(upcoming_listview, upcomingTasks);
        setCell(completed_listview, completedTasks);
    }

    // Bind list view height dynamically
    private void bindHeight(ListView<Task> listView, ObservableList<Task> tasks) {
        double maxHeight = 500;
        listView.prefHeightProperty().bind(
                Bindings.when(Bindings.size(tasks).multiply(50).add(20).lessThan(maxHeight))
                        .then(Bindings.size(tasks).multiply(50).add(20))
                        .otherwise(maxHeight)
        );
    }

    // Setup new task UI components
    private void setupNewTask() {
        // Create new task components
        var textField = createCustomTextField();
        var cal = createCalendar();
        var datePickerButton = createDatePickerButton(cal);
        var newTaskBtn = createNewTaskButton(textField, cal);

        // Add components to toolbar
        toolbar_newtask.setAlignment(Pos.CENTER_LEFT);
        toolbar_newtask.getChildren().addAll(textField, new Spacer(120), datePickerButton, new Spacer(10), newTaskBtn);
    }

    // Create custom text field for new task
    private CustomTextField createCustomTextField() {
        var textField = new CustomTextField();
        textField.setPromptText("What are you working on?");
        textField.setPrefWidth(500);
        textField.setFocusTraversable(false);
        return textField;
    }

    // Create calendar for selecting task deadline
    private Calendar createCalendar() {
        var cal = new Calendar(LocalDate.now());
        cal.setDayCellFactory(c -> new FutureDateCell());
        cal.getStyleClass().add(Tweaks.EDGE_TO_EDGE);
        cal.setValue(null);
        return cal;
    }

    // Create button for opening date picker popover
    private Button createDatePickerButton(Calendar cal) {
        var datePickerButton = new Button(null, new FontIcon(Feather.CALENDAR));
        var pop2 = createCalendarPopover(cal);
        datePickerButton.setOnMouseClicked(e -> pop2.show(datePickerButton));
        datePickerButton.getStyleClass().add(Styles.FLAT);
        return datePickerButton;
    }

    // Create popover for calendar
    private Popover createCalendarPopover(Calendar cal) {
        var pop2 = new Popover(cal);
        pop2.setHeaderAlwaysVisible(false);
        pop2.setDetachable(false);
        pop2.setArrowLocation(Popover.ArrowLocation.TOP_LEFT);
        return pop2;
    }

    // Create button for adding new task
    private Button createNewTaskButton(CustomTextField textField, Calendar cal) {
        var newTaskBtn = new Button("Add Task", new FontIcon(Feather.PLUS));
        newTaskBtn.getStyleClass().add(Styles.ACCENT);
        newTaskBtn.setOnMouseClicked(e -> addNewTask(textField, cal));
        return newTaskBtn;
    }

    // Add new task
    private void addNewTask(CustomTextField textField, Calendar cal) {
        if (!textField.getText().isEmpty()) {
            LocalDate calVal = (cal.getValue() != null) ? cal.getValue() : LocalDate.now();
            Task task = new Task(Model.getInstance().getAllTasks().size(), textField.getText(), calVal, 0.0, false);
            // Create new task in Model
            Model.getInstance().createNewTask(task);
            addToAppropriateTaskList(task);
            clearTextFieldAndCalendar(textField, cal);
        }
    }

    // Add task to appropriate task list
    private void addToAppropriateTaskList(Task task) {
        if (task.deadlineProperty().get().isEqual(LocalDate.now())) {
            todayTasks.add(task);
        } else if (task.deadlineProperty().get().isAfter(LocalDate.now())) {
            upcomingTasks.add(task);
        }
    }

    // Clear text field and calendar after adding task
    private void clearTextFieldAndCalendar(CustomTextField textField, Calendar cal) {
        textField.setText("");
        cal.setValue(null);
    }


    // Setup list view selection listeners
    private void setupListViewSelectionListeners() {
        today_listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Model.getInstance().setSelectedTask(newValue);
                upcoming_listview.getSelectionModel().clearSelection();
            } else {
                Model.getInstance().setSelectedTask(null);
            }
        });
        upcoming_listview.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                Model.getInstance().setSelectedTask(newValue);
                today_listview.getSelectionModel().clearSelection();
            } else {
                Model.getInstance().setSelectedTask(null);
            }
        });
    }

    // Set custom cell factory for list views
    private void setCell(ListView<Task> listView, ObservableList<Task> tasks) {
        listView.setItems(tasks);
        listView.setCellFactory(e -> new TaskCellFactory());
    }

    // Set listeners for task properties
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
                Model.getInstance().deleteTask(task);
            }
        });
    }

    // Date cell for future dates
    static class FutureDateCell extends DateCell {
        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.isBefore(LocalDate.now()));
        }
    }
}