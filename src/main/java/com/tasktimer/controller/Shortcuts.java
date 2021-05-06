package com.tasktimer.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class Shortcuts {
    static final KeyCombination START_STOP = new KeyCodeCombination(KeyCode.SPACE);
    static final KeyCombination RESET = new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_ANY);

}
