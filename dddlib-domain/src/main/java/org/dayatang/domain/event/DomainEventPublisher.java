package org.dayatang.domain.event;

import java.util.Collection;

/**
 * Created by yyang on 14-9-12.
 */
public interface DomainEventPublisher {

    void publish(DomainEvent event);

    void publishAll(Collection<DomainEvent> events);

    void subscribe(DomainEventSubscriber subscriber);

    void unsubscribe(DomainEventSubscriber subscriber);

    void clearSubscribers();
}
