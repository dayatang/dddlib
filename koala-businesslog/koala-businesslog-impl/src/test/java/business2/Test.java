package business2;

import business.TestApplication;
import businesslog.AbstractIntegrationTest;
import com.dayatang.domain.InstanceFactory;

import javax.inject.Inject;

/**
 * User: zjzhai
 * Date: 12/16/13
 * Time: 11:18 AM
 */
public class Test extends AbstractIntegrationTest {
    @Inject
    private TestApplication testApplication;

    @org.junit.Test
    public void testName() throws Exception {
        assert InstanceFactory.isInitialized();
        testApplication.testLogMethod(1);

    }

}
