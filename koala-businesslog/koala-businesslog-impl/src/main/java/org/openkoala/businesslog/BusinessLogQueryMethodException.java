package org.openkoala.businesslog;

/**
 * 关联查询的方法的实例化异常
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:14 AM
 */
public class BusinessLogQueryMethodException extends RuntimeException {

    public BusinessLogQueryMethodException() {
    }

    public BusinessLogQueryMethodException(String message) {
        super(message);
    }

    public BusinessLogQueryMethodException(Throwable cause) {
        super(cause);
    }

    public BusinessLogQueryMethodException(String message, Throwable cause) {
        super(message, cause);
    }
}
