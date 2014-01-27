package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 12/13/13
 * Time: 2:35 PM
 */
public class SimpleClassEnumNotFoundException extends RuntimeException {
    public SimpleClassEnumNotFoundException() {
    }

    public SimpleClassEnumNotFoundException(String message) {
        super(message);
    }

    public SimpleClassEnumNotFoundException(Throwable cause) {
        super(cause);
    }

    public SimpleClassEnumNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
