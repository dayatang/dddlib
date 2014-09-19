package org.dayatang.utils.support;

import org.dayatang.utils.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * 领域事件基类，领域事件代表具有业务含义的事件，例如员工调动或者机构调整
 * Created by yyang on 14-9-12.
 */
public abstract class DomainEvent {

    private String id = UUID.randomUUID().toString();

    private Date occurredOn = new Date();

    private int version = 1;

    public DomainEvent() {
        this(new Date(), 1);
    }

    public DomainEvent(Date occurredOn) {
        this(occurredOn, 1);
    }

    public DomainEvent(Date occurredOn, int version) {
        Assert.notNull(occurredOn);
        this.occurredOn = new Date(occurredOn.getTime());
        this.version = version;
    }

    public String getId() {
        return id;
    }

    /**
     * For test only
     *
     * @param id
     */
    protected void setId(String id) {
        this.id = id;
    }

    public Date getOccurredOn() {
        return occurredOn;
    }

    public int getVersion() {
        return version;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DomainEvent)) {
            return false;
        }
        DomainEvent that = (DomainEvent) other;
        return this.getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
