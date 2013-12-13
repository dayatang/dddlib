package org.openkoala.businesslog;

/**
 * User: zjzhai
 * Date: 12/13/13
 * Time: 9:35 AM
 */
public class FreemarkerProcessorException extends RuntimeException {
    public FreemarkerProcessorException() {
    }

    public FreemarkerProcessorException(String message) {
        super(message);
    }

    public FreemarkerProcessorException(Throwable cause) {
        super(cause);
    }

    public FreemarkerProcessorException(String message, Throwable cause) {
        super(message, cause);
    }
}
