import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.*;

import java.io.IOException;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 8:42 AM
 */
@Ignore
public class MainIntegrationTest {


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
