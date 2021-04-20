package com.tasktimer.repository;

import java.time.LocalDate;
import java.util.SortedSet;

public interface TimePointRepository<T> extends ObservableCollection<T> {

    void addPoint(T timePoint);

    T getLastPoint();

    SortedSet<T> getForDate(LocalDate date);

    SortedSet<T> getForToday();

    void resetToday();

}
