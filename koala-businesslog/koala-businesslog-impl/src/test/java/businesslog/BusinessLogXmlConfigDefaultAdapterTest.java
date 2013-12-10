package businesslog;

import business.ContractApplication;
import business.ContractApplicationImpl;
import business.Project;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.junit.Ignore;
import org.junit.Test;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 3:00 PM
 */
public class BusinessLogXmlConfigDefaultAdapterTest {


    @Test
    public void testName() throws Exception {

        String method = "Invoice business.InvoiceApplication.addInvoice(String,long)";

        BusinessLogConfigAdapter adapter = new BusinessLogXmlConfigDefaultAdapter();

        adapter.findConfigByBusinessOperator(method);

        assert "${user!\"\"}:${ip!\"\"}:".equals(adapter.getPreTemplate());

        assert "向项目${project.name}的合同${contract.name}添加发票：${_methodReturn.sn}"
                .equals(adapter.getTemplate());


    }


}