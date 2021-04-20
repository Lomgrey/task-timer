package com.tasktimer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import static com.tasktimer.config.AppConfig.*;

public class TaskTimerApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        URL resource = getClass().getClassLoader().getResource(ROOT_FXML);
        Parent root = FXMLLoader.load(Objects.requireNonNull(resource));

        stage.setTitle(APP_NAME);
        Scene scene = new Scene(root, INIT_ROOT_WIDTH, INIT_ROOT_HEIGHT);
        stage.setScene(scene);
        stage.show();

//        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
//            System.out.println("width" + newVal);
//        });
//
//        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
//            System.out.println("height" + newVal);
//        });
    }
}
