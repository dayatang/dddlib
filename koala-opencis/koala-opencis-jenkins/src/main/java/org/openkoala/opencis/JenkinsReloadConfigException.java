package org.openkoala.opencis;

/**
 * User: zjzhai
 * Date: 12/30/13
 * Time: 10:00 AM
 */

public class JenkinsReloadConfigException extends RuntimeException {
    private static final long serialVersionUID = -1828633110513627939L;

    public JenkinsReloadConfigException() {
        super();
    }

    public JenkinsReloadConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public JenkinsReloadConfigException(String message) {
        super(message);
    }

    public JenkinsReloadConfigException(Throwable cause) {
        super(cause);
    }
}
