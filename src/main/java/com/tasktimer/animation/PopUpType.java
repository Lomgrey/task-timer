package com.tasktimer.animation;

import javafx.scene.paint.Color;

public enum PopUpType {
    INFO(Color.GREEN), ERROR(Color.RED);
    final Color color;

    PopUpType(Color color) {
        this.color = color;
    }
}
