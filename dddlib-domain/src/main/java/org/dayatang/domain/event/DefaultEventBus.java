package org.dayatang.domain.event;

import org.dayatang.utils.Assert;

/**
 * Created by yyang on 14-10-4.
 */
public class DefaultEventBus implements EventBus {

    private com.google.common.eventbus.EventBus eventBus;

    private EventStore eventStore;

    public DefaultEventBus(com.google.common.eventbus.EventBus eventBus, EventStore eventStore) {
        Assert.notNull(eventBus, "EventBus is null!");
        Assert.notNull(eventStore, "EventStore is null!");
        this.eventBus = eventBus;
        this.eventStore = eventStore;
    }

    @Override
    public void publish(DomainEvent event) {
        eventBus.post(event);
        eventStore.append(event);
    }

    @Override
    public void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    @Override
    public void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }
}
