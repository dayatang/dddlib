package org.dayatang.domain.event;

import org.dayatang.utils.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yyang on 14-9-12.
 */
public class DomainEventPublisherImpl implements DomainEventPublisher {

    private List<DomainEventSubscriber> subscribers = new ArrayList<DomainEventSubscriber>();

    @Override
    public void publish(final DomainEvent event) {
        for (DomainEventSubscriber subscriber : subscribers) {
            subscriber.handleEvent(event);
        }
    }

    @Override
    public void publishAll(final Collection<DomainEvent> events) {
        if (events == null) {
            return;
        }
        for (DomainEvent event : events) {
            publish(event);
        }
    }

    @Override
    public void subscribe(final DomainEventSubscriber subscriber) {
        Assert.notNull(subscriber);
        subscribers.add(subscriber);
    }

    @Override
    public void unsubscribe(final DomainEventSubscriber subscriber) {
        Assert.notNull(subscriber);
        subscribers.remove(subscriber);
    }

    @Override
    public void clearSubscribers() {
        subscribers.clear();
    }

    boolean hasSubscriber(final DomainEventSubscriber subscriber) {
        Assert.notNull(subscriber);
        return subscribers.contains(subscriber);
    }

    boolean hasSubscribers() {
        return !subscribers.isEmpty();
    }
}
