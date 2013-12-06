package org.openkoala.businesslog;

import org.junit.Test;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 10:00 AM
 */
public class BusinessLogEngineTest {
    @Test
    public void testSomeThing() {
        String template = "参数${_param0},参数${_params1},返回值:${_operatorReturn},关联查询值：${invoice}";
        String preTemplate = "${user}:${time}:${ip}";
        String resultLog = "${user}:${time}:${ip}参数1,参数String,返回值:xxxxxx,关联查询值：发票编号1";

        String businessOperator = "void org.openkoala.business.method(int, String)";

        BusinessLogConfigAdapter configAdapter = mock(BusinessLogConfigAdapter.class);

        BusinessLogConfig config =
                BusinessLogConfig.createByAdapterAndBusinessOperator(configAdapter, businessOperator);

        BusinessLogRender render = mock(BusinessLogRender.class);

        BusinessLogExporter exporter = mock(BusinessLogExporter.class);

        BusinessLogContextQueryExecutor executor = mock(BusinessLogContextQueryExecutor.class);

        BusinessLogEngine engine =
                new BusinessLogEngine(config, render, executor, createInitContext());


        when(configAdapter.findConfigByBusinessOperator(businessOperator)).thenReturn(configAdapter);
        when(configAdapter.getQueries()).thenReturn(new ArrayList<BusinessLogContextQuery>());
        when(configAdapter.getPreTemplate()).thenReturn(preTemplate);
        when(configAdapter.getTemplate()).thenReturn(template);
        when(render.render(createInitContext(), preTemplate, template)).thenReturn(render);
        when(render.build()).thenReturn(resultLog);


        String exlog = engine.exportLogBy(exporter);

        assert resultLog.equals(exlog);
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

   /* private List<BusinessLogContextQuery> createQuerys() {
        BusinessLogContextQuery query = mock(BusinessLogContextQuery.class);
        Map<String, Object> queryResult = new HashMap<String, Object>();
        queryResult.put("invoice", new Invoice("发票编号1"));
        when(query.query()).thenReturn(queryResult);

        BusinessLogContextQuery query1 = mock(BusinessLogContextQuery.class);
        Map<String, Object> queryResult1 = new HashMap<String, Object>();
        queryResult1.put("project", new Project("项目1"));
        when(query1.query()).thenReturn(queryResult1);

        List<BusinessLogContextQuery> results = new ArrayList<BusinessLogContextQuery>();
        results.add(query);
        results.add(query1);
        return results;
    }*/
}
