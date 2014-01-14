package org.openkoala.opencis.jenkins.impl;

import org.openkoala.opencis.api.AuthenticationStrategy;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:25 AM
 */
public class CASAuthentication implements AuthenticationStrategy {


    private String error;

    @Override
    public boolean authenticate() {
        System.out.println("cas authentication success");
        return true;
    }

    @Override
    public Object getContext() {
        return new String();
    }

    @Override
    public String getErrors() {
        return error;
    }
}
