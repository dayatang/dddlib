package org.openkoala.opencis;

/**
 * jenkins设置SVN时，认证失败
 */
public class JenkinsSvnAuthenticationFailureException extends RuntimeException {

    private static final long serialVersionUID = 504377236660620600L;

    public JenkinsSvnAuthenticationFailureException() {
        super();
    }

    public JenkinsSvnAuthenticationFailureException(String message, Throwable cause) {
        super(message, cause);
    }

    public JenkinsSvnAuthenticationFailureException(String message) {
        super(message);
    }

    public JenkinsSvnAuthenticationFailureException(Throwable cause) {
        super(cause);
    }

}
