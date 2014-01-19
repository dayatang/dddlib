package org.openkoala.opencis;


public class DeveloperValidateFailureException extends CISClientBaseRuntimeException {

    private static final long serialVersionUID = 2389484663291825626L;

    public DeveloperValidateFailureException() {
        super();
    }

    public DeveloperValidateFailureException(String message) {
        super(message);
    }

    public DeveloperValidateFailureException(String message, Throwable cause) {
        super(message, cause);
    }

}
