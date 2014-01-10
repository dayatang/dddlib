package org.openkoala.opencis.authentication;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.CISClientAbstactIntegrationTest;
import org.openkoala.opencis.jenkins.configureImpl.user.GlobalProjectAuthorization;
import org.openqa.selenium.WebDriver;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 11:52 PM
 */
@Ignore
public class GlobalProjectAuthorizationInteTest extends CISClientAbstactIntegrationTest {


    @Test
    public void testName() throws Exception {
        WebDriver driver = ownAuthenticationAndCreateWebDriver();
        GlobalProjectAuthorization cisAuthorization =
                new GlobalProjectAuthorization(jenkinsUrl.toString());
        cisAuthorization.authorize(getDeveloper(), driver);
    }


}
