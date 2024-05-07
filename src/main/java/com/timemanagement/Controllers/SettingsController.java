/**
 * The SettingsController class handles the user interface and logic associated with the settings view of the time management application.
 * It initializes and manages UI components such as sliders, toggle switches, and labels for configuring various settings such as theme,
 * notifications, focus time, and break time. This class sets up listeners for user interactions, binds UI elements to model properties,
 * and updates the UI based on changes in settings.
 */

package com.timemanagement.Controllers;

import atlantafx.base.controls.ProgressSliderSkin;
import atlantafx.base.controls.ToggleSwitch;
import com.timemanagement.Models.Model;
import com.timemanagement.Theme;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.ImageView;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    // UI elements
    public ImageView avatar_image;
    public ToggleSwitch theme_toggle;
    public ToggleSwitch notifications_toggle;
    public Slider focus_time_slider;
    public Slider break_time_slider;
    public Label focus_time_lbl;
    public Label break_time_lbl;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize sliders
        initializeSliders();

        // Theme switch listener
        theme_toggle.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                Model.getInstance().getThemeProperty().set(Theme.LIGHT);
            } else {
                Model.getInstance().getThemeProperty().set(Theme.DARK);
            }
        });

        // Notification toggle
        notifications_toggle.selectedProperty().addListener((observable, oldValue, newValue) -> Model.getInstance().getNotificationOnProperty().set(newValue));

        // Bind sliders to model properties and update labels
        bindSlidersAndLabels();

        // Set notifications on by default
        notifications_toggle.setSelected(true);
    }

    // Initialize sliders with min and max values
    private void initializeSliders() {
        focus_time_slider.setSkin(new ProgressSliderSkin(focus_time_slider));
        break_time_slider.setSkin(new ProgressSliderSkin(break_time_slider));
        focus_time_slider.setMin(5);
        break_time_slider.setMin(5);
        break_time_slider.setMax(60);
        focus_time_slider.setMax(60);
    }

    // Bind slider values to model properties and update labels
    private void bindSlidersAndLabels() {
        focus_time_slider.valueProperty().addListener(observable -> focus_time_lbl.setText(String.valueOf(((int) focus_time_slider.getValue()))));
        Model.getInstance().getFocusSlider().bind(focus_time_slider.valueProperty());
        Model.getInstance().getBreakSlider().bind(break_time_slider.valueProperty());
        break_time_slider.valueProperty().addListener(observable -> break_time_lbl.setText(String.valueOf(((int) break_time_slider.getValue()))));
    }
}
