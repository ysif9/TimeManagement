/**
 * Controller class for managing the application's main layout.
 * This class is responsible for updating the center content of the BorderPane based on the chosen navigation item.
 * It listens for changes in the chosen navigation item and updates the center content accordingly.
 */
package com.timemanagement.Controllers;

import com.timemanagement.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ApplicationController implements Initializable {

    public BorderPane client_parent;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Model.getInstance().getViewFactory().getChosenNavItem().addListener((observable, oldValue, newValue) -> {
            switch (newValue) {
                case SETTINGS -> client_parent.setCenter(Model.getInstance().getViewFactory().getSettingsView());
                case TASKS -> client_parent.setCenter(Model.getInstance().getViewFactory().getTasksView());
                case CALENDAR -> client_parent.setCenter(Model.getInstance().getViewFactory().getCalenderView());
                case FOCUS -> client_parent.setCenter(Model.getInstance().getViewFactory().getFocusView());
                default -> System.out.println("nothing");
            }
        });
    }
}

