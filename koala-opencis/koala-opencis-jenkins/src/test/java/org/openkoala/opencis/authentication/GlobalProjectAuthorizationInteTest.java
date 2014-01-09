package org.openkoala.opencis.authentication;

import org.junit.Test;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.api.Project;
import org.openkoala.opencis.jenkins.authentication.CasAuthen;
import org.openkoala.opencis.jenkins.configureImpl.authorize.GlobalProjectAuthorization;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 11:52 PM
 */
public class GlobalProjectAuthorizationInteTest extends CISClientAbstactIntegrationTest {


    @Test
    public void testName() throws Exception {
        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        GlobalProjectAuthorization cisAuthorization =
                new GlobalProjectAuthorization(jenkinsUrl.toString());
        cisAuthorization.authorize(getDeveloper(), driver);
    }


}
