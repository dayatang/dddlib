package org.openkoala.businesslog.utils;

import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.AbstractBusinessLogRender;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;

import javax.inject.Inject;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: zjzhai
 * Date: 11/28/13
 * Time: 11:38 AM
 */
public class BusinessLogInterceptor {

    private static Logger logger = Logger.getLogger(BusinessLogInterceptor.class.toString());

    /**
     * 业务方法返回值，在模板中使用的key
     */
    public final static String BUSINESS_METHOD_RETURN_VALUE_KEY = "methodReturn";

    @Inject
    private org.openkoala.businesslog.BusinessLogExporter businessLogExporter;


    @Inject
    private BusinessLogEngine businessLogEngine;


    public void logAfter(JoinPoint joinPoint, Object result) {


        System.out.println(joinPoint.getSignature());
        Map<String, Object> context = createDefaultContext(joinPoint, result);



    }

    private Map<String, Object> createDefaultContext(JoinPoint joinPoint, Object result) {

        Map<String, Object> context = ThreadLocalBusinessLogContext.get();

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.put(i + "", args[i]);
        }

        context.put(BUSINESS_METHOD_RETURN_VALUE_KEY, result);
        return context;
    }

    private BusinessLogEngine getBusinessLogEngine() {
        if (businessLogEngine == null) {
            BusinessLogXmlConfigDefaultAdapter adapter = new BusinessLogXmlConfigDefaultAdapter();
            //businessLogEngine = new BusinessLogEngine()

        }
        return businessLogEngine;
    }


}
