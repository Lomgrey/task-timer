package com.tasktimer.repository.factory;

import com.tasktimer.repository.LapRepository;
import com.tasktimer.repository.local.LocalFileLapRepository;
import javafx.util.Duration;

public class LapRepositoryFactory {

    private final static LapRepository<Duration> CYCLE_REPOSITORY;
    static {
        CYCLE_REPOSITORY = new LocalFileLapRepository(
                DaysInfoRepositoryFactory.getInstance());
    }

    public synchronized static LapRepository<Duration> getInstance() {
        return CYCLE_REPOSITORY;
    }

}
