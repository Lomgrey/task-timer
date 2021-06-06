package com.tasktimer.animation;

import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class PopUpAnimation {

    private PopUp popUp;
    private Pane pane;

    public PopUpAnimation animate(PopUp popUp) {
        this.popUp = popUp;
        return this;
    }

    public PopUpAnimation on(Pane pane) {
        this.pane = pane;
        return this;
    }

    public void start() {
        Label label = buildLabel();
        pane.getChildren().add(label);

        var duration = Duration.millis(900);
        var transition = buildTransition(label, duration);
        transition.play();
    }

    private TranslateTransition buildTransition(Label label, Duration duration) {
        var transition = new TranslateTransition(duration, label);
        transition.setOnFinished(e -> pane.getChildren().remove(label));
        transition.setByY(25);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        transition.setInterpolator(Interpolator.EASE_OUT);
        return transition;
    }

    private Label buildLabel() {
        var label = new Label(popUp.text);
        label.setTextFill(popUp.popUpType.color);
        label.setFont(Font.font(16));

        label.setTranslateX(pane.getWidth() / 2);
        label.setTranslateY(-15);

        return label;
    }

}

