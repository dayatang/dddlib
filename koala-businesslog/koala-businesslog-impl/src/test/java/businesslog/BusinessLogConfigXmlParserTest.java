package businesslog;

import org.junit.Test;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQuery;
import org.openkoala.businesslog.common.BusinessLogConfigXmlParser;

/**
 * User: zjzhai
 * Date: 12/6/13
 * Time: 3:02 PM
 */
public class BusinessLogConfigXmlParserTest {


    @Test
    public void testName() throws Exception {

        String xmlconfigPath = "src/test/resources/koala-businesslog-config.xml";

        BusinessLogConfigXmlParser parser = BusinessLogConfigXmlParser.parsing(xmlconfigPath);

        String operation = "Invoice business.InvoiceApplication.addInvoice(String,long)";

        assert "向项目${project.name}的合同${contract.name}添加发票：${(_methodReturn.sn)!\"\"}".equals(parser.getTemplateFrom(operation));

        assert parser.getQueriesFrom(operation).size() == 3;

        assert parser.getQueriesFrom(operation).get(0) instanceof BusinessLogDefaultContextQuery;

        BusinessLogDefaultContextQuery query = (BusinessLogDefaultContextQuery)
                parser.getQueriesFrom(operation).get(0);

        assert "business.ContractApplication".equals(query.getBeanClassName());
        assert "contractApplication".equals(query.getBeanName());
        assert "${_param1}".equals(query.getArgs().get(0));

        BusinessLogDefaultContextQuery query2 = (BusinessLogDefaultContextQuery) parser.getQueriesFrom(operation).get(1);

        assert "business.Project".equals(query2.getBeanClassName());
        assert "".equals(query2.getBeanName());
        assert "findByContract(business.Contract)".equals(query2.getMethodSignature());
        assert "${contract}".equals(query2.getArgs().get(0));
        assert "发票操作".equals(parser.getBusinessLogMethodCategory(operation));


        String operation1 = "String business.ContractApplication.addContract(long)";

        assert "添加合同${_methodReturn}".equals(parser.getTemplateFrom(operation1));
        assert "合同操作".equals(parser.getBusinessLogMethodCategory(operation1));

    }
}
