package com.timemanagement.Views;

import com.timemanagement.ChosenNavItem;
import com.timemanagement.Controllers.ApplicationController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ViewFactory {
    private VBox focusView;
    private HBox navBarView;
    private VBox tasksView;
    private AnchorPane settingsView;
    private final ObjectProperty<ChosenNavItem> chosenNavItem;


    public ViewFactory() {
        this.chosenNavItem = new SimpleObjectProperty<>();
    }

    public ObjectProperty<ChosenNavItem> getChosenNavItem() {
        return chosenNavItem;
    }

    public AnchorPane getSettingsView() {
        if (settingsView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Settings.fxml"));
                settingsView = loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return settingsView;
    }

    public VBox getTasksView() {
        if (tasksView == null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Tasks.fxml"));
                tasksView = loader.load();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return tasksView;
    }

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
        chosenNavItem.setValue(ChosenNavItem.FOCUS);
        createStage(fxmlLoader);
    }

    private void createStage(FXMLLoader fxmlLoader) {
        Scene scene;
        try {
            scene = new Scene(fxmlLoader.load());
        }
         catch (Exception e) {
             throw new RuntimeException(e);
         }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Chronus");
        stage.getIcons().add(new Image(String.valueOf(getClass().getResource("/Images/logo3.png"))));
        stage.show();
        stage.setOnCloseRequest(e -> System.exit(0));
    }


}
