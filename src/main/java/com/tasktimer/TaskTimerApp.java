package com.tasktimer;

import com.tasktimer.controller.MainViewController;
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
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        stage.setTitle(APP_NAME);
        stage.setMinHeight(MIN_ROOT_HEIGHT);
        stage.setMinWidth(MIN_ROOT_WIDTH);

        Scene scene = new Scene(root, INIT_ROOT_WIDTH, INIT_ROOT_HEIGHT);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("default.css").toExternalForm());
        stage.setScene(scene);

        MainViewController controller = loader.getController();
        controller.setStage(stage);

        stage.show();
    }
}
