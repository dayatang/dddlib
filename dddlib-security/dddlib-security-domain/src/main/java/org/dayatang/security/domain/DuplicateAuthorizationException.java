package org.dayatang.security.domain;

/**
 * Created by yyang on 15/7/27.
 */
public class DuplicateAuthorizationException extends SecurityException {
    public DuplicateAuthorizationException() {
    }

    public DuplicateAuthorizationException(String message) {
        super(message);
    }

    public DuplicateAuthorizationException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateAuthorizationException(Throwable cause) {
        super(cause);
    }

    public DuplicateAuthorizationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
