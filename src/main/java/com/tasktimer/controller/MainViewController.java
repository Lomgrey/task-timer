package com.tasktimer.controller;

import com.tasktimer.repository.CycleRepository;
import com.tasktimer.repository.DaysInfoRepository;
import com.tasktimer.repository.TimePointRepository;
import com.tasktimer.repository.factory.CycleRepositoryFactory;
import com.tasktimer.repository.factory.DaysInfoRepositoryFactory;
import com.tasktimer.repository.factory.TimeRepositoryFactory;
import com.tasktimer.repository.local.DayInfo;
import com.tasktimer.stopwatch.StopwatchFX;
import com.tasktimer.util.DurationFX;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import one.util.streamex.EntryStream;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.google.common.collect.Iterables.getLast;
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
    private Duration lastLapPoint;

    private StopwatchFX stopwatch;

    private TimePointRepository<Duration> timePointRepository;
    private CycleRepository<Duration> cycleRepository;
    private DaysInfoRepository daysInfoRepository;

    public void initialize() {
        stopwatch = new StopwatchFX(timerLabel);
        timePointRepository = TimeRepositoryFactory.getInstance();
        cycleRepository = CycleRepositoryFactory.getInstance();
        daysInfoRepository = DaysInfoRepositoryFactory.getInstance();

        loadTodayInfoAndUpdateView();

        registryTimePointsAreaUpdate();
        registryResize();
        registryCurrentLapUpdate();
    }

    private void loadTodayInfoAndUpdateView() {
        var todayInfo = daysInfoRepository.getTodayInfo();
        stopwatch.setTime(todayInfo.getDayDuration());
        lastLapPoint = todayInfo.getDayDuration();
        updateView(todayInfo);
    }

    private void updateView(DayInfo dayInfo) {
        updateCyclesView(dayInfo.getCycles());
        timerLabel.setText(DurationFX.toFormatView(dayInfo.getDayDuration()));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        registryShortcuts();
    }

    private void registryTimePointsAreaUpdate() {
        cycleRepository.addListener((fullList, newVal) -> {
            updateCyclesView(fullList);
        });
    }

    private void updateCyclesView(Collection<? extends Duration> fullList) {
        if (fullList.isEmpty()) return;
        var formattedPoints = EntryStream.of(List.copyOf(fullList))
                .mapKeyValue((i, d) -> format("Lap %d:\t%s", i + 1, toFormatView(d)))
                .toList();
        lapsTextArea.setText(String.join("\n", formattedPoints) + "\n"); // todo: fix it for scroll down
        lapsTextArea.setScrollTop(Double.MAX_VALUE);
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

    private void registryCurrentLapUpdate() {
        stopwatch.bindTime(currentDuration -> {
            currentLapLabel.setText(toFormatView(currentDuration.subtract(lastLapPoint)));
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
        Duration lapDuration = stopPoint.subtract(lastLapPoint);
        lastLapPoint = stopPoint;

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
        lastLapPoint = Duration.ZERO;
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

    /** COPY TO CLIPBOARD */

    public void copyToClipboard(MouseEvent event) {
        var source = event.getSource();
        checkAndCast(source)
                .ifPresent(labelToCopy -> {
                    copyLabelDataToClipboard(labelToCopy);
                    showPopUp("Copied ✓", Color.GREEN);
                });
    }

    private void showPopUp(String text, Color color) {
        var popUp = new Label(text);
        popUp.setFont(Font.font(16));
        popUp.setTextFill(color);

        var width = stage.getScene().getWidth();
        popUp.setTranslateX(width - (width - 10));
        var height = stage.getScene().getHeight();
        popUp.setTranslateY(height - 30);

        int fat = 2_000; // 5 sec
        int oneFrameTime = 50;
        var tl = new Timeline(new KeyFrame(
                Duration.millis(oneFrameTime),
                event -> { // todo: add animation (for opacity of position)
//                    var time = ((KeyFrame) event.getSource()).getTime();
//                    double opacity = calcOpacity(fat, time.toMillis());
//                    popUp.setTextFill(Color.color(
//                            color.getRed(),
//                            color.getGreen(),
//                            color.getBlue(),
//                            opacity));
                }
        ));
        tl.setOnFinished(event -> anchorPane.getChildren().remove(popUp));
        tl.setCycleCount(fat / oneFrameTime);

        tl.play();
        anchorPane.getChildren().add(popUp);

    }

    /**
     *
     * @param fat - full animation time
     * @param millis - current state
     * @return - opacity for that time
     */
    private double calcOpacity(double fat, double millis) {
        var thirdPart = fat / 3;
        if (millis < thirdPart) {
            // increase opacity
            return 0.5;
        } else if (millis > thirdPart && millis < thirdPart * 2) {
            // static period
            return 1;
        } else {
            // reduce opacity
            return 0.5;
        }
    }

    private Optional<Label> checkAndCast(Object source) {
        if (source instanceof Label) {
            return Optional.of((Label) source);
        }
        return Optional.empty();
    }

    private void copyLabelDataToClipboard(Label labelToCopy) {
        var clipboard = Clipboard.getSystemClipboard();
        var content = new ClipboardContent();
        content.putString(labelToCopy.getText());
        clipboard.setContent(content);
    }

    public void changeMouseCursorToHand(MouseEvent event) {
        var source = event.getSource();
        checkAndCast(source)
                .ifPresent(label -> label.setTextFill(Color.gray(0.5)));
        stage.getScene().setCursor(Cursor.HAND);
    }

    public void changeMouseCursorToDefault(MouseEvent event) {
        var source = event.getSource();
        checkAndCast(source)
                .ifPresent(label -> label.setTextFill(Color.gray(0)));
        stage.getScene().setCursor(Cursor.DEFAULT);
    }
}
