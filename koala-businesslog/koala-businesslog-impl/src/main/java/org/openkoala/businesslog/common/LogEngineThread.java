package org.openkoala.businesslog.common;

import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;

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

        businessLogEngine.setInitContext(context);
        BusinessLog log =  businessLogEngine.exportLogBy(joinPointSignature, businessLogExporter);
        System.out.println("__________________");
        System.out.println(log.getContext());
    }
}
