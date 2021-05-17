package com.tasktimer.repository;

import com.tasktimer.repository.local.LocalFileCycleRepository;
import com.tasktimer.repository.local.files.OSDependLocalFile;
import javafx.util.Duration;

import java.io.File;

public class CycleRepositoryFactory {

    private static CycleRepository<Duration> cycleRepository;

    public synchronized static CycleRepository<Duration> getInstance() {
        if (cycleRepository == null) {
            File localFile = OSDependLocalFile.get();
            cycleRepository = new LocalFileCycleRepository(localFile);
        }
        return cycleRepository;
    }

}
