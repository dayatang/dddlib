package org.dayatang.domain.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by yyang on 14-9-12.
 */
public class DomainEventPublisherImpl implements DomainEventPublisher {

    private List<DomainEventSubscriber> subscribers = new ArrayList<DomainEventSubscriber>();

    @Override
    public void publish(DomainEvent event) {

    }

    @Override
    public void publishAll(Collection<DomainEvent> events) {

    }

    @Override
    public void subscribe(DomainEventSubscriber subscriber) {

    }

    @Override
    public void unsubscribe(DomainEventSubscriber subscriber) {

    }

    @Override
    public void clearSubscribers() {

    }
}
