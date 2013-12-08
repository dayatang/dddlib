package businesslog;

import business.Project;
import org.junit.Test;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 8:49 PM
 */
public class BusinessLogDefaultContextQueryTest {

    @Test
    public void testName() throws Exception {
        String queryContextKey = "project";


        BusinessLogDefaultContextQuery query = new BusinessLogDefaultContextQuery();
        query.setBeanClassName("business.ContractApplicationImpl");
        query.setMethodSignature("findByContractIdAndProject(long,business.Project)");
        query.setArgs(getArgs());
        query.setContextKey(queryContextKey);

        Map<String, Object> queryResult = query.queryInContext(createContext());

        assert "项目11".equals(((Project)queryResult.get(queryContextKey)).getName());

    }

    private List<String> getArgs() {
        List<String> result = new ArrayList<String>();
        result.add("1");
        result.add("${project}");
        return result;
    }

    private Map<String, Object> createContext() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("project", new Project("projectA"));
        return result;
    }

    @Test
    public void testdd() throws Exception {
        System.out.println(Class.forName("org.openkoala.businesslog.common.MethodProcesser").getClass());


    }
}
