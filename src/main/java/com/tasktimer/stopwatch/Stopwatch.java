package com.tasktimer.stopwatch;

import javafx.util.Duration;

import java.util.function.Consumer;

public interface Stopwatch {

    Duration start();

    Duration start(Duration startPoint);

    Duration stop();

    void reset();

    void bindTime(Consumer<Duration> bindConsumer);
}
