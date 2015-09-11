package org.dayatang.security.domain;


import org.dayatang.dddlib.event.api.AbstractEvent;

import java.util.Date;

/**
 * Created by yyang on 15/2/10.
 */
public class ActorDisabledEvent<T extends Actor> extends AbstractEvent {
    private T actor;

    public ActorDisabledEvent(T actor) {
        this.actor = actor;
    }

    public ActorDisabledEvent(T actor, Date occurredOn) {
        super(occurredOn);
        this.actor = actor;
    }

    public T getActor() {
        return actor;
    }
}
