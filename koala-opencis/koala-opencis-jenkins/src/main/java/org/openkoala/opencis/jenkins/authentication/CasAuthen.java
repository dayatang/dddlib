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
public class CasAuthen implements CISAuthentication {

    private WebDriver driver;

    private URL jenkinsURL = null;

    private String username = null;

    private String password = null;

    private CasAuthen() {
    }

    public CasAuthen(WebDriver driver, URL jenkinsURL, String username, String password) {
        this.driver = driver;
        this.jenkinsURL = jenkinsURL;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean authenticate() {
        driver.get(jenkinsURL.toString());
        WebElement usernameElement = driver.findElement(By.id("username"));
        usernameElement.sendKeys(username);
        WebElement passwordElement = driver.findElement(By.id("password"));
        passwordElement.sendKeys(password);
        passwordElement.submit();
        return "Dashboard [Jenkins]".equals(driver.getTitle());
    }

    @Override
    public void setAppURL(URL url) {
        jenkinsURL = url;
    }

    @Override
    public Object getContext() {
        return driver;
    }
}