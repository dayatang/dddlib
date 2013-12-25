package org.openkoala.businesslog.common;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import org.openkoala.businesslog.*;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor;
import org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender;
import org.openkoala.businesslog.impl.BusinessLogJpaDefaultExporter;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import java.util.Collections;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/15/13
 * Time: 6:45 PM
 */
public class LogEngineThread implements Runnable {


    private Map<String, Object> context;

    private String joinPointSignature;

    private BusinessLogConfigAdapter configAdapter;

    private BusinessLogRender render;

    private org.openkoala.businesslog.BusinessLogExporter businessLogExporter;

    private BusinessLogContextQueryExecutor queryExecutor;

    public LogEngineThread(Map<String, Object> context, String joinPointSignature, BusinessLogConfigAdapter configAdapter, BusinessLogRender render, BusinessLogExporter businessLogExporter, BusinessLogContextQueryExecutor queryExecutor) {
        this.context = context;
        this.joinPointSignature = joinPointSignature;
        this.configAdapter = configAdapter;
        this.render = render;
        this.businessLogExporter = businessLogExporter;
        this.queryExecutor = queryExecutor;
    }

    @Override
    public void run() {
        ThreadLocalBusinessLogContext.put(BUSINESS_METHOD, joinPointSignature);
        BusinessLogEngine businessLogEngine = new BusinessLogEngine(configAdapter, render, queryExecutor);
        businessLogEngine.setInitContext(Collections.unmodifiableMap(context));
        businessLogEngine.exportLogBy(joinPointSignature, businessLogExporter);
    }


}
