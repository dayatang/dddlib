package org.openkoala.opencis.jenkins.authentication;

import org.openkoala.opencis.api.AuthenticationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 4:57 PM
 */
public class JenkinsOwnAuthentication implements AuthenticationStrategy {

    private WebDriver driver;

    private String jenkinsURL = null;

    private String username;

    private String password;

    private String errors;

    private JenkinsOwnAuthentication() {
    }

    public JenkinsOwnAuthentication(WebDriver driver, String jenkinsURL, String username, String password) {
        this.driver = driver;
        this.jenkinsURL = jenkinsURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean authenticate() {
        driver.get(jenkinsURL);
        WebElement usernameElement = driver.findElement(By.name("j_username"));
        usernameElement.sendKeys(username);
        WebElement passwordElement = driver.findElement(By.name("j_password"));
        passwordElement.sendKeys(password);
        passwordElement.submit();
        return "Dashboard [Jenkins]".equals(driver.getTitle());
    }

    @Override
    public Object getContext() {
        return driver;
    }

    @Override
    public String getErrors() {
        return errors;
    }
}