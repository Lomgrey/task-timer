package com.tasktimer.feature.edit;

import javafx.util.Duration;

import java.util.function.BiConsumer;

public enum EditAction {
    ADD((item, editor) -> editor.add(item)),
    REMOVE((item, editor) -> editor.remove(item));

    private final BiConsumer<Duration, TimeHistoryEditor> consumer;

    EditAction(BiConsumer<Duration, TimeHistoryEditor> consumer) {
        this.consumer = consumer;
    }

    void process(Duration item, TimeHistoryEditor editor) {
        consumer.accept(item, editor);
    }
}
