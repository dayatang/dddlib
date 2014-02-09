package org.openkoala.businesslog;

/**
 * XML配置文件解析异常
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:14 AM
 */
public class BusinessLogXmlConfigParseException extends RuntimeException {

    public BusinessLogXmlConfigParseException() {
    }

    public BusinessLogXmlConfigParseException(String message) {
        super(message);
    }

    public BusinessLogXmlConfigParseException(Throwable cause) {
        super(cause);
    }

    public BusinessLogXmlConfigParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
