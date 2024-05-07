package com.timemanagement;

import atlantafx.base.theme.CupertinoDark;
import atlantafx.base.theme.CupertinoLight;

import com.timemanagement.Models.DatabaseDriver;
import com.timemanagement.Models.Model;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.sql.SQLException;

public class App extends Application {

    @Override
    public void start(Stage stage) {

        Font.loadFont(getClass().getResourceAsStream("/Fonts/Typo_Round_Regular_Demo.otf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/Fonts/Typo_Round_Bold_Demo.otf"), 12);
        try {
            DatabaseDriver.createTable();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());

        Model.getInstance().getViewFactory().showApplication();
        Model.getInstance().getThemeProperty().addListener((obs, oldValue, newValue) -> {
           if (newValue == Theme.DARK) {
               Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());
           } else if (newValue == Theme.LIGHT) {
               Application.setUserAgentStylesheet(new CupertinoLight().getUserAgentStylesheet());
           }
        });
    }

}
