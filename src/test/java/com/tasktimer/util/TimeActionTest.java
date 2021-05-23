package com.tasktimer.util;

import javafx.util.Duration;
import org.junit.jupiter.api.Test;

import static com.tasktimer.util.TimeAction.MINUS;
import static com.tasktimer.util.TimeAction.PLUS;
import static javafx.util.Duration.minutes;
import static org.junit.jupiter.api.Assertions.*;

class TimeActionTest {

    @Test
    void testPlusAction() {
        var actual = PLUS.apply(minutes(1), minutes(2));

        assertEquals(minutes(3), actual);
    }

    @Test
    void testMinusAction() {
        var actual = MINUS.apply(minutes(3), minutes(1));

        assertEquals(minutes(2), actual);
    }
}