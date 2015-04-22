package org.dayatang.domain.event;

import java.util.Date;

/**
 * 领域事件接口，领域事件代表具有业务含义的事件，例如员工调动或者机构调整
 * Created by yyang on 14-9-12.
 */
public interface DomainEvent {

    /**
     * 获得事件ID
     * @return 事件的ID
     */
    String getId();

    /**
     * 获得事件发生时间
     * @return 事件发生时间
     */
    Date getOccurredOn();

    /**
     * 获得版本
     * @return 事件的版本
     */
    int getVersion();
}
