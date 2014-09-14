package org.dayatang.domain.event;

import org.dayatang.domain.InstanceFactory;
import org.dayatang.utils.Assert;

import java.util.Date;

/**
 * 事件的存储形式。
 * Created by yyang on 14-9-14.
 */
public class StoredEvent {

    private String typeName;    //事件类型名称
    private Date occurredOn;    //事件发生时间
    private String eventBody;   //用字符串表示的事件体
    private String eventId;       //事件ID，关联领域事件DomainEvent的ID
    private EventSerializer serializer;

    public StoredEvent(String typeName, Date occurredOn, String eventBody) {
        Assert.notNull(occurredOn, "occurredOn is null!");
        Assert.notEmpty(typeName, "typeName is null or empty!");
        this.typeName = typeName;
        this.eventBody = eventBody;
        this.occurredOn = occurredOn;
    }

    public StoredEvent(String typeName, Date occurredOn, String eventBody, String eventId) {
        this(typeName, occurredOn, eventBody);
        this.eventId = eventId;
    }

    public final EventSerializer getSerializer() {
        if (serializer == null) {
            serializer = InstanceFactory.getInstance(EventSerializer.class);
        }
        return serializer;
    }

    public void setSerializer(EventSerializer serializer) {
        this.serializer = serializer;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }

    public String getEventBody() {
        return eventBody;
    }

    public String getEventId() {
        return eventId;
    }

    public String getTypeName() {
        return typeName;
    }

    public static StoredEvent fromDomainEvent(DomainEvent event, EventSerializer serializer) {
        Assert.notNull(event);
        Assert.notNull(serializer);
        return new StoredEvent(event.getClass().getName(), event.getOccurredOn(),
                serializer.serialize(event), event.getId());
    }

    public <T extends DomainEvent> T toDomainEvent() {
        Class<T> domainEventClass = null;

        try {
            domainEventClass = (Class<T>) Class.forName(this.getTypeName());
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Class load error, because: "
                            + e.getMessage());
        }
        return getSerializer().deserialize(eventBody, domainEventClass);
    }
}
