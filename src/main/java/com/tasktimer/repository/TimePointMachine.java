package com.tasktimer.repository;

import java.time.LocalDate;
import java.util.SortedSet;

public interface TimePointMachine<T> extends ObservableCollection<T> {

    void addPoint(T timePoint);

    T getLastPoint();

    SortedSet<T> getForDate(LocalDate date);

    SortedSet<T> getForToday();

    void resetToday();

}
