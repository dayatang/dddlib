package org.openkoala.businesslog.common;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.config.BusinessLogConfig;
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

    private org.openkoala.businesslog.BusinessLogEngine businessLogEngine;

    private org.openkoala.businesslog.BusinessLogExporter businessLogExporter;

    private Map<String, Object> context;

    private String joinPointSignature;


    public LogEngineThread(BusinessLogEngine businessLogEngine, BusinessLogExporter businessLogExporter, Map<String, Object> context, String joinPointSignature) {
        this.businessLogEngine = businessLogEngine;
        this.businessLogExporter = businessLogExporter;
        this.context = context;
        this.joinPointSignature = joinPointSignature;
    }

    @Override
    public void run() {

        ThreadLocalBusinessLogContext.put(BUSINESS_METHOD, joinPointSignature);
        businessLogEngine.setInitContext(Collections.unmodifiableMap(context));
        businessLogEngine.exportLogBy(joinPointSignature, businessLogExporter);


    }


}
