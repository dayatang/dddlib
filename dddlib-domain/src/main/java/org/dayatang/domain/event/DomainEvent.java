package org.dayatang.domain.event;

import org.dayatang.utils.Assert;

import java.util.Date;

/**
 * 领域事件基类，领域事件代表具有业务含义的事件，例如员工调动或者机构调整
 * Created by yyang on 14-9-12.
 */
public abstract class DomainEvent {

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

    public Date getOccurredOn() {
        return occurredOn;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
