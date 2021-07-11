package com.tasktimer.feature.edit;

import com.tasktimer.repository.LapRepository;
import javafx.util.Duration;

public class TimeHistoryEditor {

    private final LapRepository<Duration> lapRepository;

    public TimeHistoryEditor(LapRepository<Duration> lapRepository) {
        this.lapRepository = lapRepository;
    }

    public void edit(EditAction editAction, Duration item) {
        editAction.process(item, this);
    }

    void add(Duration lap) {
        lapRepository.addLap(lap);
    }

    void remove(Duration lap) {
        lapRepository.removeLap(lap);
    }

}

