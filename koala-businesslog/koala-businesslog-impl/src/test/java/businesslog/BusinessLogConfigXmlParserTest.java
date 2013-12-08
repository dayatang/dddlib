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

        assert "${user}:${ip}:${time!Date}".equals(parser.getPreTemplate());

        String operator = "String business.ContractApplication.addInvoice(String,long,long)";

        assert "合同添加入项目${project.name}".equals(parser.getTemplateFrom(operator));

        assert parser.getQueriesFrom(operator).size() == 2;

        assert parser.getQueriesFrom(operator).get(0) instanceof BusinessLogDefaultContextQuery;

        BusinessLogDefaultContextQuery query = (BusinessLogDefaultContextQuery)
                parser.getQueriesFrom(operator).get(0);

        assert "business.ProjectApplication".equals(query.getBeanClassName());
        assert "projectApplication".equals(query.getBeanName());

        BusinessLogDefaultContextQuery query2 = (BusinessLogDefaultContextQuery) parser.getQueriesFrom(operator).get(1);

        assert "business.ContractApplication".equals(query2.getBeanClassName());
        assert "".equals(query2.getBeanName());
        assert "findByContractIdAndProject(long,business.Project)".equals(query2.getMethodSignature());
        assert "xx".equals(query2.getArgs().get(0));
        assert "${project}".equals(query2.getArgs().get(1));


        String operator1 = "String business.ContractApplication.addContract(long)";

        assert "添加合同${methodReturn}".equals(parser.getTemplateFrom(operator1));



    }
}
