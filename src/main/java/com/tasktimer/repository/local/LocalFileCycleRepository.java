package com.tasktimer.repository.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.tasktimer.repository.CycleRepository;
import com.tasktimer.repository.mapper.ObjectMapperFactory;
import com.tasktimer.util.TimeAction;
import javafx.util.Duration;
import lombok.SneakyThrows;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import static com.tasktimer.util.TimeAction.MINUS;
import static com.tasktimer.util.TimeAction.PLUS;

public class LocalFileCycleRepository implements CycleRepository<Duration> {
    private final List<CollectionAddValueListener<Duration>> listeners = new ArrayList<>();

    private final ObjectMapper mapper;

    private final File localFile;
    private final DayInfo todayInfo; // todo: decide what to do when new day stared
    private final List<DayInfo> allDays;

    private final boolean autosave;

    public LocalFileCycleRepository(File localFile) {
        this(localFile, false);
    }

    public LocalFileCycleRepository(File localFile, boolean autosave) {
        this.autosave = autosave;
        mapper = ObjectMapperFactory.MAPPER;

        this.localFile = localFile;
        boolean fileIsEmpty = localFile.length() == 0;
        if (fileIsEmpty) {
            todayInfo = DayInfo.withDefaultFields();
            allDays = new ArrayList<>();
        } else {
            allDays = readDaysInfo(localFile); // todo: load only necessary days
            todayInfo = getDayInfoForDate(LocalDate.now())
                    .orElseGet(DayInfo::withDefaultFields);
        }
        allDays.add(todayInfo);
        save();
    }

    private DayInfo createTodayDayInfo() {
        return DayInfo.withDefaultFields();
    }

    @SneakyThrows
    private List<DayInfo> readDaysInfo(File file) {
        // todo: handle exception and decide what to do
        return mapper.readValue(file, asListOf(DayInfo.class));

    }

    // todo: move to another place
    private CollectionType asListOf(Class<?> type) {
        return mapper.getTypeFactory().constructCollectionType(List.class, type);
    }

    @Override
    public void addLap(Duration lap) {
        todayInfo.getCycles().add(lap);
        updateDayDuration(PLUS, lap);
        notifyListeners(lap);
        save(localFile);
    }

    @Override
    public void removeLap(Duration lap) {
        todayInfo.getCycles().remove(lap);
        updateDayDuration(MINUS, lap);
        save(localFile);
    }

    @Override
    public List<Duration> getForDate(LocalDate date) {
        return getDayInfoForDate(date)
                .map(DayInfo::getCycles)
                .orElse(new ArrayList<>());
    }

    @Override
    public List<Duration> getForToday() {
        return todayInfo.getCycles();
    }

    @Override
    public void resetToday() {
        // todo
    }

    @Override
    public boolean save(File file) {
        try {
            mapper.writeValue(file, allDays);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean save() {
        return save(localFile);
    }

    private Optional<DayInfo> getDayInfoForDate(LocalDate date) {
        return allDays.stream()
                .filter(day -> day.getDate().isEqual(date))
                .findFirst();
    }

    private void updateDayDuration(TimeAction action, Duration time) {
        Duration dayDuration = todayInfo.getDayDuration();
        todayInfo.setDayDuration(action.apply(dayDuration, time));
    }


    /**
     * LISTENERS
     */

    @Override
    public void addListener(CollectionAddValueListener<Duration> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CollectionAddValueListener<Duration> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(Duration added) {
        listeners.forEach(listener -> listener.onChange(todayInfo.getCycles(), added));
    }

}
