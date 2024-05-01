package com.timemanagement;

import atlantafx.base.theme.CupertinoDark;
import com.timemanagement.Models.Model;
import javafx.application.Application;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        Font.loadFont(getClass().getResourceAsStream("/Fonts/Typo_Round_Regular_Demo.otf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/Fonts/Typo_Round_Bold_Demo.otf"), 12);

        Application.setUserAgentStylesheet(new CupertinoDark().getUserAgentStylesheet());

        Model.getInstance().getViewFactory().showApplication();
    }

}
