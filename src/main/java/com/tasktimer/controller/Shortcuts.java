package com.tasktimer.controller;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class Shortcuts {
    static final KeyCombination START_STOP = new KeyCodeCombination(KeyCode.SPACE);
    static final KeyCombination RESET = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_ANY);

    static final KeyCombination EDIT = new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_ANY);

}
