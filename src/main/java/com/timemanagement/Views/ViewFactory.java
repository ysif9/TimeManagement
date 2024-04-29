package com.timemanagement.Views;

import atlantafx.base.controls.RingProgressIndicator;
import com.timemanagement.Controllers.ApplicationController;
import com.timemanagement.Controllers.FocusController;
import com.timemanagement.Models.Model;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewFactory {
    private VBox focusView;
    private HBox navBarView;


    public VBox getFocusView() {
        if (focusView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Focus.fxml"));
                focusView = loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return focusView;
    }

    public HBox getNavBarView() {
        if (navBarView == null) {
            try {
                navBarView = new FXMLLoader(getClass().getResource("/Fxml/NavBar.fxml")).load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return navBarView;
    }

    public void showApplication() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Fxml/Application.fxml"));
        ApplicationController controller = new ApplicationController();
        fxmlLoader.setController(controller);
        createStage(fxmlLoader);
    }

    private void createStage(FXMLLoader fxmlLoader) {
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        //stage.setResizable(false);
        stage.setTitle("Chronus");
        stage.show();
    }
}
