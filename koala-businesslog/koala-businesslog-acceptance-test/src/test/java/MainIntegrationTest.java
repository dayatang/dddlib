import org.junit.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 8:42 AM
 */
public class MainIntegrationTest {


    protected static WebDriver driver;

    @BeforeClass
    public static void openBrowser() throws InterruptedException, IOException {
        System.out.println("openBrowser");
        driver = new HtmlUnitDriver();
        //

    }

    @Test
    public void test() throws InterruptedException, IOException  {
        driver.get(getBaseUrl());
        driver.get(getBaseUrl() + "admin");
        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getPageSource());
        driver.findElement(By.id("size")).getText().equals("size:3");
    }

    @AfterClass
    public static void closeBrowser() throws Exception {
        driver.close();
        driver.quit();
    }

    @After
    public void tearDown() throws Exception {

    }

    protected static String getBaseUrl() throws IOException {
       /* Properties properties = new Properties();
        properties.load(new FileInputStream(String.valueOf(MainTest.class.getResource("browser.properties"))));
        String host = (String)properties.get("host");
        String port = (String)properties.get("port");
        String context = (String)properties.get("context");*/
        return "http://localhost:6699/";
    }


}
