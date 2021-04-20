package com.tasktimer.controller;

import com.tasktimer.repository.InMemoryTimePointMachine;
import com.tasktimer.repository.TimePointMachine;
import com.tasktimer.stopwatch.StopwatchFX;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import one.util.streamex.EntryStream;

import java.util.List;

import static com.tasktimer.util.DurationFX.toFormatView;
import static java.lang.String.format;

public class MainController {
    @FXML
    public Label timerLabel;
    @FXML
    public Button controlBtn;
    @FXML
    public Button resetBtn;

    public TextArea timePointArea;

    public AnchorPane anchorPane;
    public HBox hbox;

    private boolean timerRun = false;

    private StopwatchFX stopwatch;
    private TimePointMachine<Duration> timePointMachine;

    public void initialize() {
        stopwatch = new StopwatchFX(timerLabel);
        timePointMachine = new InMemoryTimePointMachine();

        registryTimePointsAreaUpdate();
        registryResize();
    }

    private void registryTimePointsAreaUpdate() {
        timePointMachine.addListener((fullList, newVal) -> {
            var formattedPoints = EntryStream.of(List.copyOf(fullList))
                    .mapKeyValue((i, d) -> format("Lap %d:\t%s", i+1, toFormatView(d)))
                    .toList();
            timePointArea.setText(String.join("\n", formattedPoints));
        });
    }

    private void registryResize() {
        anchorPane.widthProperty().addListener((obs, oldVal, newVal) ->
                hbox.setPrefWidth(newVal.doubleValue()));
        anchorPane.heightProperty().addListener((obs, oldVal, newVal) ->
                hbox.setPrefHeight(newVal.doubleValue()));
    }

    public void stopwatchControl(MouseEvent event) {
        if (timerRun) {
            controlBtn.setText("Start");
            Duration stopPoint = stopwatch.stop();
            timePointMachine.addPoint(stopPoint);
        } else {
            controlBtn.setText("Stop");
            stopwatch.start();
        }
        timerRun = !timerRun;
    }

    public void reset(MouseEvent event) {
        timePointMachine.resetToday();
        stopwatch.reset();
        timerLabel.setText(toFormatView(Duration.ZERO));
    }
}
