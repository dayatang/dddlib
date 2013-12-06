package businesslog;

import org.junit.Test;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQuery;
import org.openkoala.businesslog.utils.BusinessLogConfigXmlParser;

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

        assert "${user}:${ip}:${time!Date}".equals(parser.findPreTemplate());

        String operator = "String business.ContractApplication.addInvoice(String,long,long)";

        assert "合同添加入项目${project.name}".equals(parser.findTemplateFrom(operator));

        assert parser.findQueriesFrom(operator).size() == 2;

        assert parser.findQueriesFrom(operator).get(0) instanceof BusinessLogDefaultContextQuery;

    }
}
