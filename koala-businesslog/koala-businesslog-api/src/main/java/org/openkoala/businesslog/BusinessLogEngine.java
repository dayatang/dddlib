package org.openkoala.businesslog;

import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.config.BusinessLogContextQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 2:21 PM
 */
public class BusinessLogEngine {

    private BusinessLogConfig config;

    private BusinessLogRender render;

    private Map<String, Object> initContext = new HashMap<String, Object>();

    private BusinessLogContextQueryExecutor queryExecutor;

    public BusinessLogEngine(BusinessLogConfig config, BusinessLogRender render, BusinessLogContextQueryExecutor queryExecutor) {
        this.config = config;
        this.queryExecutor = queryExecutor;
        this.render = render;
    }

    public BusinessLogEngine(BusinessLogConfig config, BusinessLogRender render, BusinessLogContextQueryExecutor queryExecutor, Map<String, Object> aContext) {
        this(config, render, queryExecutor);
        initContext = aContext;
    }

    public String exportLogBy(BusinessLogExporter exporter) {
        String log = render();
        exporter.export(log);
        return log;
    }

    private String render() {
        render.render(createContext(), config.getPreTemplate(), config.getTemplate());
        return render.build();
    }

    private Map<String, Object> createContext() {
        if (null == initContext) {
            initContext = new HashMap<String, Object>();
        }
        if (null == config.getQueries()) {
            return initContext;
        }
        BusinessLogContextQuery[] queries = new BusinessLogContextQuery[config.getQueries().size()];
        return queryExecutor.startQuery(initContext, queries);
    }

}
