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

    public BusinessLogEngine(BusinessLogConfig config,
                             BusinessLogRender render,
                             BusinessLogContextQueryExecutor queryExecutor,
                             Map<String, Object> aContext) {
        this(config, render, queryExecutor);
        initContext = aContext;
    }

    public String exportLogBy(String businessOperator, BusinessLogExporter exporter) {
        String log = render(businessOperator);
        exporter.export(log);
        return log;
    }

    private String render(String businessOperator) {
        render.render(createContext(businessOperator), config.getPreTemplate(), config.getLogTemplateof(businessOperator));
        return render.build();
    }

    private Map<String, Object> createContext(String businessOperator) {
        if (null == initContext) {
            initContext = new HashMap<String, Object>();
        }
        BusinessLogContextQuery[] queries = new BusinessLogContextQuery[config.getQueries(businessOperator).size()];
        return queryExecutor.startQuery(initContext, config.getQueries(businessOperator).toArray(queries));
    }

    public void setRender(BusinessLogRender render) {
        this.render = render;
    }

    public void setConfig(BusinessLogConfig config) {
        this.config = config;
    }

    public void setQueryExecutor(BusinessLogContextQueryExecutor queryExecutor) {
        this.queryExecutor = queryExecutor;
    }

    public void setInitContext(Map<String, Object> initContext) {
        this.initContext = initContext;
    }
}
