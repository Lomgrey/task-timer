package com.tasktimer.repository.local;

import javafx.util.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DayInfo {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter DURATION_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private LocalDate date;
    private List<Duration> cycles;
    private Duration dayDuration;

    public static DayInfo defaultInstance() {
        return DayInfo.builder()
                .date(LocalDate.now())
                .cycles(new ArrayList<>())
                .dayDuration(Duration.ZERO)
                .build();
    }

}
