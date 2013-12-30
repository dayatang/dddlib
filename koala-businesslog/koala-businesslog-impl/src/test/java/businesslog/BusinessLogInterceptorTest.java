package businesslog;

import business.*;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.InstanceProvider;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openkoala.businesslog.model.DefaultBusinessLog;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.openkoala.businesslog.utils.BusinessLogInterceptor;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Ignore
public class BusinessLogInterceptorTest extends AbstractIntegrationTest {

    @Inject
    private ContractApplication contractApplication;

    @Inject
    private InvoiceApplication invoiceApplication;

    @Inject
    private ProjectApplication projectApplication;

    @Test
    public void testFindProjects() throws ClassNotFoundException, InterruptedException {

        ThreadLocalBusinessLogContext.put("user", "张三");
        ThreadLocalBusinessLogContext.put("time", new Date());
        ThreadLocalBusinessLogContext.put("ip", "202.11.22.33");


        invoiceApplication.addInvoice("发票编号", 1l);

        invoiceApplication.addInvoice("发票编号2", 22l);

        List<String> names = new ArrayList<String>();

        names.add("1");
        names.add("2");
        names.add("3");
        names.add("4");

        projectApplication.findSomeProjects(names);

        System.out.println(DefaultBusinessLog.findAll(DefaultBusinessLog.class).size() + "===========");

        assert DefaultBusinessLog.findAll(DefaultBusinessLog.class).size() == 3;


    }

}
