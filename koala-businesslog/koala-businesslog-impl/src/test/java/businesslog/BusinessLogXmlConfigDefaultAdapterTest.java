package businesslog;

import org.junit.Test;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 3:00 PM
 */
public class BusinessLogXmlConfigDefaultAdapterTest {


    @Test
    public void testName() throws Exception {

        String method = "Invoice business.InvoiceApplicationImpl.addInvoice(String,long)";

        BusinessLogConfigAdapter adapter = new BusinessLogXmlConfigDefaultAdapter();

        adapter.findConfigByBusinessOperation(method);

        assert 3 == adapter.getQueries().size();

        assert "向项目${project.name}的合同${contract.name}添加发票：${(_methodReturn.sn)!\"\"}"
                .equals(adapter.getTemplate());

        assert "发票操作".equals(adapter.getCategory());


    }


}