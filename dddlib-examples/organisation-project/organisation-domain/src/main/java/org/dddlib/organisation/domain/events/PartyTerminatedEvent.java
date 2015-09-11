package org.dddlib.organisation.domain.events;

import org.dayatang.dddlib.event.api.AbstractEvent;

import java.util.Date;

/**
 * 当事人终止事件
 * Created by yyang on 15/4/23.
 */
public class PartyTerminatedEvent extends AbstractEvent {

    private Long partyId;

    public PartyTerminatedEvent(Long partyId) {
        this.partyId = partyId;
    }

    public PartyTerminatedEvent(Date occurredOn, Long partyId) {
        super(occurredOn);
        this.partyId = partyId;
    }

    public Long getPartyId() {
        return partyId;
    }
}
