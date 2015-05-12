package org.dayatang.dddlib.event;

/**
 * 事件总线接口。当接收到领域事件时调用合适的事件处理器处理这些事件。
 * Created by yyang on 15/4/23.
 */
public interface EventBus {

    /**
     * 注册事件处理器
     * @param handler 要注册的事件处理器
     */
    void register(EventListener handler);

    /**
     * 卸载事件处理器
     * @param handler 要卸载的事件处理器
     */
    void unregister(EventListener handler);

    /**
     * 接收领域事件
     * @param event 要接收的领域事件
     */
    void post(Event event);

}
