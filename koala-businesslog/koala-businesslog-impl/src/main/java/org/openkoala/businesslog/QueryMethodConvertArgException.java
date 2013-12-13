package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:38 AM
 */
public class QueryMethodConvertArgException extends RuntimeException {
    public QueryMethodConvertArgException() {
    }

    public QueryMethodConvertArgException(String message) {
        super(message);
    }

    public QueryMethodConvertArgException(Throwable cause) {
        super(cause);
    }

    public QueryMethodConvertArgException(String message, Throwable cause) {
        super(message, cause);
    }
}
