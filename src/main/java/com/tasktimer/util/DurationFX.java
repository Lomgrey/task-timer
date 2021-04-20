package com.tasktimer.util;

import com.tasktimer.config.StopwatchConfig;
import javafx.util.Duration;

public class DurationFX extends Duration {
    /**
     * Creates a new Duration with potentially fractional millisecond resolution.
     *
     * @param millis The number of milliseconds
     */
    public DurationFX(double millis) {
        super(millis);
    }

    public String toFormatView(){
        return toFormatView(this);
    }

    public static String toFormatView(Duration time) {
        return String.format(
                StopwatchConfig.OUTPUT_FORMAT,
                (int) time.toHours() % 10,
                (int) time.toMinutes() % 60,
                time.toSeconds() % 60);
    }
}
