package businesslog;

import business.ContractApplication;
import business.InvoiceApplication;
import business.ProjectApplication;
import org.junit.Test;

import javax.inject.Inject;


public class BusinessLogInterceptorTestb extends KoalaBaseSpringTestCase {

    @Inject
    private ContractApplication contractApplication;

    @Inject
    private InvoiceApplication invoiceApplication;

    @Inject
    private ProjectApplication projectApplication;

    @Test
    public void test() {



    }

}
