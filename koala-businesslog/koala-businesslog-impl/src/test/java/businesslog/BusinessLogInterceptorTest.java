package businesslog;

import business.ContractApplication;
import business.InvoiceApplication;
import business.ProjectApplication;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.openkoala.businesslog.utils.BusinessLogInterceptor;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class BusinessLogInterceptorTest extends AbstractIntegrationTest {

    @Inject
    private ContractApplication contractApplication;

    @Inject
    private InvoiceApplication invoiceApplication;

    @Inject
    private ProjectApplication projectApplication;

    @Test
    public void test() {
        ThreadLocalBusinessLogContext.put("user", "张三");
        ThreadLocalBusinessLogContext.put("time", new Date());
        ThreadLocalBusinessLogContext.put("ip", "202.11.22.33");


        invoiceApplication.addInvoice("发票编号", 1l);


    }

    @Test
    public void testFindProjects() {

        List<String> names = new ArrayList<String>();

        names.add("1");
        names.add("2");
        names.add("3");
        names.add("4");

        projectApplication.findSomeProjects(names);


    }


}
