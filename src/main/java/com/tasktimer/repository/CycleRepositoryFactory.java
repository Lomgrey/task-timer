package com.tasktimer.repository;

import com.tasktimer.repository.local.LocalFileCycleRepository;
import javafx.util.Duration;

public class CycleRepositoryFactory {

    private static CycleRepository<Duration> cycleRepository;

    public synchronized static CycleRepository<Duration> getInstance() {
        if (cycleRepository == null) {
            cycleRepository = new LocalFileCycleRepository(null);
        }
        return cycleRepository;
    }

}
