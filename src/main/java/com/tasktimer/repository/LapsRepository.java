package com.tasktimer.repository;

import java.time.LocalDate;
import java.util.List;

public interface LapsRepository<T> extends ObservableCollection<T> {

    void addLap(T lapTime);

    void removeLap(T lap);

    List<T> getForDate(LocalDate date);

    List<T> getForToday();

    void resetToday();

}
