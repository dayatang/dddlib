package org.dayatang.dddlib.event.api;

import java.util.Date;

/**
 * Created by yyang on 15/4/23.
 */
public class DomainEvent implements Event {

    private Date occurredAt = new Date();

    public DomainEvent() {
    }

    public DomainEvent(Date occurredAt) {
        this.occurredAt = occurredAt;
    }

    @Override
    public Date occurredAt() {
        return occurredAt;
    }
}
