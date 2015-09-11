package org.dayatang.dddlib.event.bus;

import org.dayatang.dddlib.event.api.Event;
import org.dayatang.dddlib.event.api.EventBus;
import org.dayatang.dddlib.event.api.EventListener;
import org.dayatang.dddlib.event.api.EventStore;
import org.dayatang.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * 简单的进程内事件总线
 * @author yyang
 */
public final class SimpleEventBus implements EventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEventBus.class);

    private EventStore eventStore;

    private List<EventListener> listeners = new ArrayList<EventListener>();

    public SimpleEventBus(EventStore eventStore) {
        this.eventStore = eventStore;
    }

    public SimpleEventBus(EventStore eventStore, List<EventListener> listeners) {
        Assert.notNull(eventStore, "Event Store is null.");
        this.eventStore = eventStore;
        Assert.notEmpty(listeners, "listeners must not be null or empty.");
        this.listeners = Collections.unmodifiableList(listeners);
    }

    List<EventListener> getListeners() {
        return listeners;
    }

    @Override
    public void register(EventListener... listeners) {
        this.listeners.addAll(Arrays.asList(listeners));
    }

    @Override
    public void unregister(EventListener... listeners) {
        this.listeners.removeAll(Arrays.asList(listeners));
    }

    @Override
    public void post(Event event) {
        LOGGER.info("Post a event " + event + " to event bus");
        eventStore.store(event);
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
