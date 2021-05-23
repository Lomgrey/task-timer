package com.tasktimer.repository;

import com.tasktimer.repository.local.DayInfo;

import java.time.LocalDate;

public interface DaysInfoRepository {

    DayInfo getTodayInfo();

    DayInfo getDayInfo(LocalDate forDate);

    void writeToday(DayInfo dayInfo);

    void write(DayInfo dayInfo, LocalDate date);
}
