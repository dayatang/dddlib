package businesslog;

import business.ContractApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.openkoala.businesslog.utils.BusinessLogInterceptor;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:context.xml"})
public class LogGeneratorTest extends AbstractJUnit4SpringContextTests {

    @Inject
    private ContractApplication contractApplication;

    @Test
    public void test() {
        ThreadLocalBusinessLogContext.put("user", "张三");
        ThreadLocalBusinessLogContext.put("time", new Date());
        ThreadLocalBusinessLogContext.put("ip", "202.11.22.33");
        contractApplication.addInvoice("项目XXX", 1l, 2l);

        Map<String, Object> context = ThreadLocalBusinessLogContext.get();


        assert "项目XXX".equals(context.get("0"));
        assert new Long(1).equals(context.get("1"));
        assert new Long(2).equals(context.get("2"));
        assert "K-8999".equals(context.get(BusinessLogInterceptor.BUSINESS_METHOD_RETURN_VALUE_KEY));
        assert "张三".equals(context.get("user"));





    }


}
