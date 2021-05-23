package com.tasktimer;

import com.tasktimer.controller.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.tasktimer.config.AppConfig.*;
import static java.util.Objects.requireNonNull;

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
        addIcon(stage);

        Scene scene = new Scene(root, INIT_ROOT_WIDTH, INIT_ROOT_HEIGHT);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("default.css").toExternalForm());
        stage.setScene(scene);

        MainViewController controller = loader.getController();
        controller.setStage(stage);

        stage.show();
    }

    private void addIcon(Stage stage) {
        var iconResourceAsStream = getClass().getClassLoader().getResourceAsStream("app-icon.png");
        stage.getIcons().add(new Image(requireNonNull(iconResourceAsStream)));
    }
}
