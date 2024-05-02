package com.timemanagement.Models;


import com.timemanagement.Theme;
import com.timemanagement.Views.ViewFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Model {
    private final ViewFactory viewFactory;
    private static Model model;
    private ObjectProperty<Theme> theme;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.theme = new SimpleObjectProperty<>(this, "theme", Theme.DARK);
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public ObjectProperty<Theme> getThemeProperty() {
        return theme;
    }


}
