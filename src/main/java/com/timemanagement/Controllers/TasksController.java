package com.timemanagement.Controllers;

import atlantafx.base.controls.Calendar;
import atlantafx.base.controls.CustomTextField;
import atlantafx.base.controls.Popover;
import atlantafx.base.controls.Spacer;
import atlantafx.base.theme.Tweaks;
import com.timemanagement.Models.Task;
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
    public ListView<Task> today_listview;
    public ListView<Task> completed_listview;
    public ListView<Task> upcoming_listview;
    public Accordion accordion;
    public TitledPane today_pane;
    public VBox parent_vbox;


    private final ObservableList<Task> tasks = FXCollections.observableArrayList();
    public HBox toolbar_newtask;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        newTaskSetup();
        listviewSetup();
    }


    private void listviewSetup() {
        double maxHeight = 500;

        today_listview.prefHeightProperty().bind(
                Bindings.when(Bindings.size(tasks).multiply(50).add(20).lessThan(maxHeight))
                        .then(Bindings.size(tasks).multiply(50).add(20))
                        .otherwise(maxHeight)
        );
        upcoming_listview.maxHeightProperty().bind(Bindings.size(tasks).multiply(70));
        completed_listview.maxHeightProperty().bind(Bindings.size(tasks).multiply(70));

        today_listview.setItems(tasks);
        today_listview.setCellFactory(e -> new TaskCellFactory());
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
            if (!textField.getText().isEmpty() && cal.getValue() != null ) {
                Task task = new Task(textField.getText(), cal.getValue(), "00:00");
                tasks.add(task);
                textField.setText("");
                cal.setValue(null);
            }
        });

    }

    static class FutureDateCell extends DateCell {

        @Override
        public void updateItem(LocalDate date, boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.isBefore(LocalDate.now()));
        }
    }
}
