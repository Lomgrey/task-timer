package com.tasktimer.repository;

import com.tasktimer.repository.local.InMemoryCycleRepository;
import javafx.util.Duration;

public class CycleRepositoryFactory {

    private static final CycleRepository<Duration> CYCLE_REPOSITORY =
            new InMemoryCycleRepository<>();

    public static CycleRepository<Duration> getInstance() {
        return CYCLE_REPOSITORY;
    }

}
