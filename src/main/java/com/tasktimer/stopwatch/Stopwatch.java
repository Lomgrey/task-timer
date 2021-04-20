package com.tasktimer.stopwatch;

import javafx.util.Duration;

public interface Stopwatch {

    Duration start();

    Duration start(Duration startPoint);

    Duration stop();

    void reset();

}
