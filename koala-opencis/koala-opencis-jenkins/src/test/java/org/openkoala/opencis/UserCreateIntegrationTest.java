package org.openkoala.opencis;

import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.opencis.jenkins.configureImpl.user.UserCreator;
import org.openqa.selenium.WebDriver;

/**
 * User: zjzhai
 * Date: 1/9/14
 * Time: 3:15 PM
 */
@Ignore
public class UserCreateIntegrationTest extends CISClientAbstactIntegrationTest {

    @Test
    public void testName() throws Exception {
        UserCreator creator = new UserCreator();
        creator.createUser(jenkinsURL, getDeveloper("xxx"), ownAuthenticationAndCreateWebDriver());

        System.out.println(creator.getError());
    }
}
