package com.tasktimer.stopwatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.function.Consumer;

import static com.tasktimer.util.DurationFX.toFormatView;

public class StopwatchFX implements Stopwatch {
    final Label stopwatchLabel;
    final Timeline timeline;

    private Duration time = Duration.ZERO;

    public StopwatchFX(Label stopwatchLabel) {
        timeline = setupTimeline();
        this.stopwatchLabel = stopwatchLabel;
    }

    private Timeline setupTimeline() {
        Timeline tl = new Timeline(new KeyFrame(
                Duration.millis(100),
                event -> {
                    Duration duration = ((KeyFrame) event.getSource()).getTime();
                    time = time.add(duration);
                    stopwatchLabel.setText(toFormatView(time));
                }));
        tl.setCycleCount(Timeline.INDEFINITE);

        return tl;
    }

    @Override
    public Duration start() {
        timeline.play();
        return time;
    }

    @Override
    public Duration start(Duration startPoint) {
        time = startPoint;
        timeline.play();
        return startPoint;
    }

    @Override
    public Duration stop() {
        timeline.stop();
        return time;
    }

    @Override
    public void reset() {
        timeline.stop();
        time = Duration.ZERO;
    }

    @Override
    public void bindTime(Consumer<Duration> bindConsumer) {
        stopwatchLabel.textProperty().addListener((observable, oldValue, newValue) -> {
            bindConsumer.accept(time);
        });
    }
}
