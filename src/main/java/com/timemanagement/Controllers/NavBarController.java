/**
 * The NavBarController class manages the navigation bar UI elements and their interactions within the time management application.
 * It sets up event handlers for navigation buttons, handles button clicks to switch between different views (tasks, settings, calendar, focus),
 * and provides hover effects to enhance user experience. This class ensures proper styling of navigation buttons based on user interactions
 * and maintains the selected state of the active button for visual feedback.
 */


package com.timemanagement.Controllers;

import com.timemanagement.ChosenNavItem;
import com.timemanagement.Models.Model;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {

    // UI elements
    public VBox tasks_btn;
    public VBox focus_btn;
    public VBox calendar_btn;
    public VBox settings_btn;
    public HBox navbar;
    public FontAwesomeIconView focus_icon;
    private VBox selected = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set event handlers for navigation buttons
        setupButtonEvents();

        // Set hover logic for navigation buttons
        setHoverLogic();

        // Initially select the focus button
        selected = focus_btn;
    }

    // Set event handlers for navigation buttons
    private void setupButtonEvents() {
        tasks_btn.setOnMouseClicked(e -> onTasksClicked());
        settings_btn.setOnMouseClicked(e -> onSettingsClicked());
        calendar_btn.setOnMouseClicked(e -> onCalendarClicked());
        focus_btn.setOnMouseClicked(e -> onFocusClicked());
    }

    // Set hover logic for navigation buttons
    private void setHoverLogic() {
        for (Node node : navbar.getChildren()) {
            if (node instanceof VBox) {
                node.setOnMouseEntered(e -> onHover((VBox) node));
                node.setOnMouseExited(e -> onStoppedHover((VBox) node));
            }
        }
    }

    // Event handler for tasks button click
    private void onTasksClicked() {
        setSelectedStyle(tasks_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.TASKS);
    }

    // Event handler for settings button click
    private void onSettingsClicked() {
        setSelectedStyle(settings_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.SETTINGS);
    }

    // Event handler for focus button click
    private void onFocusClicked() {
        setSelectedStyle(focus_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.FOCUS);
    }

    // Event handler for calendar button click
    private void onCalendarClicked() {
        setSelectedStyle(calendar_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.CALENDAR);
    }

    // Apply selected style to the clicked button
    private void setSelectedStyle(VBox vBox) {
        if (selected != vBox) {
            resetStyle();
            selected = vBox;
            vBox.setStyle("-fx-border-color: -color-accent-emphasis;");
            for (Node node : vBox.getChildren()) {
                if (node instanceof Text) {
                    node.setStyle("-fx-fill: -color-accent-emphasis;");
                }
            }
        }
    }

    // Reset styles of previously selected button
    private void resetStyle() {
        if (selected != null) {
            selected.setStyle("-fx-border-color: -color-fg-muted;");
            for (Node node : selected.getChildren()) {
                if (node instanceof Text) {
                    node.setStyle("-fx-fill: -color-fg-muted;");
                }
            }
            selected = null;
        }
    }

    // Change style on hover
    private void onHover(VBox vBox) {
        if (selected == vBox) {
            return;
        }
        for (Node node : vBox.getChildren()) {
            if (node instanceof Text) {
                node.setStyle("-fx-fill: -color-accent-emphasis;");
            }
        }
    }

    // Restore style on hover exit
    private void onStoppedHover(VBox vBox) {
        if (selected == vBox) {
            return;
        }
        for (Node node : vBox.getChildren()) {
            if (node instanceof Text) {
                node.setStyle("-fx-fill: -color-fg-muted;");
            }
        }
    }
}
