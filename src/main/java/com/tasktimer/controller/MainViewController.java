package com.tasktimer.controller;

import com.tasktimer.repository.InMemoryLapsRepository;
import com.tasktimer.repository.InMemoryTimePointRepository;
import com.tasktimer.repository.LapsRepository;
import com.tasktimer.repository.TimePointRepository;
import com.tasktimer.stopwatch.StopwatchFX;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Duration;
import one.util.streamex.EntryStream;

import java.util.List;

import static com.tasktimer.util.DurationFX.toFormatView;
import static java.lang.String.format;

public class MainViewController {

    public Label timerLabel;
    public Button controlBtn;
    public TextArea lapsTextArea;

    public AnchorPane anchorPane;
    public HBox hbox;
    public Label currentLapLabel;

    private boolean timerRun = false;
    private Duration lastLap = Duration.ZERO;

    private StopwatchFX stopwatch;
    private TimePointRepository<Duration> timePointRepository;
    private LapsRepository<Duration> lapsRepository;

    public void initialize() {
        stopwatch = new StopwatchFX(timerLabel);
        timePointRepository = new InMemoryTimePointRepository();
        lapsRepository = new InMemoryLapsRepository<>();

        registryTimePointsAreaUpdate();
        registryResize();

        stopwatch.bindTime(currentDuration -> {
            currentLapLabel.setText(toFormatView(currentDuration.subtract(lastLap)));
        });
    }

    private void registryTimePointsAreaUpdate() {
        lapsRepository.addListener((fullList, newVal) -> {
            var formattedPoints = EntryStream.of(List.copyOf(fullList))
                    .mapKeyValue((i, d) -> format("Lap %d:\t%s", i+1, toFormatView(d)))
                    .toList();
            lapsTextArea.setText(String.join("\n", formattedPoints) + "\n"); // todo: fix it for scroll down
            lapsTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void registryResize() {
        anchorPane.widthProperty().addListener((obs, oldVal, newVal) ->
                hbox.setPrefWidth(newVal.doubleValue()));
        anchorPane.heightProperty().addListener((obs, oldVal, newVal) ->
                hbox.setPrefHeight(newVal.doubleValue()));
    }

    public void stopwatchControl(MouseEvent event) {
        if (timerRun)
            stopStopwatch();
        else
            startStopwatch();
        timerRun = !timerRun;
    }

    private void startStopwatch() {
        controlBtn.setText("Stop");
        stopwatch.start();
    }

    private void stopStopwatch() {
        controlBtn.setText("Start");
        var stopPoint = stopwatch.stop();
        Duration lapDuration = stopPoint.subtract(lastLap);
        lastLap = stopPoint;

        timePointRepository.addPoint(stopPoint);
        lapsRepository.addLap(lapDuration);
    }

    public void resetConfirmation() {
        String contentText = "Are you sure that you want to reset? All data fot today will be lost";
        ButtonType resetBtn = new ButtonType("Reset", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.OK_DONE);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, contentText, cancelBtn, resetBtn);
        alert.setTitle("Reset");
        alert.setHeaderText("");
        alert.showAndWait()
                .filter(answer -> answer.equals(resetBtn))
                .ifPresent(ignoreValue -> reset());
    }

    private void reset() {
        lastLap = Duration.ZERO;
        timePointRepository.resetToday();
        lapsRepository.resetToday();
        stopwatch.reset();
        timerLabel.setText(toFormatView(Duration.ZERO));
    }

    public void handleKeyInput(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.R) {
            resetConfirmation();
        }
    }
}
