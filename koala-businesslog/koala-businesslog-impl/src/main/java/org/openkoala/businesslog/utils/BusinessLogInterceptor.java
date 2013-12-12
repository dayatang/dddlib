package org.openkoala.businesslog.utils;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor;
import org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;

import javax.inject.Inject;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: zjzhai
 * Date: 11/28/13
 * Time: 11:38 AM
 */
public class BusinessLogInterceptor {

    private static Logger logger = Logger.getLogger(BusinessLogInterceptor.class.toString());

    private final String BUSINESS_LOG_CONFIG_PROPERTIES_NAME = "koala-busniesslog.properties";

    private final String LOG_ENABLE = "kaola-businesslog.enable";

    @Inject
    private BusinessLogEngine businessLogEngine;

    @Inject
    private BusinessLogExporter businessLogExporter;


    public void logAfter(JoinPoint joinPoint, Object result) {
        log(joinPoint, result, null);
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable error) {
        log(joinPoint, null, error);

    }

    public void log(JoinPoint joinPoint, Object result, Throwable error) {
        try {
            PropertiesConfiguration configuration = new PropertiesConfiguration(BUSINESS_LOG_CONFIG_PROPERTIES_NAME);
            if (!configuration.getBoolean(LOG_ENABLE, true)) {
                return;
            }
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }


        BusinessLogEngine engine = getBusinessLogEngine();
        engine.setInitContext(createDefaultContext(joinPoint, result, error));
        engine.exportLogBy(joinPoint.getSignature().toString(), businessLogExporter);
    }

    private Map<String, Object> createDefaultContext(JoinPoint joinPoint, Object result, Throwable errer) {
        Map<String, Object> context = ThreadLocalBusinessLogContext.get();

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.put(PRE_OPERATOR_OF_METHOD_KEY + i, args[i]);
        }

        context.put(BUSINESS_METHOD_RETURN_VALUE_KEY, result);

        if (null != errer) {
            context.put(BUSINESS_METHOD_EXECUTE_ERROR, errer.getCause());
        }

        context.put(BUSINESS_METHOD, joinPoint.getSignature().toLongString());
        context.put(BUSINESS_OPERATION_TIME, new Date());

        return context;
    }

    private BusinessLogEngine getBusinessLogEngine() {
        if (businessLogEngine == null) {
            BusinessLogConfig config = new BusinessLogConfig(new BusinessLogXmlConfigDefaultAdapter());
            businessLogEngine = new BusinessLogEngine(config,
                    new BusinessLogFreemarkerDefaultRender(), new BusinessLogDefaultContextQueryExecutor());
        }
        return businessLogEngine;
    }


}
