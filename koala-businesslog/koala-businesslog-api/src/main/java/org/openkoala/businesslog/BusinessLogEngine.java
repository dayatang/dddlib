package org.openkoala.businesslog;

import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 2:21 PM
 */
public class BusinessLogEngine {

    private BusinessLogConfigAdapter configAdapter;

    private BusinessLogRender render;

    private Map<String, Object> initContext;

    private BusinessLogContextQueryExecutor queryExecutor;

    public BusinessLogEngine(BusinessLogConfigAdapter configAdapter, BusinessLogRender render, BusinessLogContextQueryExecutor queryExecutor) {
        this.configAdapter = configAdapter;
        this.queryExecutor = queryExecutor;
        this.render = render;
    }

    public synchronized BusinessLog exportLogBy(String businessOperation, BusinessLogExporter exporter) {
            BusinessLogConfig config = configAdapter.findConfigBy(businessOperation);
            if (null == config) {
                return null;
            }
            BusinessLog businessLog = new BusinessLog();
            Map<String, Object> context = createContext(config);
            String template = config.getTemplate();
            businessLog.setLog(render.render(context, template).build());
            businessLog.setCategory(config.getCategory());
            businessLog.addContext(context);
            exporter.export(businessLog);

            return businessLog;
    }


    private synchronized Map<String, Object> createContext(BusinessLogConfig config) {
        if (null == initContext) {
            initContext = new Hashtable<String, Object>();
        }
        List<BusinessLogContextQuery> list = config.getQueries();
        BusinessLogContextQuery[] queries = new BusinessLogContextQuery[list.size()];
        return queryExecutor.startQuery(initContext, list.toArray(queries));
    }

    public void setRender(BusinessLogRender render) {
        this.render = render;
    }

    public void setQueryExecutor(BusinessLogContextQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public void setConfigAdapter(BusinessLogConfigAdapter configAdapter) {
        this.configAdapter = configAdapter;
    }

    public synchronized void setInitContext(Map<String, Object> initContext) {
        this.initContext = initContext;
    }
}
