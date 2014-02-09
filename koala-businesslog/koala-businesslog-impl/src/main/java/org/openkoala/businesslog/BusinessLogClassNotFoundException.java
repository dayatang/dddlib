package org.openkoala.businesslog;

/**
 *
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:14 AM
 */
public class BusinessLogClassNotFoundException extends RuntimeException {

    public BusinessLogClassNotFoundException() {
    }

    public BusinessLogClassNotFoundException(String message) {
        super(message);
    }

    public BusinessLogClassNotFoundException(Throwable cause) {
        super(cause);
    }

    public BusinessLogClassNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
