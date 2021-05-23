package com.tasktimer.repository.factory;

import com.tasktimer.repository.TimePointRepository;
import com.tasktimer.repository.local.InMemoryTimePointRepository;
import javafx.util.Duration;

public class TimeRepositoryFactory {

    private static final TimePointRepository<Duration> TIME_POINT_REPOSITORY =
            new InMemoryTimePointRepository();

    public static TimePointRepository<Duration> getInstance() {
        return TIME_POINT_REPOSITORY;
    }

}
