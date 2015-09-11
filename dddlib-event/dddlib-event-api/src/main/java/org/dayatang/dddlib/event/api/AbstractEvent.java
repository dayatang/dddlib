package org.dayatang.dddlib.event.api;

import org.dayatang.utils.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * 领域事件基类，领域事件代表具有业务含义的事件，例如员工调动或者机构调整
 * Created by yyang on 14-9-12.
 */
public abstract class AbstractEvent implements Event {

    private String id = UUID.randomUUID().toString();

    private Date occurredOn = new Date();

    private int version = 1;

    public AbstractEvent() {
        this(new Date(), 1);
    }

    /**
     * @param occurredOn 发生时间
     */
    public AbstractEvent(Date occurredOn) {
        this(occurredOn, 1);
    }

    /**
     * @param occurredOn 发生时间
     * @param version    版本
     */
    public AbstractEvent(Date occurredOn, int version) {
        Assert.notNull(occurredOn);
        this.occurredOn = new Date(occurredOn.getTime());
        this.version = version;
    }

    /**
     * 获得事件ID
     *
     * @return 事件的ID
     */
    @Override
    public String id() {
        return id;
    }

    /**
     * 获得事件发生时间
     *
     * @return 事件发生时间
     */
    @Override
    public Date occurredOn() {
        return occurredOn;
    }

    /**
     * 获得版本
     *
     * @return 事件的版本
     */
    @Override
    public int version() {
        return version;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AbstractEvent)) {
            return false;
        }
        AbstractEvent that = (AbstractEvent) other;
        return this.id().equals(that.id());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}