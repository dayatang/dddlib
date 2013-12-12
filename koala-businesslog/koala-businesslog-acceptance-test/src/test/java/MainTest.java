
import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 8:42 AM
 */
@Ignore
public class MainTest {


    protected static WebDriver driver;

    /*@BeforeClass
    public static void openBrowser() throws InterruptedException, IOException {
        System.out.println("openBrowser");
        driver = new HtmlUnitDriver();
        driver.get(getBaseUrl());

        driver.manage().timeouts().implicitlyWait(9000, TimeUnit.MILLISECONDS);

    }

    @Test
    public void test() {

        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());
        System.out.println(driver.getPageSource());
    }

    @AfterClass
    public static void closeBrowser() throws Exception {
        driver.close();
        driver.quit();
    }

    @After
    public void tearDown() throws Exception {

    }            */

    protected static String getBaseUrl() throws IOException {
       /* Properties properties = new Properties();
        properties.load(new FileInputStream(String.valueOf(MainTest.class.getResource("browser.properties"))));
        String host = (String)properties.get("host");
        String port = (String)properties.get("port");
        String context = (String)properties.get("context");*/
        return "http://localhost:6699/";
    }


}
