package org.openkoala.opencis.jenkins.configureImpl.user;

import org.openkoala.opencis.AuthenticationException;
import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.authentication.CISAuthentication;
import org.openkoala.opencis.jenkins.configureApi.UserCreator;
import org.openqa.selenium.WebDriver;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 3:20 PM
 */
public class CreateUser extends UserCreator {

    private CISAuthentication cisAuthentication;

    public CreateUser(String jenkinsUrl, CISAuthentication cisAuthentication) {
        super(jenkinsUrl);
        this.cisAuthentication = cisAuthentication;
    }

    public CreateUser(String jenkinsUrl) {
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
        return false;
    }
}
