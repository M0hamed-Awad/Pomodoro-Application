package com.example.pomodoro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class PomodoroApplication extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Loading the "Initial Scene" as the Main
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        /* Creating a Rectangle with Border Radius and Clipping the Scene to it */
        Rectangle rect = new Rectangle(600,785);
        rect.setArcHeight(36);
        rect.setArcWidth(36);
        root.setClip(rect);

        /* Hide default minimize, maximize, and close buttons */
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        scene.setFill(Color.TRANSPARENT);

        // Adding the CSS styles
        String css = Objects.requireNonNull(getClass().getResource("/css/style.css")).toExternalForm();
        scene.getStylesheets().add(css);

        // Adding the APP Logo
        Image logo = new Image(Objects.requireNonNull(getClass().getResource(Constants.LOGO)).toExternalForm());
        primaryStage.getIcons().add(logo);

        // Setting the APP Title
        primaryStage.setTitle("Pomodoro");
        primaryStage.setResizable(false);

        // Display the Scene
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}