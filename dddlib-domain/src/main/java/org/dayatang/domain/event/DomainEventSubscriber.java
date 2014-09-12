package org.dayatang.domain.event;

/**
 * 领域事件订阅者接口。它可以被注册到领域事件发布者DomainEventPublisher，当后者发布事件时得到通知，
 * 如果该事件是自己订阅的事件，执行handleEvent()方法。
 * Created by yyang on 14-9-12.
 */
public interface DomainEventSubscriber {

    void handleEvent(DomainEvent domainEvent);

}
