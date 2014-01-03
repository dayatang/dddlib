package org.openkoala.opencis.authentication;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.jenkins.HttpCASAuthentication;

import java.net.URL;

/**
 * User: zjzhai
 * Date: 12/29/13
 * Time: 10:22 AM
 */
@Ignore

public class CISAuthenticationTest {

    @Test
    public void testName() throws Exception {

        HttpCASAuthentication authentication = new HttpCASAuthentication(
                new URL("http", "10.108.1.138", 8080, "/cas/v1/tickets/"),
                "admin",
                "admin");

        authentication.setJenkinsAuthenticationUrl(new URL("http", "10.108.1.138", 8080, "/ci/jenkins"));


        assert authentication.authentication();


    }
}
