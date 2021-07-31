package com.tasktimer.controller.edit;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.SneakyThrows;

import java.net.URL;

import static com.tasktimer.config.AppConfig.EDIT_FXML;

public class EditSceneLoader {

    @SneakyThrows
    public static void loadAndShow() {
        var editStage = new Stage();
        URL resource = EditSceneLoader.class.getClassLoader().getResource(EDIT_FXML);
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        editStage.setScene(scene);

        editStage.show();
    }

}
