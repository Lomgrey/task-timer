package com.tasktimer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

public class TaskTimerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        URL resource = getClass().getClassLoader().getResource("scene.fxml");
        Parent root = FXMLLoader.load(Objects.requireNonNull(resource));

        stage.setTitle("Task Timer");
        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();
    }
}
