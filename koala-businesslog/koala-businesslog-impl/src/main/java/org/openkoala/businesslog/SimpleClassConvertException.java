package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 12/13/13
 * Time: 2:33 PM
 */
public class SimpleClassConvertException extends RuntimeException {
    public SimpleClassConvertException() {
    }

    public SimpleClassConvertException(String message) {
        super(message);
    }

    public SimpleClassConvertException(Throwable cause) {
        super(cause);
    }

    public SimpleClassConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
