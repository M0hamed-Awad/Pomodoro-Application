package com.example.pomodoro.controllers;

import com.example.pomodoro.Constants;
import com.example.pomodoro.models.PomodoroModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class MainController {

    PomodoroModel pomodoroTimerModel = new PomodoroModel();

    @FXML
    Text timeText;

    @FXML
    Circle pomodoroCircle1;
    @FXML
    Circle pomodoroCircle2;
    @FXML
    Circle pomodoroCircle3;
    @FXML
    Circle pomodoroCircle4;

    @FXML
    Circle backgroundCircle;

    @FXML
    Arc progressIndicatorArc;

    @FXML
    Label titleLabel;

    @FXML
    AnchorPane mainAnchor;

    @FXML
    Button startButton;

    @FXML
    Button pauseButton;
    @FXML
    Button resumeButton;
    @FXML
    Button skipButton;

    @FXML
    Button focusButton;
    @FXML
    Button shortBreakButton;

    @FXML
    Button finishButton;

    @FXML
    public void initialize() {
        backgroundCircle.setFill(Paint.valueOf(Constants.INITIAL_SCENE_COLOR));
        mainAnchor.setBackground(Background.fill(Paint.valueOf(Constants.INITIAL_SCENE_COLOR)));
        pomodoroCircle1.setFill(Paint.valueOf(Constants.DEFAULT_POMODORO_COLOR));
        pomodoroCircle2.setFill(Paint.valueOf(Constants.DEFAULT_POMODORO_COLOR));
        pomodoroCircle3.setFill(Paint.valueOf(Constants.DEFAULT_POMODORO_COLOR));
        pomodoroCircle4.setFill(Paint.valueOf(Constants.DEFAULT_POMODORO_COLOR));
    }

    public Circle getCircle(Integer value) {
        return switch (value) {
            case 1 -> pomodoroCircle1;
            case 2 -> pomodoroCircle2;
            case 3 -> pomodoroCircle3;
            case 4 -> pomodoroCircle4;
            default -> throw new IllegalStateException("Unexpected value: " + value);
        };
    }

    public void setTimeText(String time) {
        timeText.setText(time);
    }

    public void goToInitialScene() {
        pomodoroTimerModel.resetPomodoro();
        buildInitialScene();
    }

    public void buildInitialScene() {
        // Repainting the Scene and the Title label to be the Initial Scene
        paintScene(178, Constants.INITIAL_SCENE_COLOR, Constants.INITIAL_SCENE_TITLE);
        // Repainting the Pomodoro Circles
        resetPomodorosPaint();
        /* Updating the Progress Indicator UI to be Rested to ZERO */
        updateProgressIndicatorUI();
        /* Make only the "Start" button  Visible */
        adjustButtonsVisibilityForInitialScene();
    }

    // Pomodoro Functionalities


    // Start Focus Time and Build the Focus Scene
    public void startFocusing() {
        buildFocusScene();
        pomodoroTimerModel.setPomodoroController(this);
        pomodoroTimerModel.startFocusTime();
    }

    public void buildFocusScene() {
        // Repainting the Scene and the Title label to be the Focus Scene
        paintScene(231, Constants.FOCUS_SCENE_COLOR, Constants.FOCUS_SCENE_TITLE);
        // Paint Current Pomodoro Circle to BLUE to indicate the current Focusing Pomodoro
        paintCurrentPomodoro();
        /* Make only both of the "Pause" and "Skip" buttons Visible */
        adjustButtonsVisibilityForFocusScene();
    }


    // Start Short Break Time and Build the Short Break Scene
    public void startShortBreak() {
        buildShortBreakScene();
        pomodoroTimerModel.setPomodoroController(this);
        pomodoroTimerModel.startShortBreakTime();
    }

    public void buildShortBreakScene() {
        /* Make only both of the "Pause" and "Skip" buttons Visible */
        adjustButtonsVisibilityForShortBreakScene();
        // Repainting the Scene to be the Short Break Scene
        paintScene(148, Constants.SHORT_BREAK_SCENE_COLOR, Constants.SHORT_BREAK_SCENE_TITLE);
    }

    // Start Long Break Time and Build the Long Break Scene
    public void startLongBreak() {
        buildLongBreakScene();
        pomodoroTimerModel.setPomodoroController(this);
        pomodoroTimerModel.startLongBreakTime();
    }

    public void buildLongBreakScene() {
        /* Make only the "Finish" button Visible */
        adjustButtonsVisibilityForLongBreakScene();
        // Repainting the Scene to be the Long Break Scene
        paintScene(119, Constants.LONG_BREAK_SCENE_COLOR, Constants.LONG_BREAK_SCENE_TITLE);
    }


    // Stopping Pomodoro Timer
    public void stopPomodoro() {
        /* Make the "Pause" button Invisible and the "Resume" button Visible */
        pauseButton.setVisible(false);
        resumeButton.setVisible(true);
        pomodoroTimerModel.stopPomodoro();
    }

    // Resuming Pomodoro Timer
    public void resumePomodoro() {
        /* Make the "Resume" button Invisible and the "Pause" button Visible */
        resumeButton.setVisible(false);
        pauseButton.setVisible(true);
        pomodoroTimerModel.resumePomodoro();
    }

    // Skipping Pomodoro Timer
    public void skipPomodoro() {
        /* Make the "Resume" button Invisible and the "Pause" and "Skip" buttons Visible */
        resumeButton.setVisible(false);
        pauseButton.setVisible(true);
        /* If we are in Focus Time, then skip to the Focus Scene, else skip to Short Break Scene */
        if (pomodoroTimerModel.isFocusTime()) {
            skipFocusScene();
        } else {
            skipShortBreakScene();
        }
    }

    // Start Break Time (Either Short OR Long Break)
    public void startBreakTime() {
        if (pomodoroTimerModel.isLongBreakTime()) {
            startLongBreak();   // If number of pomodoros done is 4 (which mean the pomodoro round is DONE), go to Long Break Scene
        } else {
            startShortBreak();  // Else go to Short Break Scene
        }
    }

    // Skip Focus Time to either Short Break Time OR Long Break Time
    public void skipFocusScene() {
        /* Increase number of Pomodoros Completed and Paint it as Skipped in the UI */
        pomodoroTimerModel.increaseNumberOfPomodoros();
        Circle circle = getCircle(pomodoroTimerModel.getNumberOfPomodorosDone());   // Get the Skipped Pomodoro Circle
        paintSkippedPomodoro(circle);

        /* Go to the Appropriate Break Scene After Finishing the Focus Scene */
        startBreakTime();
    }

    // Skip Short Break Time to Focus Time
    public void skipShortBreakScene() {
        startFocusing();
    }


    // Reset Pomodoro
    public void resetPomodoro() {
        goToInitialScene();
    }


    // Changing the UI

    public void resetPomodorosPaint() {
        Circle circle;
        /* Get every circle and Paint them as initial (GRAY) */
        for (int i = 1; i <= 4; i++) {
            circle = getCircle(i);
            circle.setFill(Paint.valueOf(Constants.DEFAULT_POMODORO_COLOR));
        }
    }

    public void updateUI() {
        Platform.runLater(() -> {
            setTimeText(pomodoroTimerModel.getTimeElapsedString());       // Update the Time Text on the UI
            updateProgressIndicatorUI();     // Update the Progress Indicator on the UI
        });
    }

    public void updateProgressIndicatorUI() {
        // Calculating Progress based on the Time Elapsed
        double progress = (pomodoroTimerModel.getTimeElapsed() / pomodoroTimerModel.getCurrentTime()) * 360;
        // Updating the UI Progress Indicator by the value of the new Progress value
        progressIndicatorArc.setLength(progress);
    }

    public void paintScene(Integer titleLayoutX, String backgroundColor, String title) {
        /* Set Title label text */
        titleLabel.setText(title);
        mainAnchor.setBackground(Background.fill(Paint.valueOf(backgroundColor)));    // Set background of the Scene
        backgroundCircle.setFill(Paint.valueOf(backgroundColor));   // Set Circle background to the Scene background so that there will be no difference
        /* Adjust Title label position */
        titleLabel.setLayoutX(titleLayoutX);
        titleLabel.setLayoutY(88);
    }


    /* Painting Pomodoros Circles */

    public void paintDonePomodoro(Circle circle) {
        /* If the Pomodoro is Done then Paint it as "GREEN"*/
        circle.setFill(Paint.valueOf(Constants.DONE_POMODORO_COLOR));
    }

    public void paintSkippedPomodoro(Circle circle) {
        /* If the Pomodoro is Skipped then Paint it as "RED"*/
        circle.setFill(Paint.valueOf(Constants.SKIPPED_POMODORO_COLOR));
    }

    public void paintCurrentPomodoro() {
        // The Current Focusing Pomodoro is the NEXT Pomodoro of the lastly DONE Pomodoro
        int currentFocusingPomodoro = pomodoroTimerModel.getNumberOfPomodorosDone() + 1;
        /* Get the current focusing Pomodoro circle and Paint it as In Progress which mean "BLUE" */
        Circle circle = getCircle(currentFocusingPomodoro);
        circle.setFill(Paint.valueOf(Constants.FOCUSING_POMODORO_COLOR));
    }


    /* Adjusting Buttons Visibility for Different Scenes */

    public void adjustButtonsVisibilityForShortBreakScene() {
        /* Make only both of the "Pause" and "Skip" buttons Visible and the rest of the buttons are Invisible */
        shortBreakButton.setVisible(false);
        resumeButton.setVisible(false);

        pauseButton.setVisible(true);
        skipButton.setVisible(true);
    }

    public void adjustButtonsVisibilityForFocusScene() {
        /* Make only both of the "Pause" and "Skip" buttons Visible and the rest of the buttons are Invisible */
        startButton.setVisible(false);
        focusButton.setVisible(false);

        pauseButton.setVisible(true);
        skipButton.setVisible(true);
    }

    public void adjustButtonsVisibilityForLongBreakScene() {
        /* Make only the "Finish" button Visible and the rest of the buttons are Invisible */
        resumeButton.setVisible(false);
        pauseButton.setVisible(false);
        skipButton.setVisible(false);
        shortBreakButton.setVisible(false);

        finishButton.setVisible(true);
    }

    public void adjustButtonsVisibilityForInitialScene() {
        /* Make only the "Start" button Visible and the rest of the buttons are Invisible */
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        skipButton.setVisible(false);
        focusButton.setVisible(false);
        shortBreakButton.setVisible(false);
        finishButton.setVisible(false);

        startButton.setVisible(true);
    }

    public void adjustButtonsVisibilityForFocusTimeCompleted() {
        /* Make only the "Take a Break" button Visible and the rest of the buttons are Invisible */
        resumeButton.setVisible(false);
        pauseButton.setVisible(false);
        skipButton.setVisible(false);

        shortBreakButton.setVisible(true);
    }

    public void adjustButtonsVisibilityForBreakTimeCompleted() {
        /* Make only the "Focus" button Visible and the rest of the buttons are Invisible */
        resumeButton.setVisible(false);
        pauseButton.setVisible(false);
        skipButton.setVisible(false);

        focusButton.setVisible(true);
    }



    /* Control The App Window */

    /* Close App Window */
    public void closeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    /* Minimize App Window */
    public void minimizeWindow(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}