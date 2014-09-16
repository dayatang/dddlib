package org.dayatang.mysql;

import org.dayatang.domain.event.DomainEvent;
import org.dayatang.domain.event.EventStore;
import org.dayatang.domain.event.StoredEvent;

import java.util.Date;
import java.util.List;

/**
 * Created by yyang on 14-9-16.
 */
public class MySqlEventStore implements EventStore {
    @Override
    public List<StoredEvent> findStoredEventsBetween(Date occurredFrom, Date occurredTo) {
        return null;
    }

    @Override
    public List<StoredEvent> findStoredEventsSince(Date occurredFrom) {
        return null;
    }

    @Override
    public StoredEvent append(DomainEvent domainEvent) {
        return null;
    }

    @Override
    public void close() {

    }

    @Override
    public long countStoredEvents() {
        return 0;
    }
}
