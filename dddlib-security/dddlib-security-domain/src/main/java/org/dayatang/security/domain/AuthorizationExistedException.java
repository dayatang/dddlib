package org.dayatang.security.domain;

/**
 * Created by yyang on 15/7/27.
 */
public class AuthorizationExistedException extends SecurityException {
    public AuthorizationExistedException() {
    }

    public AuthorizationExistedException(String message) {
        super(message);
    }

    public AuthorizationExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthorizationExistedException(Throwable cause) {
        super(cause);
    }

    public AuthorizationExistedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
