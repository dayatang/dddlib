package org.dayatang.dddlib.event;

import com.google.common.collect.ImmutableList;
import org.dayatang.utils.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * 缺省的进程内事件总线
 * @author yyang
 */
public final class DefaultEventBus implements EventBus {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultEventBus.class);

    private List<EventListener> listeners = new ArrayList<EventListener>();

    public DefaultEventBus() {
    }

    public DefaultEventBus(List<EventListener> listeners) {
        Assert.notEmpty(listeners, "listeners must not be null or empty.");
        this.listeners = ImmutableList.copyOf(listeners);
    }

    @Override
    public void register(EventListener listener) {
        listeners.add(listener);
    }

    @Override
    public void unregister(EventListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void post(DomainEvent event) {
        for (EventListener listener : listeners) {
            listener.onEvent(event);
        }
    }
}
