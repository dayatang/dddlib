package org.openkoala.opencis.jenkins.impl;

import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import sun.net.www.http.HttpClient;

/**
 * User: zjzhai
 * Date: 1/14/14
 * Time: 9:25 AM
 */
public class CASAuthentication implements AuthenticationStrategy {


    @Override
    public AuthenticationResult authenticate() {

        AuthenticationResult authenticationResult = new AuthenticationResult();

        authenticationResult.setContext("context");




        return authenticationResult;
    }
}
