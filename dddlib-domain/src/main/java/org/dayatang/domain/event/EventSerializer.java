package org.dayatang.domain.event;

/**
 * 领域事件序列化/反序列化工具
 * Created by yyang on 14-9-14.
 */
public interface EventSerializer {

    /**
     * 将领域事件序列化为字符串
     * @param domainEvent 要序列化的字符串
     * @return 领域事件的序列化形式
     */
    String serialize(DomainEvent domainEvent);

    /**
     * 从字符串形式反序列化为领域事件实例
     * @param eventBody 领域事件的字符串序列化形式
     * @param domainEventClass 领域事件的类
     * @param <T> 领域事件的类型
     * @return 领域事件实例
     */
    <T extends DomainEvent> T deserialize(String eventBody, Class<T> domainEventClass);
}
