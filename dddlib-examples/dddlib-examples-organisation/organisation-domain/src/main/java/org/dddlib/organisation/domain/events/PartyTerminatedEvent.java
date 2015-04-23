package org.dddlib.organisation.domain.events;

import org.dayatang.dddlib.event.DomainEvent;
import org.dayatang.dddlib.event.Event;

import java.util.Date;

/**
 * 当事人终止事件
 * Created by yyang on 15/4/23.
 */
public class PartyTerminatedEvent extends DomainEvent {
    private Long partyId;
    private Date occurredAt;

    public PartyTerminatedEvent(Long partyId) {
        this.partyId = partyId;
    }

    public PartyTerminatedEvent(Date occurredAt, Long partyId) {
        super(occurredAt);
        this.partyId = partyId;
    }

    public Long getPartyId() {
        return partyId;
    }
}
