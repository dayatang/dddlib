package org.openkoala.businesslog.utils;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import com.dayatang.domain.InstanceFactory;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.*;
import org.openkoala.businesslog.common.BusinessLogPropertiesConfig;
import org.openkoala.businesslog.common.LogEngineThread;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.inject.Inject;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;

/**
 * User: zjzhai Date: 11/28/13 Time: 11:38 AM
 */
public class BusinessLogInterceptor {


    @Inject
    private BusinessLogConfigAdapter businessLogConfigAdapter;

    @Inject
    private BusinessLogRender businessLogRender;

    @Inject
    private BusinessLogExporter businessLogExporter;

    @Inject
    private BusinessLogContextQueryExecutor queryExecutor;

    @Inject
    private ThreadPoolTaskExecutor executor;

    public void logAfter(JoinPoint joinPoint, Object result) {
        log(joinPoint, result, null);
    }

    public void afterThrowing(JoinPoint joinPoint, Throwable error) {
        log(joinPoint, null, error);

    }

    public void log(JoinPoint joinPoint, Object result, Throwable error) {


        if (!BusinessLogPropertiesConfig.getInstance().getLogEnableConfig()
                || ThreadLocalBusinessLogContext.get().get(BUSINESS_METHOD) != null) {
            return;
        }
        executor.execute(new LogEngineThread(
                Collections.unmodifiableMap(createDefaultContext(joinPoint, result, error)),
                joinPoint.getSignature().toString(),
                businessLogConfigAdapter,
                businessLogRender,
                businessLogExporter,
                queryExecutor)
        );


    }

    private synchronized Map<String, Object> createDefaultContext(JoinPoint joinPoint,
                                                     Object result, Throwable error) {
        Map<String, Object> context = ThreadLocalBusinessLogContext.get();

        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            context.put(PRE_OPERATOR_OF_METHOD_KEY + i, args[i]);
        }

        context.put(BUSINESS_METHOD_RETURN_VALUE_KEY, result);

        if (null != error) {
            context.put(BUSINESS_METHOD_EXECUTE_ERROR, error.getCause());
        }

        context.put(BUSINESS_METHOD, joinPoint.getSignature().toString());
        context.put(BUSINESS_OPERATION_TIME, new Date());
        return context;

    }


}
