package org.dayatang.dddlib.event.api;

import java.util.Date;

/**
 * Created by yyang on 15/9/11.
 */
public class EmployeeRetiredEvent extends AbstractEvent {
    public EmployeeRetiredEvent() {
    }

    public EmployeeRetiredEvent(Date occurredOn) {
        super(occurredOn);
    }

    public EmployeeRetiredEvent(Date occurredOn, int version) {
        super(occurredOn, version);
    }
}
