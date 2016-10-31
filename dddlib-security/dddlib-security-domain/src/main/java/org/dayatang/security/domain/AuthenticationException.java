package org.dayatang.security.domain;

/**
 * 用户认证异常。当用户登录时，如果用户名不存在、口令错误、用户已失效、用户已被锁定、口令过期等等，均抛出该异常。
 * Created by yyang on 2016/10/31.
 */
public class AuthenticationException extends SecurityException {
    public AuthenticationException() {
    }

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthenticationException(Throwable cause) {
        super(cause);
    }

    public AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
