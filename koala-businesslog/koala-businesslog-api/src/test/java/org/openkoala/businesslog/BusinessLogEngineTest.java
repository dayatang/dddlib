package org.openkoala.businesslog;

import org.junit.Test;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 10:00 AM
 */
public class BusinessLogEngineTest {
    String template = "参数${_param0},参数${_params1},返回值:${_operatorReturn},关联查询值：${invoice}";

    @Test
    public void testSomeThing() {

        String resultLog = "${user}:${time}:${ip}参数1,参数String,返回值:xxxxxx,关联查询值：发票编号1";

        String businessOperation = "void org.openkoala.business.method(int, String)";

        String businessMethodCategory = "发票操作";

        BusinessLogConfigAdapter configAdapter = mock(BusinessLogConfigAdapter.class);

        BusinessLogRender render = mock(BusinessLogRender.class);

        BusinessLogExporter exporter = mock(BusinessLogExporter.class);

        BusinessLogContextQueryExecutor queryExecutor = mock(BusinessLogContextQueryExecutor.class);

        Map<String, Object> initContext = createContext();

        BusinessLogEngine engine =
                new BusinessLogEngine(configAdapter, render, queryExecutor);
        engine.setInitContext(initContext);

        BusinessLogConfig config = getConfig();

        when(configAdapter.findConfigBy(businessOperation)).thenReturn(config);


        when(queryExecutor.startQuery(initContext,
                config.getQueries().toArray(new BusinessLogContextQuery[config.getQueries().size()])))
                .thenReturn(initContext);

        when(render.render(initContext, template)).thenReturn(resultLog);


        BusinessLog businessLog = engine.exportLogBy(businessOperation, exporter);

        assert businessMethodCategory.equals(businessLog.getCategory());
        assert resultLog.equals(businessLog.getLog());
    }

    private BusinessLogConfig getConfig() {
        BusinessLogConfig config = new BusinessLogConfig();

        config.setCategory("发票操作");
        config.setTemplate(template);
        config.setQueries(new ArrayList<BusinessLogContextQuery>());

        return config;
    }


    private Map<String, Object> createInitContext() {
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("user", "zhangsan");
        result.put("time", "2012-1-1");
        result.put("ip", "192.168.1.1");
        result.put("_param0", 1);
        result.put("_params1", "string");
        result.put("_operatorReturn", "xxxxxx");
        return result;
    }

    private Map<String, Object> createContext() {
        Map<String, Object> queryResult = new HashMap<String, Object>();
        queryResult.put("invoice", "发票编号1");
        queryResult.put("project", "项目1");
        return queryResult;

    }

    private List<BusinessLogContextQuery> createQuerys(Map<String, Object> initContext) {
        BusinessLogContextQuery query = mock(BusinessLogContextQuery.class);
        Map<String, Object> queryResult = new HashMap<String, Object>();
        queryResult.put("invoice", "发票编号1");
        when(query.queryInContext(initContext)).thenReturn(queryResult);

        BusinessLogContextQuery query1 = mock(BusinessLogContextQuery.class);
        Map<String, Object> queryResult1 = new HashMap<String, Object>();
        queryResult1.put("project", "项目1");
        when(query1.queryInContext(initContext)).thenReturn(queryResult1);

        List<BusinessLogContextQuery> results = new ArrayList<BusinessLogContextQuery>();
        results.add(query);
        results.add(query1);
        return results;
    }
}
