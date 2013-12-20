package businesslog;

import org.junit.Test;
import org.openkoala.businesslog.common.BusinessLogConfigPathProcessor;

/**
 * User: zjzhai
 * Date: 12/19/13
 * Time: 10:00 AM
 */
public class BusinessLogConfigPathProcessorTest {

    private static final String configPath = "koala-log-conf";

    @Test
    public void testName() throws Exception {



        assert BusinessLogConfigPathProcessor.getAllConfigFiles().size() == 4;


    }
}
