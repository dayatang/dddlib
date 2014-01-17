package org.openkoala.opencis.jenkins.authentication;

import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 4:57 PM
 */
public class CasAuthentication implements AuthenticationStrategy {

    private WebDriver driver;

    private String jenkinsURL = null;

    private String username = null;

    private String password = null;

    private CasAuthentication() {
    }

    /**
     * 用于方便测试使用
     *
     * @param driver
     * @param jenkinsURL
     * @param username
     * @param password
     */
    public CasAuthentication(WebDriver driver, String jenkinsURL, String username, String password) {
        this(jenkinsURL, username, password);
        this.driver = driver;
    }

    public CasAuthentication(String jenkinsURL, String username, String password) {
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
        WebElement usernameElement = driver.findElement(By.id("username"));
        usernameElement.sendKeys(username);
        WebElement passwordElement = driver.findElement(By.id("password"));
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