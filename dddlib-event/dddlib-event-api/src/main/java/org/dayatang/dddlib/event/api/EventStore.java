package org.dayatang.dddlib.event.api;

/**
 * 事件存储
 * Created by yyang on 15/4/23.
 */
public interface EventStore {

    void store(Event event);
}
