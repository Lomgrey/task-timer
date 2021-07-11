package com.tasktimer.controller;

import com.tasktimer.animation.PopUp;
import com.tasktimer.animation.PopUpAnimation;
import com.tasktimer.animation.PopUpType;
import com.tasktimer.feature.edit.EditAction;
import com.tasktimer.feature.edit.TimeHistoryEditor;
import com.tasktimer.feature.edit.TimeHistoryEditorFactory;
import com.tasktimer.repository.LapRepository;
import com.tasktimer.repository.DaysInfoRepository;
import com.tasktimer.repository.TimePointRepository;
import com.tasktimer.repository.factory.LapRepositoryFactory;
import com.tasktimer.repository.factory.DaysInfoRepositoryFactory;
import com.tasktimer.repository.factory.TimeRepositoryFactory;
import com.tasktimer.repository.local.DayInfo;
import com.tasktimer.stopwatch.StopwatchFX;
import com.tasktimer.util.DurationFX;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.SneakyThrows;
import one.util.streamex.EntryStream;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.tasktimer.config.AppConfig.*;
import static com.tasktimer.config.AppConfig.INIT_ROOT_HEIGHT;
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
    private LapRepository<Duration> lapRepository;
    private DaysInfoRepository daysInfoRepository;

    public void initialize() {
        stopwatch = new StopwatchFX(timerLabel);
        timePointRepository = TimeRepositoryFactory.getInstance();
        lapRepository = LapRepositoryFactory.getInstance();
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
        lapRepository.addListener((fullList, newVal) -> {
            var todayInfo = daysInfoRepository.getTodayInfo();
            todayInfo.setCycles(new ArrayList<>(fullList));

            updateView(todayInfo);
        });
    }

    private void updateCyclesView(Collection<? extends Duration> fullList) {
        if (fullList.isEmpty()) return;
        var formattedPoints = EntryStream.of(List.copyOf(fullList))
                .mapKeyValue((i, d) -> toFormatView(d))
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
            } else if (Shortcuts.EDIT.match(ke)) {
                loadEditScene();
            }
        });
    }

    @SneakyThrows
    private void loadEditScene() {
        // todo: Move to loader
        var editStage = new Stage();
        URL resource = getClass().getClassLoader().getResource(EDIT_FXML);
        FXMLLoader loader = new FXMLLoader(resource);
        Parent root = loader.load();

        Scene scene = new Scene(root);
        editStage.setScene(scene);

        editStage.show();

    }

    private void registryCurrentLapUpdate() {
        stopwatch.bindTime(currentDuration ->
                currentLapLabel.setText(toFormatView(currentDuration.subtract(lastLapPoint)))
        );
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
        var stopPoint = stopwatch.stop();

        controlBtn.setText("Start");

        Duration lapDuration = stopPoint.subtract(lastLapPoint);
        lastLapPoint = stopPoint;

        timePointRepository.addPoint(stopPoint);
        lapRepository.addLap(lapDuration);
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
        lapRepository.resetToday();
        stopwatch.reset();
        timerLabel.setText(toFormatView(Duration.ZERO));
    }

    /** COPY TO CLIPBOARD */

    public void copyToClipboardEvent(MouseEvent event) {
        var source = event.getSource();
        checkAndCast(source)
                .ifPresent(labelToCopy -> {
                    copyLabelDataToClipboard(labelToCopy);
                    var popUp = new PopUp()
                            .setText("Copied ✓")
                            .setPopUpType(PopUpType.INFO);
                    showPopUp(popUp);
                });
    }

    private void showPopUp(PopUp popUp) {
        new PopUpAnimation()
                .animate(popUp)
                .on(anchorPane)
                .start();
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

    /** EDIT TIME LAPS */

    private TimeHistoryEditor editor = TimeHistoryEditorFactory.getInstance();

    public void addNewLap(Duration item) {
        editor.edit(EditAction.ADD, item);
    }

    public void deleteLap(Duration item) {
        editor.edit(EditAction.REMOVE, item);
    }

}
