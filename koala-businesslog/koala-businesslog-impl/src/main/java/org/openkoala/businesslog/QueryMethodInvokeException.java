package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:38 AM
 */
public class QueryMethodInvokeException extends RuntimeException {
    public QueryMethodInvokeException() {
    }

    public QueryMethodInvokeException(String message) {
        super(message);
    }

    public QueryMethodInvokeException(Throwable cause) {
        super(cause);
    }

    public QueryMethodInvokeException(String message, Throwable cause) {
        super(message, cause);
    }
}
