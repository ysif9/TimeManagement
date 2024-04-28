package com.timemanagement.Controllers;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class NavBarController implements Initializable {
    public VBox tasks_btn;
    public VBox flashcards_btn;
    public VBox focus_btn;
    public VBox calendar_btn;
    public VBox settings_btn;
    public HBox navbar;
    public FontAwesomeIconView focus_icon;
    private VBox selected = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        for (Node node : navbar.getChildren()) {
            if (node instanceof VBox) {
                node.setOnMouseEntered(e -> onHover((VBox) node));
                node.setOnMouseExited(e -> onStoppedHover((VBox) node));
                node.setOnMouseClicked(e -> onClicked((VBox) node));
            }
        }
        selected = focus_btn;
    }

    private void resetClick() {
        if (selected != null) {
            selected.setStyle("-fx-border-color: #C9D1D9;");
            for (Node node : selected.getChildren()) {
                if (node instanceof Text) {
                    node.setStyle("-fx-fill: #C9D1D9;");
                }
            }
            selected = null;
        }
    }

    private void onClicked(VBox vBox) {
        if (selected != vBox){
            resetClick();
            selected = vBox;
            vBox.setStyle("-fx-border-color: #1F6FEBFF;");
            for (Node node : vBox.getChildren()) {
                if (node instanceof Text) {
                    node.setStyle("-fx-fill: #1F6FEBFF;");
                }
            }
        }
    }

    private void onHover(VBox vBox) {
        if (selected == vBox) {return;}
        for (Node node : vBox.getChildren()) {
            if (node instanceof Text) {
                node.setStyle("-fx-fill: #1F6FEBFF;");
            }
        }

    }

    private void onStoppedHover(VBox vBox) {
        if (selected == vBox) {return;}
        for (Node node : vBox.getChildren()) {
            if (node instanceof Text) {
                node.setStyle("-fx-fill: #C9D1D9;");
            }
        }
    }
}
