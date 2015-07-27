package org.dayatang.security.domain;

/**
 * Created by yyang on 15/7/27.
 */
public class PasswordUnmatchException extends SecurityException {
    public PasswordUnmatchException() {
    }

    public PasswordUnmatchException(String message) {
        super(message);
    }

    public PasswordUnmatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordUnmatchException(Throwable cause) {
        super(cause);
    }

    public PasswordUnmatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
