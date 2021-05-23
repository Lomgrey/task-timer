package com.tasktimer.util;

import javafx.util.Duration;

import java.util.function.BiFunction;

public enum TimeAction {
    PLUS(Duration::add),
    MINUS(Duration::subtract);

    private final BiFunction<Duration, Duration, Duration> function;

    TimeAction(BiFunction<Duration, Duration, Duration> function) {
        this.function = function;
    }

    public Duration apply(Duration time1, Duration time2) {
        return function.apply(time1, time2);
    }
}
