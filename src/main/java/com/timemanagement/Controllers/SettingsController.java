package com.timemanagement.Controllers;

import atlantafx.base.controls.ToggleSwitch;
import com.timemanagement.Models.Model;
import com.timemanagement.Theme;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public ImageView avatar_image;
    public ToggleSwitch theme_toggle;
    public ToggleSwitch notifications_toggle;
    public DatePicker date_picker;
    public ChoiceBox weekstart_cb;
    public TextField name_field;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        theme_toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Model.getInstance().getThemeProperty().set(Theme.LIGHT);
            } else {
                Model.getInstance().getThemeProperty().set(Theme.DARK);
            }
        });
    }
}
