package com.example.pomodoro.models;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

import java.util.Objects;

public class NotificationModel {
    private String notificationSoundPath;
    private String notificationTitle;
    private String notificationMessage;
    private String notificationImagePath;
    private Notifications notification = Notifications.create();

    public NotificationModel() {
        notificationSoundPath = "/sounds/notification_sound.mp3";
        notificationImagePath = "/images/Notification_Image.png";
        notificationTitle = "Your Notification";
        notificationMessage = "New Notification";
    }

    public NotificationModel(String notificationTitle) {
        notificationSoundPath = "/sounds/notification_sound.mp3";
        notificationImagePath = "/images/Notification_Image.png";
        notificationMessage = "New Notification";
        this.notificationTitle = notificationTitle;
    }

    public NotificationModel(String notificationTitle, String notificationImage) {
        this(notificationTitle);
        this.notificationImagePath = notificationImage;
    }

    public NotificationModel(String notificationTitle, String notificationImage, String notificationMessage) {
        this(notificationTitle, notificationImage);
        this.notificationMessage = notificationMessage;
    }

    public NotificationModel(String notificationTitle, String notificationImage, String notificationMessage, String notificationSound) {
        this(notificationTitle, notificationImage, notificationMessage);
        this.notificationSoundPath = notificationSound;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public String getNotificationSoundPath() {
        return notificationSoundPath;
    }

    public void setNotificationSoundPath(String notificationSoundPath) {
        this.notificationSoundPath = notificationSoundPath;
    }

    public String getNotificationImagePath() {
        return notificationImagePath;
    }

    public void setNotificationImagePath(String notificationImagePath) {
        this.notificationImagePath = notificationImagePath;
    }

    private Notifications createNewNotification() {
        notification.hideAfter(Duration.seconds(5));       // Hide notification after 5 seconds
        Image notificationImage = new Image(String.valueOf(getClass().getResource(notificationImagePath)));
        notification.graphic(new ImageView(notificationImage));     // Set an Image in the Notification
        notification.title(notificationTitle);
        return notification;
    }

    public void popNotification() {
        Platform.runLater(() -> {
            playNotificationSound();
            displayNotification();
        });
    }

    public void displayNotification() {
        notification = createNewNotification();   // Creating a new Notification to Display
        notification.text(notificationMessage);    // Setting the text of the notification
        notification.show();
    }

    public void playNotificationSound() {
        Media media = new Media(Objects.requireNonNull(getClass().getResource(notificationSoundPath)).toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media); // Preparing thw Sound to be Played
        mediaPlayer.play();
    }
}
