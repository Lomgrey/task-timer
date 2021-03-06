package com.tasktimer.repository.local;

import com.tasktimer.repository.LapRepository;
import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InMemoryLapRepository<T> implements LapRepository<Duration> {
    private final List<CollectionAddValueListener<Duration>> listeners = new ArrayList<>();
    private final List<Duration> laps = new ArrayList<>();

    @Override
    public void addLap(Duration lapTime) {
        laps.add(lapTime);
        notifyListeners(lapTime);
    }

    @Override
    public void removeLap(Duration lap) {
        laps.remove(lap);
    }

    @Override
    public List<Duration> getForDate(LocalDate date) {
        return laps;
    }

    @Override
    public List<Duration> getForToday() {
        return laps;
    }

    @Override
    public void resetToday() {
        laps.clear();
        notifyListeners();
    }

    /** Listener */

    @Override
    public void addListener(CollectionAddValueListener<Duration> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(CollectionAddValueListener<Duration> listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        notifyListeners(null);
    }

    private void notifyListeners(Duration added) {
        listeners.forEach(listener -> listener.onChange(laps, added));
    }
}
