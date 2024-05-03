package com.timemanagement.Controllers;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.controls.ToggleSwitch;
import com.timemanagement.Models.Model;
import com.timemanagement.Theme;
import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {
    public ImageView avatar_image;
    public ToggleSwitch theme_toggle;
    public ToggleSwitch notifications_toggle;
    public Slider focus_time_slider;
    public Slider break_time_slider;
    public Label focus_time_lbl;
    public Label break_time_lbl;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //initialization
        focus_time_slider.setValue(50);
        break_time_slider.setValue(10);
        focus_time_slider.setSkin(new ProgressSliderSkin(focus_time_slider));
        break_time_slider.setSkin(new ProgressSliderSkin(break_time_slider));

        //theme switch
        theme_toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Model.getInstance().getThemeProperty().set(Theme.LIGHT);
            } else {
                Model.getInstance().getThemeProperty().set(Theme.DARK);
            }
        });

        //sliders listeners
        focus_time_slider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
           focus_time_lbl.setText(String.valueOf(((int)focus_time_slider.getValue()*60 /100)));

        });;
        break_time_slider.valueChangingProperty().addListener((observable, oldValue, newValue) -> {
            break_time_lbl.setText(String.valueOf(((int)break_time_slider.getValue() *60 /100)));
        });;

    }
}
