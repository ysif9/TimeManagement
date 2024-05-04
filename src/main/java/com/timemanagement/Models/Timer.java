package com.timemanagement.Models;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.util.Duration;

public class Timer {
    private Thread timerThread;
    private Timeline timeline;
    private final DoubleProperty percentage;
    private final LongProperty time;
    private final BooleanProperty timerDoneFlag;

    public Timer(long time) {
        this.time = new SimpleLongProperty(this, "time", time);
        this.percentage = new SimpleDoubleProperty(1);
        this.timerDoneFlag = new SimpleBooleanProperty(false);
    }

    // Create new thread for timer
    public void startTimer() {
        timerDoneFlag.set(false);
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

        timeline = new Timeline(new KeyFrame(Duration.millis(50), event -> {
            percentage.set(((double) (endTime - System.currentTimeMillis()) / (time.getValue() * 60 * 1000)));
            if (System.currentTimeMillis() >= endTime) {
                timerDoneFlag.set(true);
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

    public BooleanProperty timerDoneFlagProperty() {
        return timerDoneFlag;
    }
}
