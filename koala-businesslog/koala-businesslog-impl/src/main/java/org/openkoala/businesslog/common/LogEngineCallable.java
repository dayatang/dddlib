package org.openkoala.businesslog.common;

import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * User: zjzhai
 * Date: 12/15/13
 * Time: 5:12 PM
 */
public class LogEngineCallable implements Callable<BusinessLog> {

    private org.openkoala.businesslog.BusinessLogEngine businessLogEngine;

    private org.openkoala.businesslog.BusinessLogExporter businessLogExporter;

    private Map<String, Object> context;

    private String joinPointSignature;

    public LogEngineCallable(final BusinessLogEngine businessLogEngine,
                             final BusinessLogExporter businessLogExporter,
                             final Map<String, Object> context, final String joinPointSignature) {
        this.businessLogEngine = businessLogEngine;
        this.businessLogExporter = businessLogExporter;
        this.context = context;
        this.joinPointSignature = joinPointSignature;
    }

    @Override
    public BusinessLog call() throws Exception {



        businessLogEngine.setInitContext(context);
        BusinessLog log = businessLogEngine.exportLogBy(joinPointSignature, businessLogExporter);
        System.out.println("__________________");
        System.out.println(joinPointSignature);
        System.out.println(log.getContext());
        return log;
    }
}
