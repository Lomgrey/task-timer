package com.tasktimer.controller;

import com.tasktimer.repository.LapRepository;
import com.tasktimer.repository.factory.LapRepositoryFactory;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class EditHistoryScene {
    public TextField hoursField;
    public TextField minutesField;
    public Button addBtn;

    private final LapRepository<Duration> lapRepository = LapRepositoryFactory.getInstance();

    public void addLap(ActionEvent e) {
        Duration lap = checkInput();
        lapRepository.addLap(lap);
    }

    private Duration checkInput() {
        var hoursVal = parseDouble(hoursField.getText());
        var minutesVal = parseDouble(minutesField.getText());

        var minutesToAdd = minutesVal + hoursVal * 60;

        return Duration.minutes(minutesToAdd);
    }

    double parseDouble(String text) {
        if (text == null || text.isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(text);
    }

    public void changeAddBtnText(KeyEvent event) {
        var hours = hoursField.getText();
        var minutes = minutesField.getText();

        var btnText = "Add " + buildTime(hours, minutes);
        addBtn.setText(btnText);
    }

    /**
     * "", 23 -> 23 minutes
     * 2, "" -> 2 hours
     * 2, 13 -> 2 hours and 23 minutes
     * "", "" -> ""
     */
    private String buildTime(String hours, String minutes) {
        var hourText = hours + " hours";
        var minuteText = minutes + " minutes";

        var hoursIsEmpty = isEmpty(hours);
        var minsIsEmpty = isEmpty(minutes);

        if (hoursIsEmpty && minsIsEmpty) {
            return "";
        }
        if (hoursIsEmpty) {
            return minuteText;
        }
        if (minsIsEmpty)
            return hourText;

        return hourText + " and " + minuteText;
    }

    private boolean isNotEmpty(String time) {
        return !isEmpty(time);
    }

    private boolean isEmpty(String time) {
        // "", "0"
        var trim = time.trim();
        return trim.isEmpty() || trim.equals("0");
    }
}
