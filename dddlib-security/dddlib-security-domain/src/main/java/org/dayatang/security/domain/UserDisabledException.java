package org.dayatang.security.domain;

/**
 * Created by yyang on 15/8/5.
 */
public class UserDisabledException extends SecurityException {
    public UserDisabledException() {
    }

    public UserDisabledException(String message) {
        super(message);
    }

    public UserDisabledException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDisabledException(Throwable cause) {
        super(cause);
    }

    public UserDisabledException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
