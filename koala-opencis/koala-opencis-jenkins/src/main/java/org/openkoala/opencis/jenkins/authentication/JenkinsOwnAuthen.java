package org.openkoala.opencis.jenkins.authentication;

import org.openkoala.opencis.authentication.CISAuthentication;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 4:57 PM
 */
public class JenkinsOwnAuthen implements CISAuthentication {

    private WebDriver driver;

    private String jenkinsURL = null;

    private String username;

    private String password;

    private JenkinsOwnAuthen() {
    }

    public JenkinsOwnAuthen(WebDriver driver, String jenkinsURL, String username, String password) {
        this.driver = driver;
        this.jenkinsURL = jenkinsURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean authenticate() {
        driver.get(jenkinsURL);
        WebElement usernameElement = driver.findElement(By.cssSelector("input[name=\"j_username\"]"));
        usernameElement.sendKeys(username);
        WebElement passwordElement = driver.findElement(By.name("j_password"));
        passwordElement.sendKeys(password);
        passwordElement.submit();
        return "Dashboard [Jenkins]".equals(driver.getTitle());
    }

    @Override
    public void setAppURL(URL url) {
        jenkinsURL = url.toString();
    }

    @Override
    public Object getContext() {
        return driver;
    }
}