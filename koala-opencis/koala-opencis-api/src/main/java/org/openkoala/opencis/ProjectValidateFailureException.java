package org.openkoala.opencis;

/**
 * User: zjzhai
 * Date: 1/19/14
 * Time: 10:18 AM
 */
public class ProjectValidateFailureException extends CISClientBaseRuntimeException {
    public ProjectValidateFailureException() {
        super();
    }

    public ProjectValidateFailureException(String message) {
        super(message);
    }

    public ProjectValidateFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
