package org.openkoala.opencis.jenkins.util;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * User: zjzhai
 * Date: 1/7/14
 * Time: 6:03 PM
 */
public class SeleniumUtil {
    /**
     * 点击空白区域：坐标（0，0）
     */

    public static void clickBlankArea(WebDriver driver) {
        Actions actions = new Actions(driver);
        actions.doubleClick();
    }

    /**
     * 某元素是否存在
     *
     * @param driver
     * @param locator
     * @return
     */
    public static boolean elementExist(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (org.openqa.selenium.NoSuchElementException ex) {
            return false;
        }
    }

}
