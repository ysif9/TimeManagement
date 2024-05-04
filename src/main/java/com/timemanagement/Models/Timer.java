package com.timemanagement.Models;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.util.Duration;

public class Timer {
    private Thread timerThread;
    private Timeline timeline;
    private final DoubleProperty percentage;
    private final LongProperty time;

    public Timer(long time) {
        this.time = new SimpleLongProperty(this, "time", time);
        this.percentage = new SimpleDoubleProperty(1);
    }

    // Create new thread for timer
    public void startTimer() {
        timerThread = new Thread(this::countdownTimer);
        timerThread.start();
    }


    public void stopTimer() {
        if (timerThread != null) {
            timerThread.interrupt();
            timerThread = null;
            timeline.stop();
            percentage.set(1);
        }

    }

    private void countdownTimer() {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + time.getValue() * 60 * 1000; // Calculate endTime once

        timeline = new Timeline(new KeyFrame(Duration.millis(100), event -> {
            percentage.set(((double) (endTime - System.currentTimeMillis()) / (time.getValue() * 60 * 1000)));
            if (System.currentTimeMillis() >= endTime) {
                stopTimer();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public DoubleProperty percentageProperty() {
        return percentage;
    }

    public LongProperty timeProperty() {
        return time;
    }
}
