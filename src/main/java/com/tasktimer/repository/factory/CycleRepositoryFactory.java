package com.tasktimer.repository.factory;

import com.tasktimer.repository.CycleRepository;
import com.tasktimer.repository.local.LocalFileCycleRepository;
import javafx.util.Duration;

public class CycleRepositoryFactory {

    private final static CycleRepository<Duration> CYCLE_REPOSITORY;
    static {
        CYCLE_REPOSITORY = new LocalFileCycleRepository(
                DaysInfoRepositoryFactory.getInstance());
    }

    public synchronized static CycleRepository<Duration> getInstance() {
        return CYCLE_REPOSITORY;
    }

}
