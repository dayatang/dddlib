package org.openkoala.businesslog;

/**
 * properties配置文件异常
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:14 AM
 */
public class BusinessLogPropertiesConfigException extends RuntimeException {

    public BusinessLogPropertiesConfigException() {
    }

    public BusinessLogPropertiesConfigException(String message) {
        super(message);
    }

    public BusinessLogPropertiesConfigException(Throwable cause) {
        super(cause);
    }

    public BusinessLogPropertiesConfigException(String message, Throwable cause) {
        super(message, cause);
    }
}
