package org.dayatang.dddlib.event.api;

import java.util.Date;

/**
 * Created by yyang on 15/4/23.
 */
public interface Event {

    /**
     * 获得事件ID
     * @return 事件的ID
     */
    String id();

    /**
     * 获得事件发生时间
     * @return 事件发生时间
     */
    Date occurredOn();

    /**
     * 获得版本
     * @return 事件的版本
     */
    int version();
}
