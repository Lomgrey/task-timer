package com.tasktimer.controller;

import com.tasktimer.stopwatch.StopwatchFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class MainController {
    @FXML
    public Label timerLabel;
    @FXML
    public Button controlBtn;
    @FXML
    public Button resetBtn;

    private boolean timerRun = false;

    StopwatchFX stopwatch;

    public void initialize() {
        stopwatch = new StopwatchFX(timerLabel);
    }


    public void stopwatchControl(MouseEvent event) {
        if (timerRun) {
            controlBtn.setText("Start");
            stopwatch.stop();

        } else {
            controlBtn.setText("Stop");
            stopwatch.start();
        }
        timerRun = !timerRun;
    }

    public void reset(MouseEvent event) {
        stopwatch.reset();
        timerLabel.setText("0:00:00");
    }
}
