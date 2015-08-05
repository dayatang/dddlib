package org.dayatang.security.domain;

/**
 * Created by yyang on 15/8/5.
 */
public class UserLockedException extends SecurityException {
    public UserLockedException() {
    }

    public UserLockedException(String message) {
        super(message);
    }

    public UserLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLockedException(Throwable cause) {
        super(cause);
    }

    public UserLockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
