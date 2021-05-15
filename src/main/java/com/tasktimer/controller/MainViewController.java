package com.tasktimer.controller;

import com.tasktimer.repository.CycleRepository;
import com.tasktimer.repository.CycleRepositoryFactory;
import com.tasktimer.repository.TimePointRepository;
import com.tasktimer.repository.TimeRepositoryFactory;
import com.tasktimer.stopwatch.StopwatchFX;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import one.util.streamex.EntryStream;

import java.util.List;

import static com.tasktimer.util.DurationFX.toFormatView;
import static java.lang.String.format;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.SPACE;

public class MainViewController {
    private Stage stage;

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
    private CycleRepository<Duration> cycleRepository;

    public void initialize() {
        stopwatch = new StopwatchFX(timerLabel);
        timePointRepository = TimeRepositoryFactory.getInstance();
        cycleRepository = CycleRepositoryFactory.getInstance();

        registryTimePointsAreaUpdate();
        registryResize();

        stopwatch.bindTime(currentDuration -> {
            currentLapLabel.setText(toFormatView(currentDuration.subtract(lastLap)));
        });
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        registryShortcuts();
    }

    private void registryTimePointsAreaUpdate() {
        cycleRepository.addListener((fullList, newVal) -> {
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

    private void registryShortcuts() {
        Scene scene = stage.getScene();
        scene.addEventFilter(KeyEvent.KEY_PRESSED, ke -> {
            if (Shortcuts.START_STOP.match(ke)) {
                switchStopwatch();
            } else if (Shortcuts.RESET.match(ke)) {
                resetConfirmation();
            }
        });
    }

    public void stopwatchControl(Event event) {
        switchStopwatch();
    }

    private void switchStopwatch() {
        if (timerRun)
            stopStopwatch();
        else
            startStopwatch();
        timerRun = !timerRun;
    }

    public void stopwatchControlKeyEvent(KeyEvent event) {
        var key = event.getCode();
        if (key == SPACE || key == ENTER) {
            stopwatchControl(event);
        }
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
        cycleRepository.addLap(lapDuration);
    }

    public void resetConfirmation() {
        String contentText = "Are you sure that you want to reset? All the data for today will be lost";
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
        cycleRepository.resetToday();
        stopwatch.reset();
        timerLabel.setText(toFormatView(Duration.ZERO));
    }

    public void handleKeyInput(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() && keyEvent.getCode() == KeyCode.R) {
            resetConfirmation();
        }
    }
}
