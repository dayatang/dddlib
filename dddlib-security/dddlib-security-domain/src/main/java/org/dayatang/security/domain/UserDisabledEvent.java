package org.dayatang.security.domain;

import java.util.Date;

/**
 * Created by yyang on 15/2/10.
 */
public class UserDisabledEvent extends ActorDisabledEvent<User> {
    private User user;

    public UserDisabledEvent(User user) {
        super(user);
    }

    public UserDisabledEvent(User user, Date occurredOn) {
        super(user, occurredOn);
    }
}
