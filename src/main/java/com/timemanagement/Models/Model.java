package com.timemanagement.Models;


import com.timemanagement.Theme;
import com.timemanagement.Views.ViewFactory;
import javafx.beans.property.*;

public class Model {
    private final ViewFactory viewFactory;
    private static Model model;
    private Task selectedTask;
    private final DoubleProperty focusSliderValue;
    private final DoubleProperty breakSliderValue;
    private final ObjectProperty<Theme> theme;

    private Model() {
        this.viewFactory = new ViewFactory();
        this.theme = new SimpleObjectProperty<>(this, "theme", Theme.DARK);
        this.focusSliderValue = new SimpleDoubleProperty();
        this.breakSliderValue = new SimpleDoubleProperty();
    }

    public static synchronized Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }


    public Task getSelectedTask() {
        return selectedTask;
    }
    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

   public DoubleProperty getFocusSlider() {
        return focusSliderValue;
   }

   public DoubleProperty getBreakSlider() {
        return breakSliderValue;
   }

    public ViewFactory getViewFactory() {
        return viewFactory;
    }

    public ObjectProperty<Theme> getThemeProperty() {
        return theme;
    }


}
