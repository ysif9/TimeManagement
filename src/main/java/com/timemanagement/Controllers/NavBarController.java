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
    public VBox tasks_btn;
//    public VBox flashcards_btn;
    public VBox focus_btn;
    public VBox calendar_btn;
    public VBox settings_btn;
    public HBox navbar;
    public FontAwesomeIconView focus_icon;
    private VBox selected = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setHoverLogic();

        tasks_btn.setOnMouseClicked(e -> onTasksClicked());
        settings_btn.setOnMouseClicked(e -> onSettingsClicked());
        calendar_btn.setOnMouseClicked(e -> onCalendarClicked());
//        flashcards_btn.setOnMouseClicked(e -> onFlashcardsClicked());
        focus_btn.setOnMouseClicked(e -> onFocusClicked());
        selected = focus_btn;
    }

    private void setHoverLogic() {
        for (Node node : navbar.getChildren()) {
            if (node instanceof VBox) {
                node.setOnMouseEntered(e -> onHover((VBox) node));
                node.setOnMouseExited(e -> onStoppedHover((VBox) node));
            }
        }
    }

    private void onTasksClicked() {
        setSelectedStyle(tasks_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.TASKS);
    }

    private void onSettingsClicked() {
        setSelectedStyle(settings_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.SETTINGS);
    }

    private void onFocusClicked() {
        setSelectedStyle(focus_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.FOCUS);
    }

//    private void onFlashcardsClicked() {
//        setSelectedStyle(flashcards_btn);
//        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.FLASHCARDS);
//    }

    private void onCalendarClicked() {
        setSelectedStyle(calendar_btn);
        Model.getInstance().getViewFactory().getChosenNavItem().setValue(ChosenNavItem.CALENDAR);
    }



    private void setSelectedStyle(VBox vBox) {
        if (selected != vBox){
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

    private void onHover(VBox vBox) {
        if (selected == vBox) {return;}
        for (Node node : vBox.getChildren()) {
            if (node instanceof Text) {
                node.setStyle("-fx-fill: -color-accent-emphasis;");
            }
        }

    }

    private void onStoppedHover(VBox vBox) {
        if (selected == vBox) {return;}
        for (Node node : vBox.getChildren()) {
            if (node instanceof Text) {
                node.setStyle("-fx-fill: -color-fg-muted;");
            }
        }
    }
}
