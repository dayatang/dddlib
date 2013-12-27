import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.*;
import org.junit.runner.RunWith;
import org.logicalcobwebs.proxool.ProxoolFacade;
import org.openkoala.businesslog.model.DefaultBusinessLog;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.io.IOException;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 8:42 AM
 */
@Ignore
public class MainIntegrationTest {


    protected static WebDriver driver;

    @BeforeClass
    public static void openBrowser() throws InterruptedException, IOException {
        driver = new HtmlUnitDriver();
    }

    @Test
    public void test() throws InterruptedException, IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(getBaseUrl());
        httpclient.execute(httpGet);
        Thread.sleep(5000);
        HttpGet adminGet = new HttpGet(getBaseUrl() + "admin");
        HttpResponse response = httpclient.execute(adminGet);
        String content = EntityUtils.toString(response.getEntity(), "UTF-8");
        content.contains("size:3");
        httpGet.abort();

        httpclient.getConnectionManager().shutdown();


    }

    @AfterClass
    public static void closeBrowser() throws Exception {
        driver.close();
        driver.quit();
    }

    @After
    public void tearDown() throws Exception {
        //  ProxoolFacade.shutdown(0);
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
