package com.timemanagement.Controllers;

import com.timemanagement.Models.NoSelectionModel;
import com.timemanagement.Models.Task;
import com.timemanagement.Views.TaskCellFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.kordamp.ikonli.feather.Feather;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class TasksController implements Initializable {
    public ListView<Task> today_listview;
    public ListView<Task> completed_listview;
    public ListView<Task> upcoming_listview;
    public Accordion accordion;
    public TitledPane today_pane;
    public VBox parent_vbox;
    public TextField task_field;
    public DatePicker deadline_dp;
    public Button newtask_btn;

    private final ObservableList<Task> tasks = FXCollections.observableArrayList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setButtonStyle();
        deadline_dp.setConverter(changeDateFormat());

        deadline_dp.setDayCellFactory(picker -> disablePastDates());

        // disable mouse selection of tasks in listview
        today_listview.setSelectionModel(new NoSelectionModel<>());
        completed_listview.setSelectionModel(new NoSelectionModel<>());
        upcoming_listview.setSelectionModel(new NoSelectionModel<>());

        today_listview.setItems(tasks);
        today_listview.setCellFactory(e -> new TaskCellFactory());

        newtask_btn.setOnMouseClicked(e -> onCreateTask());
    }

    private void setButtonStyle() {
        var icon = new FontIcon(Feather.PLUS);
        newtask_btn.getStyleClass().addAll(
                Styles.ACCENT, Styles.BUTTON_OUTLINED
        );
        newtask_btn.setGraphic(icon);
    }

    private StringConverter<LocalDate> changeDateFormat() {
       return new StringConverter<>() {
            private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String s) {
                return LocalDate.parse(s, dateFormatter);
            }
        };
    }

    private DateCell disablePastDates() {
        return new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);


                if (date.isBefore(LocalDate.now())) {
                    setDisable(true);
                    setStyle("-fx-background-color: #3A3A3C;");
                }
            }
        };
    }

    private void onCreateTask() {
        if (!task_field.getText().isEmpty() && deadline_dp.getValue() != null ) {
            Task task = new Task(task_field.getText(), deadline_dp.getValue());
            tasks.add(task);
            task_field.setText("");
            deadline_dp.setValue(null);
        }
    }

}
