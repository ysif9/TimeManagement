package com.timemanagement.Controllers;


import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {
    public ImageView image_view;
    public HBox dashboard_btn;
    public HBox flashcards_btn;
    public HBox tasks_btn;
    public HBox calendar_btn;
    public HBox settings_btn;
    public VBox menu_container;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for(Node node : menu_container.getChildren()){
            if(node instanceof HBox hbox){
                hbox.hoverProperty().addListener( (observable, oldValue, newValue) -> onHover(oldValue, newValue));
                hbox.setOnMouseClicked(mouseEvent -> onClicked(hbox));
            }
        }

    }

    private void resetOthers() {
        for (Node node : menu_container.getChildren()) {
            if (node instanceof HBox hBox) {
                Background background = hBox.getBackground();
                if (background != null && !background.getFills().isEmpty()) {
                    BackgroundFill fill = background.getFills().getFirst();
                    if (fill.getFill().equals(Color.rgb(228, 229, 228))) {
                        hBox.setStyle("-fx-background-color: white;");
                        for (Node child : hBox.getChildren()) {
                            if (child instanceof Line) {
                                child.setVisible(false);
                            }
                        }
                    }
                }
            }
        }
    }

    private void onClicked(HBox btn) {
        resetOthers();
        btn.setStyle("-fx-background-color:  #e4e5e4");
       for (Node node : btn.getChildren()) {
           if (node instanceof Line) {
               node.setVisible(true);
           }
       }
    }


    private void onHover(Boolean oldValue, Boolean newValue) {
        if (newValue) {
            menu_container.setCursor(Cursor.HAND);
        } else if (oldValue) {
            menu_container.setCursor(Cursor.DEFAULT);

        }
    }
}
