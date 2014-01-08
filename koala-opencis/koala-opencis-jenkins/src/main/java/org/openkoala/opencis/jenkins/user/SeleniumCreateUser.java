package org.openkoala.opencis.jenkins.user;

import org.openkoala.opencis.AuthenticationException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.authentication.CISAuthentication;
import org.openqa.selenium.WebDriver;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 3:20 PM
 */
public class SeleniumCreateUser extends UserCreator {

    private CISAuthentication cisAuthentication;

    public SeleniumCreateUser(String jenkinsUrl, CISAuthentication cisAuthentication) {
        super(jenkinsUrl);
        this.cisAuthentication = cisAuthentication;
    }

    public SeleniumCreateUser(String jenkinsUrl) {
        super(jenkinsUrl);
    }

    @Override
    public boolean createUser(Developer developer) {
        WebDriver driver;
        if (cisAuthentication.authenticate()) {
            driver = (WebDriver)cisAuthentication.getContext();
        }else{
            throw new AuthenticationException("create user authentication failure");
        }


        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
