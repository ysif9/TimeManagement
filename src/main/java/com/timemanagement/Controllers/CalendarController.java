package com.timemanagement.Controllers;

import atlantafx.base.controls.Calendar;

import com.timemanagement.Models.Model;
import javafx.fxml.Initializable;

import javafx.scene.control.Label;

import java.net.URL;



import java.util.ResourceBundle;

public class CalendarController implements Initializable {


    public Label task1;
    public Label task2;
    public Label task3;
    public Label task4;
    public Label task5;
    public Calendar calendar;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        calendar.valueProperty().addListener((observableValue, oldVal, newVal) ->
        {
            Model.getInstance().selectedDateProperty().set(newVal);

try{
    task1.setText(Model.getInstance().getTasksOnSelectedDate().getFirst().taskNameProperty().getValue());}
catch (Exception ex){
    task1.setText("");
    }

            try {
                task2.setText(Model.getInstance().getTasksOnSelectedDate().get(1).taskNameProperty().getValue());
            }catch (Exception ex){ task2.setText("");}

            try {
                task3.setText(Model.getInstance().getTasksOnSelectedDate().get(2).taskNameProperty().getValue());
            }catch (Exception ex){ task3.setText("");}

            try {
                task4.setText(Model.getInstance().getTasksOnSelectedDate().get(3).taskNameProperty().getValue());
            }catch (Exception ex){ task4.setText("");}

            try {
                task5.setText(Model.getInstance().getTasksOnSelectedDate().get(4).taskNameProperty().getValue());
            }catch (Exception ex){ task5.setText("");}

       });

    }
}
