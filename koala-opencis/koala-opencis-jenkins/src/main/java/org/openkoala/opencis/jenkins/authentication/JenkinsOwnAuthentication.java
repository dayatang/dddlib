package org.openkoala.opencis.jenkins.authentication;

import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;


/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 4:57 PM
 */
public class JenkinsOwnAuthentication implements AuthenticationStrategy{

    private WebDriver driver;

    private String jenkinsURL = null;

    private String username;

    private String password;

    private JenkinsOwnAuthentication() {
    }

    public JenkinsOwnAuthentication(WebDriver driver, String jenkinsURL, String username, String password) {
        this(jenkinsURL, username, password);
        this.driver = driver;
    }

    public JenkinsOwnAuthentication(String jenkinsURL, String username, String password) {
        this.jenkinsURL = jenkinsURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public AuthenticationResult authenticate() {
        if (driver == null) {
            driver = new HtmlUnitDriver(true);
            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
        }
        driver.get(jenkinsURL);
        WebElement usernameElement = driver.findElement(By.name("j_username"));
        usernameElement.sendKeys(username);
        WebElement passwordElement = driver.findElement(By.name("j_password"));
        passwordElement.sendKeys(password);
        passwordElement.submit();
        AuthenticationResult authenticationResult = new AuthenticationResult();

        if ("Dashboard [Jenkins]".equals(driver.getTitle())) {
            authenticationResult.setContext(driver);
        } else {
            authenticationResult.setErrors("authentication failure!");
        }
        return authenticationResult;
    }
}