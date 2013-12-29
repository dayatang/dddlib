package org.openkoala.opencis;

/**
 * 认证失败异常
 * User: zjzhai
 * Date: 12/29/13
 * Time: 10:16 AM
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super();
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }
}
