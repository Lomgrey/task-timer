package com.tasktimer.util;

import com.tasktimer.config.StopwatchConfig;
import javafx.util.Duration;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss.S");

    public static String toFormatView(Duration time) {
        int hour = (int) time.toHours() % 10;
        int minute = (int) time.toMinutes() % 60;
        int second = (int) time.toSeconds() % 60;
        int nanoOfSecond = (int) (time.toMillis() % 1000 * Math.pow(10, 6));

        LocalTime localTime = LocalTime.of(hour, minute, second, nanoOfSecond);
        return localTime.format(formatter);
    }
}
