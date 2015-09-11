package org.dayatang.dddlib.event.api;

import java.util.Date;

/**
 * Created by yyang on 15/9/11.
 */
public class PostCreatedEvent extends AbstractEvent {
    public PostCreatedEvent() {
    }

    public PostCreatedEvent(Date occurredOn) {
        super(occurredOn);
    }

    public PostCreatedEvent(Date occurredOn, int version) {
        super(occurredOn, version);
    }
}
