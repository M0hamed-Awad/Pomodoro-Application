package com.example.pomodoro.models;

import com.example.pomodoro.controllers.MainController;
import javafx.scene.shape.Circle;

import java.util.Timer;
import java.util.TimerTask;

public class PomodoroModel {

    private Timer timer;
    private MainController controller;
    private double timeElapsed;
    private int numberOfPomodorosDone;
    private double focusTime, shortBreakTime, longBreakTime;
    private boolean isFocusTime, isShortBreakTime;

    public PomodoroModel() {
        /* Attributes Initialization */
        timeElapsed = 0;
        numberOfPomodorosDone = 0;
        focusTime = 25 * 60;   // 25 Minutes
        shortBreakTime = 5 * 60;     // 5 Minutes
        longBreakTime = 15 * 60;      // 15 Minutes
        isFocusTime = false;
        isShortBreakTime = false;
    }

    public PomodoroModel(double focusTime) {
        this();
        /* (Focus time must be Greater than 0) AND (Less than 60 minutes) AND (Greater than the Short Break Time) AND (Greater than OR equal to the Long Break Time) */
        if (focusTime > 0 && focusTime <= 60 && focusTime > shortBreakTime && focusTime >= longBreakTime) {
            this.focusTime = focusTime * 60;
        } else {
            throw new RuntimeException("Invalid Value");
        }
    }

    public PomodoroModel(double focusTime, double shortBreakTime) {
        this(focusTime);
        /* (Short Break Time must be Greater than 0) AND (Less than OR equal to 15 minutes) AND (Less than the Focus Time) AND (Less than the Long Break Time) */
        if (shortBreakTime > 0 && shortBreakTime <= 15 && shortBreakTime < focusTime && shortBreakTime < longBreakTime) {
            this.shortBreakTime = shortBreakTime;
        } else {
            throw new RuntimeException("Invalid Value");
        }
    }

    public PomodoroModel(double focusTime, double shortBreakTime, double longBreakTime) {
        this(focusTime, shortBreakTime);
        /* (Long Break Time must be Greater than 0) AND (Less than OR equal to 40 minutes) AND (Less than OR equal to the Focus Time) AND (Greater than the Short Break Time) */
        if (longBreakTime <= 40 && longBreakTime <= focusTime) {
            this.longBreakTime = longBreakTime * 60.0;
        } else {
            throw new RuntimeException("Invalid Value");
        }
    }

    public double getFocusTime() {
        return focusTime;
    }

    public void setFocusTime(double focusTime) {
        /* (Focus time must be Greater than 0) AND (Less than 60 minutes) AND (Greater than the Short Break Time) AND (Greater than OR equal to the Long Break Time) */
        if (focusTime > 0 && focusTime <= 60 && focusTime > shortBreakTime && focusTime >= longBreakTime) {
            this.focusTime = focusTime * 60.0;
        } else {
            throw new RuntimeException("Invalid Value");
        }
    }

    public void setFocusTime(boolean focusTime) {
        isFocusTime = focusTime;
    }

    public double getLongBreakTime() {
        return longBreakTime;
    }

    public void setLongBreakTime(double longBreakTime) {
        /* (Long Break Time must be Greater than 0) AND (Less than OR equal to 40 minutes) AND (Less than OR equal to the Focus Time) AND (Greater than the Short Break Time) */
        if (longBreakTime > 0 && longBreakTime <= 40 * 60.0 && longBreakTime <= focusTime && longBreakTime > shortBreakTime) {
            this.longBreakTime = longBreakTime * 60.0;
        }
    }

    public double getShortBreakTime() {
        return shortBreakTime;
    }

    public void setShortBreakTime(double shortBreakTime) {
        /* (Short Break Time must be Greater than 0) AND (Less than OR equal to 15 minutes) AND (Less than the Focus Time) AND (Less than the Long Break Time) */
        if (shortBreakTime > 0 && shortBreakTime <= 15 * 60.0 && shortBreakTime < focusTime && shortBreakTime < longBreakTime) {
            this.shortBreakTime = shortBreakTime * 60.0;
        }
    }

    public void setShortBreakTime(boolean shortBreakTime) {
        isShortBreakTime = shortBreakTime;
    }

    public boolean isFocusTime() {
        return isFocusTime;
    }

    public boolean isShortBreakTime() {
        return isShortBreakTime;
    }

    public boolean isLongBreakTime() {
        return numberOfPomodorosDone == 4;
    }

    public int getNumberOfPomodorosDone() {
        return numberOfPomodorosDone;
    }

    public double getTimeElapsed() {
        return timeElapsed;
    }

    public String getTimeElapsedString() {
        /* Get Minutes and Seconds Elapsed */
        int minutes = (int) (this.timeElapsed / 60.0);
        int seconds = (int) (this.timeElapsed % 60.0);

        // Returning time in correct format (00:00)
        return String.format("%02d:%02d", minutes, seconds);
    }

    public void increaseNumberOfPomodoros() {
        this.numberOfPomodorosDone = (this.numberOfPomodorosDone + 1) % 5;
    }

    public void setPomodoroController(MainController controller) {
        this.controller = controller;
    }

    public void startFocusTime() {
        /* Switch Pomodoro Mode to Focus Time */
        isShortBreakTime = false;
        isFocusTime = true;

        /* Start Pomodoro Timer */
        stopPomodoro();
        startNewTimer();
    }

    public void startShortBreakTime() {
        /* Switch Pomodoro Mode to Short Break Time */
        isFocusTime = false;
        isShortBreakTime = true;

        /* Start Pomodoro Timer */
        stopPomodoro();
        startNewTimer();
    }

    public void startLongBreakTime() {
        /* Switch Pomodoro Mode to Long Break Time */
        /* Neither Focus Time Nor Short Break Time */
        isFocusTime = false;
        isShortBreakTime = false;

        /* Start Pomodoro Timer */
        stopPomodoro();
        startNewTimer();
    }

    public void stopPomodoro() {
        /* If the timer is NOT Stopped, then Stop it (And keep the Elapsed Time the same) and set it to NULL */
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void resumePomodoro() {
        // If the timer is Stopped, then Resume it (With the same Elapsed Time)
        if (timer == null) {
            timer = new Timer();
            startPomodoroTimer();
        }
    }

    public void resetPomodoro() {
        // Resting Timer by Stopping it
        stopPomodoro();

        // Resting Attributes Values
        isFocusTime = false;
        isShortBreakTime = false;
        numberOfPomodorosDone = 0;
        timeElapsed = 0;
        if (this.controller != null) {
            controller.setTimeText("00:00");
        }
    }

    public void finishPomodoroFocusTime() {
        /* Increasing number of Pomodoros DONE and Repaint it on the screen */
        increaseNumberOfPomodoros();
        Circle circle = controller.getCircle(numberOfPomodorosDone);    // Get the Pomodoro Circle Done
        controller.paintDonePomodoro(circle);

        /* Check whether it is a Short Break Time OR Long Break Time */
        /* If the Number of Pomodoros Done is 4 then we are in the Long Break Time, else we are in the Short Break Time */
        isShortBreakTime = (numberOfPomodorosDone != 4);

        /* Make only the "Take a Break" button  Visible */
        controller.adjustButtonsVisibilityForFocusTimeCompleted();

        /* Show notification of completing the Focus Time */
        popNotification("Take a Break Champ!");
    }

    public void finishBreakTime() {
        /* Check whether it is a Short Break Time OR Long Break Time */
        /* If the Number of Pomodoros Done is 4 then we are in the Long Break Time, else we are in the Short Break Time */
        isShortBreakTime = (numberOfPomodorosDone != 4);
        if (isShortBreakTime) {  // Prepare the Focus Scene
            /* Make only the "Focus" button  Visible */
            controller.adjustButtonsVisibilityForBreakTimeCompleted();
        }
        /* Show notification of completing the Break Time */
        popNotification("Back to Focus!");
    }

    public void handleTimeCompletion() {
        if (timeElapsed == getCurrentTime()) {  // If the time is completed
            /* Stop the timer and Finish the current Pomodoro Mode */
            timer.cancel();
            if (isFocusTime) {
                finishPomodoroFocusTime();      /* If we are in Focus Mode then Stop the timer and Finish the Focus Mode */
            } else {
                finishBreakTime();      /* Else Finish either Short Break Mode or Long Break Mode */
            }
        }
    }

    private void startPomodoroTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeElapsed++;  // Increase "Time Elapsed" by 1 on every second passed
                controller.updateUI();     // Update UI Time Text and Progress Indicator based on the new value of the "Time Elapsed"
                handleTimeCompletion();     // Handling Situation if the Current Time is Completed
            }
        }, 0, 1000);
    }

    public void popNotification(String notificationText) {
        NotificationModel notificationManager =
                new NotificationModel(
                        "Pomodoro App",
                        "/images/Pomodoro_Done.png",
                        notificationText
                );

        notificationManager.popNotification();
    }

    public void startNewTimer() {
        // Reset the Time Elapsed and Start a New Timer and Time
        timeElapsed = 0;
        timer = new Timer();
        startPomodoroTimer();
    }

    public double getCurrentTime() {
        if (isFocusTime) {
            return focusTime;
        } else if (isShortBreakTime) {
            return shortBreakTime;
        } else {
            return longBreakTime;
        }
    }

}
