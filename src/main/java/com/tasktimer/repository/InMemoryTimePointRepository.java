package com.tasktimer.repository;

import javafx.util.Duration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

public class InMemoryTimePointRepository implements TimePointRepository<Duration> {
    private final List<CollectionAddValueListener<Duration>> listeners = new ArrayList<>();

    private final SortedSet<Duration> timePointsRepo = new TreeSet<>();

    /** TimePointMachine */

    @Override
    public void addPoint(Duration timePoint) {
        timePointsRepo.add(timePoint);
        notifyListeners(timePoint);
    }

    @Override
    public Duration getLastPoint() {
        return timePointsRepo.last();
    }

    @Override
    public SortedSet<Duration> getForDate(LocalDate date) {
        return timePointsRepo;
    }

    @Override
    public SortedSet<Duration> getForToday() {
        return timePointsRepo;
    }

    @Override
    public void resetToday() {
        timePointsRepo.clear();
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

    private void notifyListeners(Duration addedPoint) {
        listeners.forEach(listener -> listener.onChange(timePointsRepo, addedPoint));
    }
}
