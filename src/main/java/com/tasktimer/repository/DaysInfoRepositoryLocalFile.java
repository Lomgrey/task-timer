package com.tasktimer.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.tasktimer.repository.local.DayInfo;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public class DaysInfoRepositoryLocalFile implements DaysInfoRepository {

    private final File daysInfoFile;
    private final ObjectMapper mapper;

    private final Map<LocalDate, DayInfo> days;

    public DaysInfoRepositoryLocalFile(File daysInfoFile, ObjectMapper mapper) {
        this.daysInfoFile = daysInfoFile;
        this.mapper = mapper;

        boolean fileIsEmpty = daysInfoFile.length() == 0;
        if (fileIsEmpty) {
            days = new HashMap<>();
            DayInfo today = DayInfo.defaultInstance();
            writeToday(today);
            save();
        } else {
            days = loadDaysInfo();
        }
    }

    @SneakyThrows
    private Map<LocalDate, DayInfo> loadDaysInfo() {
        // todo: handle exception and decide what to do
        List<DayInfo> dayList = mapper.readValue(daysInfoFile, asListOf(DayInfo.class));
        return dayList.stream()
                .collect(toMap(DayInfo::getDate, identity()));
    }

    // todo: move to another place
    private CollectionType asListOf(Class<?> type) {
        return mapper.getTypeFactory().constructCollectionType(List.class, type);
    }

    @Override
    public DayInfo getTodayInfo() {
        return getDayInfo(LocalDate.now());
    }

    @Override
    public DayInfo getDayInfo(LocalDate forDate) {
        return days.get(forDate);
    }

    @Override
    public void writeToday(DayInfo dayInfo) {
        write(dayInfo, LocalDate.now());
    }

    @Override
    public void write(DayInfo dayInfo, LocalDate date) {
        days.put(date, dayInfo);
    }

    private void save() {
        try {
            mapper.writeValue(daysInfoFile, days.values());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
