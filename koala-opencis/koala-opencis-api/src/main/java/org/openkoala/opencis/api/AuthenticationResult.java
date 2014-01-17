package org.openkoala.opencis.api;


/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 2:09 PM
 */
public class AuthenticationResult {

    private Object context;

    private String errors;

    public AuthenticationResult() {
    }

    public boolean isSuccess() {
        return errors == null || "".equals(errors.trim());
    }

    public Object getContext() {
        return context;
    }

    public void setContext(Object context) {
        this.context = context;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
