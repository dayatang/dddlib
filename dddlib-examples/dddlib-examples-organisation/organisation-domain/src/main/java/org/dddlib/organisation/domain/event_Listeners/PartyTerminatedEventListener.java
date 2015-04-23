package org.dddlib.organisation.domain.event_Listeners;

import org.dayatang.dddlib.event.AbstractEventListener;
import org.dddlib.organisation.domain.Accountability;
import org.dddlib.organisation.domain.Party;
import org.dddlib.organisation.domain.events.PartyTerminatedEvent;

/**
 * Created by yyang on 15/4/23.
 */
public class PartyTerminatedEventListener extends AbstractEventListener<PartyTerminatedEvent> {

    @Override
    public void handle(PartyTerminatedEvent event) {
        Accountability.when(event);
    }
}
