package org.dayatang.domain.event;

/**
 * 事件总线接口。事件发布者创建事件对象，然后以事件对象为参数调用post()方法，将事件发布到事件总线；
 * 通过调用register()方法将事件订阅者注册到事件总线，事件订阅者提供一个或多个以某种类型的领域事件
 * 为唯一参数，且标记为@Subscribe的方法，当事件总线中有新的事件发布且匹配该方法的参数类型时，事件
 * 总线将以该事件对象为参数调用事件订阅者的订阅方法。
 * Created by yyang on 14-10-4.
 */
public interface DomainEventBus {

    /**
     * 将领域事件发布给所有注册的事件订阅者
     * @param event 要发布的事件
     */
    void publishEvent(DomainEvent event);

    /**
     * 注册一个事件订阅者的所有订阅方法注册到事件总线以接收事件。
     * @param subscriber 事件订阅者。事件订阅者必须实现一个或多个以某个领域事件的子类实例为唯一参数
     *                   且标注为@Subscribe的方法。
     */
    void registerSubscriber(Object subscriber);

    /**
     * 撤销注册一个已注册到事件总线的事件订阅者的所有订阅方法。
     * @param subscriber 事件订阅者。
     */
    void unregisterSubscriber(Object subscriber);
}
