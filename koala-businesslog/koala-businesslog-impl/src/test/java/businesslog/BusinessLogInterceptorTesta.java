package businesslog;

import business.ContractApplication;
import business.InvoiceApplication;
import business.ProjectApplication;
import org.junit.Test;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BusinessLogInterceptorTesta extends AbstractIntegrationTest {

    @Inject
    private ContractApplication contractApplication;

    @Inject
    private InvoiceApplication invoiceApplication;

    @Inject
    private ProjectApplication projectApplication;

    @Test
    public void test() {

        invoiceApplication.addInvoice("bianhao-1111", 2);


    }

}
