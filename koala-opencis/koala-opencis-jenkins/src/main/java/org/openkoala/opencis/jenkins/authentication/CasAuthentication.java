package org.openkoala.opencis.jenkins.authentication;

import org.openkoala.opencis.api.AuthenticationResult;
import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 4:57 PM
 */
public class CasAuthentication implements AuthenticationStrategy<WebDriver> {

    private WebDriver driver;

    private String jenkinsURL = null;

    private String username = null;

    private String password = null;

    private CasAuthentication() {
    }

    public CasAuthentication(WebDriver driver, String jenkinsURL, String username, String password) {
        this.driver = driver;
        this.jenkinsURL = jenkinsURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public AuthenticationResult authenticate() {
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