package org.dayatang.domain.event;

import com.google.common.eventbus.EventBus;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 * Created by yyang on 14-10-4.
 */
public class DefaultEventBusTest {

    private DefaultEventBus instance;

    private EventBus eventBus;

    private EventStore eventStore;

    @Before
    public void setUp() {
        eventBus = mock(EventBus.class);
        eventStore = mock(EventStore.class);
        instance = new DefaultEventBus(eventBus, eventStore);
    }

    @Test
    public void publishEvent() {
        DomainEvent event = new DomainEventSub();
        instance.publish(event);
        verify(eventBus).post(event);
        verify(eventStore).append(event);
    }

    @Test
    public void registerSubscriber() {
        Object subscriber = new String("abc");
        instance.register(subscriber);
        verify(eventBus).register(subscriber);
    }

    @Test
    public void unregisterSubscriber() {
        Object subscriber = new String("abc");
        instance.unregister(subscriber);
        verify(eventBus).unregister(subscriber);
    }
}
