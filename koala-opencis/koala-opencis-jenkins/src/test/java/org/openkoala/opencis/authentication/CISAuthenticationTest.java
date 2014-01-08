package org.openkoala.opencis.authentication;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.jenkins.authentication.casAuthen.HttpAuthen;

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

        HttpClient httpClient = HttpClients.createDefault();
        HttpAuthen authentication = new HttpAuthen(httpClient,
                new URL("http", "10.108.1.138", 8080, "/cas/v1/tickets/"),
                new URL("http", "10.108.1.138", 8080, "/ci/jenkins"),
                "admin",
                "admin");


        assert authentication.authenticate();


    }
}
