package businesslog;

import org.junit.Test;
import org.openkoala.businesslog.common.BusinessLogPropertiesConfig;

/**
 * User: zjzhai
 * Date: 12/12/13
 * Time: 3:10 PM
 */
public class BusinessLogPropertiesConfigParserTest {


    @Test
    public void testName() throws Exception {
        BusinessLogPropertiesConfig parser = BusinessLogPropertiesConfig.getInstance();

        assert true == parser.getLogEnableConfig();

    }
}
