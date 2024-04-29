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
                default -> client_parent.setCenter(Model.getInstance().getViewFactory().getFocusView());
            }
        });



    }


}
