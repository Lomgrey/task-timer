package com.tasktimer.stopwatch;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class StopwatchFX implements Stopwatch {
    final Label stopwatchLabel;
    final Timeline timeline;

    Duration time = Duration.ZERO;

    public StopwatchFX(Label stopwatchLabel) {
        timeline = setupTimeLine();
        this.stopwatchLabel = stopwatchLabel;
    }

    private Timeline setupTimeLine() {
        Timeline tl = new Timeline(new KeyFrame(
                Duration.millis(100),
                event -> {
                    Duration duration = ((KeyFrame) event.getSource()).getTime();
                    time = time.add(duration);

                    stopwatchLabel.setText(formatDuration(time));

                }));
        tl.setCycleCount(Timeline.INDEFINITE);

        return tl;
    }

    private String formatDuration(Duration duration) {
        return String.format(
                "%d:%02d:%02d",
                (int) duration.toHours() % 10,
                (int) duration.toMinutes() % 60,
                (int) duration.toSeconds() % 60);
    }

    @Override
    public void start() {
        timeline.play();
    }

    public void start(int startPointInSeconds) {
        time = Duration.seconds(startPointInSeconds);
        timeline.play();
    }

    @Override
    public void stop() {
        timeline.stop();
    }

    @Override
    public void reset() {
        timeline.stop();
        time = Duration.ZERO;
    }
}
