package org.openkoala.businesslog;

/**
 * XML配置文件找不到
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:14 AM
 */
public class BusinessLogXmlConfigNotFoundException extends RuntimeException {

    public BusinessLogXmlConfigNotFoundException() {
    }

    public BusinessLogXmlConfigNotFoundException(String message) {
        super(message);
    }

    public BusinessLogXmlConfigNotFoundException(Throwable cause) {
        super(cause);
    }

    public BusinessLogXmlConfigNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
