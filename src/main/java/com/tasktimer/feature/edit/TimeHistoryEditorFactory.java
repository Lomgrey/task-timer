package com.tasktimer.feature.edit;

import com.tasktimer.repository.factory.LapRepositoryFactory;

public class TimeHistoryEditorFactory {

    private static final TimeHistoryEditor TIME_HISTORY_EDITOR;
    static {
        TIME_HISTORY_EDITOR = new TimeHistoryEditor(LapRepositoryFactory.getInstance());
    }

    public static TimeHistoryEditor getInstance() {
        return TIME_HISTORY_EDITOR;
    }

}
