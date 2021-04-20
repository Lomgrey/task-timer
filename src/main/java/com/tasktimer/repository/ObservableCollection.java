package com.tasktimer.repository;

import java.util.Collection;
import java.util.List;

/**
 * @param <T> - Type of collection's items
 */
public interface ObservableCollection<T> {

    void addListener(CollectionAddValueListener<T> listener);

    void removeListener(CollectionAddValueListener<T> listener);

    interface CollectionAddValueListener<T> {
        void onChange(Collection<? extends T> fullList, T addedValue);
    }

}
