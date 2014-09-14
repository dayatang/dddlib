package org.dayatang.domain.event;

import java.util.Date;
import java.util.List;

/**
 * 事件存储接口
 * Created by yyang on 14-9-15.
 */
public interface EventStore {

    /**
     * 获取指定时间范围发生的历史事件的集合，时间范围包含occurredFrom，不包含occurredTo。
     * @param occurredFrom 事件发生时间的下限
     * @param occurredTo 事件发生时间的上限
     * @return 指定时间范围发生的事件的集合，按发生时间升序排序。
     */
    public List<StoredEvent> allStoredEventsBetween(Date occurredFrom, Date occurredTo);

    /**
     * 获取指定时间及其之后发生的历史事件的集合。
     * @param occurredFrom 事件发生时间的下限
     * @return 指定时间及其之后发生的事件的集合，按发生时间升序排序。
     */
    public List<StoredEvent> allStoredEventsSince(Date occurredFrom);

    /**
     * 向事件存储中插入一个新的领域事件
     * @param domainEvent 一个领域事件
     * @return 代表领域事件的存储的事件
     */
    public StoredEvent append(DomainEvent domainEvent);

    /**
     * 关闭事件存储
     */
    public void close();

    /**
     * 统计存储的事件的数量
     * @return 已存储的事件的数量
     */
    public long countStoredEvents();

}
