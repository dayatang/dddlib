package org.openkoala.opencis.api;

import org.apache.commons.lang3.StringUtils;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 2:09 PM
 */
public class AuthenticationResult<T> {

    private T context;

    private String errors;

    public AuthenticationResult() {
    }

    public boolean isSuccess() {
        return StringUtils.isEmpty(errors);
    }

    public T getContext() {
        return context;
    }

    public void setContext(T context) {
        this.context = context;
    }

    public String getErrors() {
        return errors;
    }

    public void setErrors(String errors) {
        this.errors = errors;
    }
}
