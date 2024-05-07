/**
 * The Timer class provides functionality to manage countdown timers in JavaFX applications.
 * It allows the creation of timers with specified durations in minutes, enabling users to start
 * and stop timers as needed. The class offers properties to track the progress of the timer,
 * the duration of the timer, and a flag indicating whether the timer has completed.
 */

 package com.timemanagement.Models;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.*;
import javafx.util.Duration;

public class Timer {
    // Properties
    private final LongProperty time;
    private final DoubleProperty percentage;
    private final BooleanProperty timerDoneFlag;

    // Internal variables
    private Thread timerThread;
    private Timeline timeline;


    public Timer(long time) {
        this.time = new SimpleLongProperty(this, "time", time);
        this.percentage = new SimpleDoubleProperty(1);
        this.timerDoneFlag = new SimpleBooleanProperty(false);
    }


    public void startTimer() {
        // Reset flags and start the timer thread
        timerDoneFlag.set(false);
        timerThread = new Thread(this::countdownTimer);
        timerThread.start();
    }

    public void stopTimer() {
        if (timerThread != null) {
            // Interrupt the timer thread and reset properties
            timerThread.interrupt();
            timerThread = null;
            timeline.stop();
            percentage.set(1);
        }
    }

    // Private method to handle countdown logic
    private void countdownTimer() {
        // Calculate end time
        long startTime = System.currentTimeMillis();
        long endTime = startTime + time.getValue() * 60 * 1000;

        // Create timeline for updating timer progress
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
