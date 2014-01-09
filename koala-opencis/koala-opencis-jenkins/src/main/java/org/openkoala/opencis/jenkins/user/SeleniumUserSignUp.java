package org.openkoala.opencis.jenkins.user;

import org.openkoala.opencis.api.Developer;
import org.openkoala.opencis.jenkins.util.SeleniumUtil;
import org.openkoala.opencis.jenkins.util.UrlUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 1/8/14
 * Time: 2:48 PM
 */
public class SeleniumUserSignUp extends UserCreator {


    public SeleniumUserSignUp(String jenkinsUrl) {
        super(jenkinsUrl);
    }

    public static SeleniumUserSignUp create(String jenkinsUrl) {
        return new SeleniumUserSignUp(jenkinsUrl);
    }


    @Override
    public boolean createUser(Developer developer) {
        WebDriver driver = new HtmlUnitDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get(getJenkinsUrl() + "/signup");

        WebElement usernameInput = driver.findElement(By.name("username"));
        usernameInput.sendKeys(getUsername());
        WebElement passwordInput1 = driver.findElement(By.name("password1"));
        passwordInput1.sendKeys(getPassword());

        WebElement passwordInput2 = driver.findElement(By.name("password2"));
        passwordInput2.sendKeys(getPassword());

        WebElement fullnameInput = driver.findElement(By.name("fullname"));
        fullnameInput.sendKeys(getFullname());

        WebElement emailInput = driver.findElement(By.name("email"));
        emailInput.sendKeys(getEmail());

        emailInput.submit();

        if (SeleniumUtil.elementExist(driver, By.cssSelector("div.error"))) {
            setError(driver.findElement(By.cssSelector("div.error")).getText());
            driver.quit();
            return false;
        }

        driver.quit();
        return true;

    }


}
