package org.openkoala.businesslog.utils;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import com.dayatang.domain.InstanceFactory;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.common.BusinessLogPropertiesConfig;
import org.openkoala.businesslog.common.LogEngineCallable;
import org.openkoala.businesslog.common.LogEngineThread;
import org.openkoala.businesslog.common.ThreadPool;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor;
import org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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

    /**
     * 当前调用方法在ThreadLocalBusinessLogContext中的key
     */
    private static final String CURRENT_INVOKED_METHOD_KEY = "CURRENT_INVOKED_METHOD_KEY";

    @Inject
    private BusinessLogEngine businessLogEngine;

    @Inject
    private BusinessLogExporter businessLogExporter;

    @Inject
    private ThreadPoolTaskExecutor executor;

    public void logAfter(JoinPoint joinPoint, Object result) {
        log(joinPoint, result, null);
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable error) {
        log(joinPoint, null, error);

    }

    public void log(JoinPoint joinPoint, Object result, Throwable error) {

        if (!BusinessLogPropertiesConfig.getInstance().getLogEnableConfig()) {
            return;
        }

       executor.execute(
                new LogEngineThread(
                        businessLogEngine,
                        businessLogExporter,
                        createDefaultContext(joinPoint, result, error),
                        joinPoint.getSignature().toString()
                        )
        );



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

        context.put(BUSINESS_METHOD, joinPoint.getSignature().toString());
        context.put(BUSINESS_OPERATION_TIME, new Date());

        return context;
    }


    private BusinessLogEngine getBusinessLogEngine() {
        BusinessLogConfig config = new BusinessLogConfig(new BusinessLogXmlConfigDefaultAdapter());
        businessLogEngine = new BusinessLogEngine(config,
                new BusinessLogFreemarkerDefaultRender(), new BusinessLogDefaultContextQueryExecutor());
        return businessLogEngine;
    }


}
