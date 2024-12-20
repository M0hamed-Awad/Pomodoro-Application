module com.example.pomodoro {
    requires javafx.fxml;
    requires java.sql;
    requires java.management;
    requires org.controlsfx.controls;
    requires java.desktop;
    requires javafx.media;

    opens com.example.pomodoro to javafx.fxml;
    exports com.example.pomodoro;
    exports com.example.pomodoro.controllers;
    opens com.example.pomodoro.controllers to javafx.fxml;
    exports com.example.pomodoro.models;
    opens com.example.pomodoro.models to javafx.fxml;

}