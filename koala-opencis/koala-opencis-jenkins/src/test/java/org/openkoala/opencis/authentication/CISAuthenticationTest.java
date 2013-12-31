package org.openkoala.opencis.authentication;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.jenkins.HttpCASAuthentication;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

        authentication.setJenkinsAuthenticationUrl(new URL("http", "10.108.1.138", 8080, "/jenkins"));


        assert authentication.authentication();


    }
}
