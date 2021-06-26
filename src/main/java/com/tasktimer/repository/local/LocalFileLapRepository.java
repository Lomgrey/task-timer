package com.tasktimer.repository.local;

import com.tasktimer.repository.LapRepository;
import com.tasktimer.repository.DaysInfoRepository;
import com.tasktimer.util.TimeAction;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static com.tasktimer.util.TimeAction.MINUS;
import static com.tasktimer.util.TimeAction.PLUS;

public class LocalFileLapRepository implements LapRepository<Duration> {
    private final DaysInfoRepository daysInfoRepository;

    private final List<CollectionAddValueListener<Duration>> listeners = new ArrayList<>();

    private final DayInfo todayInfo; // todo: decide what to do when new day stared

    public LocalFileLapRepository(DaysInfoRepository daysInfoRepository) {
        this.daysInfoRepository = daysInfoRepository;

        todayInfo = daysInfoRepository.getTodayInfo();
    }

    /**
     * REPOSITORY
     */

    @Override
    public void addLap(Duration lap) {
        todayInfo.getCycles().add(lap);
        updateDayDuration(PLUS, lap);
        notifyListeners(lap);
        daysInfoRepository.write(todayInfo);
    }

    @Override
    public void removeLap(Duration lap) {
        todayInfo.getCycles().remove(lap);
        updateDayDuration(MINUS, lap);
        daysInfoRepository.write(todayInfo);
    }

    @Override
    public List<Duration> getForDate(LocalDate date) {
        return daysInfoRepository.getDayInfo(date)
                .getCycles();
    }

    @Override
    public List<Duration> getForToday() {
        return todayInfo.getCycles();
    }

    @Override
    public void resetToday() {
        // todo
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
