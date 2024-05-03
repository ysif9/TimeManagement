package com.timemanagement.Controllers;

import atlantafx.base.controls.Calendar;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;

import java.net.URL;
import java.util.ResourceBundle;

public class CalenderController implements Initializable {
    public Calendar calender;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        calender.setCursor(Cursor.cursor("hand"));

    }
}
