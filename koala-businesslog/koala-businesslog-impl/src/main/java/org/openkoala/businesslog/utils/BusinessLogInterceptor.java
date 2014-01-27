package org.openkoala.businesslog.utils;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import com.dayatang.domain.InstanceFactory;

import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.*;
import org.openkoala.businesslog.common.BusinessLogPropertiesConfig;
import org.openkoala.businesslog.common.LogEngineThread;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Collections;
import java.util.Date;
import java.util.Map;

/**
 * User: zjzhai Date: 11/28/13 Time: 11:38 AM
 */
public class BusinessLogInterceptor {


    private BusinessLogConfigAdapter businessLogConfigAdapter;

    private BusinessLogRender businessLogRender;

    private BusinessLogExporter businessLogExporter;

    private BusinessLogContextQueryExecutor queryExecutor;

    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

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

        LogEngineThread logEngineThread = new LogEngineThread(
                Collections.unmodifiableMap(createDefaultContext(joinPoint, result, error)),
                joinPoint.getSignature().toString(),
                getBusinessLogConfigAdapter(),
                getBusinessLogRender(),
                getBusinessLogExporter(),
                getQueryExecutor());


        if (null == getThreadPoolTaskExecutor()) {
            System.err.println("ThreadPoolTaskExecutor is not set or null");
            logEngineThread.run();
        } else {
            getThreadPoolTaskExecutor().execute(logEngineThread);

        }


    }

    private Map<String, Object> createDefaultContext(JoinPoint joinPoint,
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

    public BusinessLogConfigAdapter getBusinessLogConfigAdapter() {
        if (null == businessLogConfigAdapter) {
            businessLogConfigAdapter = InstanceFactory.getInstance(BusinessLogConfigAdapter.class, "businessLogConfigAdapter");
        }

        return businessLogConfigAdapter;
    }

    public BusinessLogRender getBusinessLogRender() {
        if (null == businessLogRender) {
            businessLogRender = InstanceFactory.getInstance(BusinessLogRender.class, "businessLogRender");

        }
        return businessLogRender;
    }

    public BusinessLogExporter getBusinessLogExporter() {
        if (null == businessLogExporter) {
            businessLogExporter = InstanceFactory.getInstance(BusinessLogExporter.class, "businessLogExporter");

        }
        return businessLogExporter;
    }

    public BusinessLogContextQueryExecutor getQueryExecutor() {
        if (null == queryExecutor) {
            queryExecutor = InstanceFactory.getInstance(BusinessLogContextQueryExecutor.class, "queryExecutor");

        }
        return queryExecutor;
    }

    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        if (null == threadPoolTaskExecutor) {
            threadPoolTaskExecutor = InstanceFactory.getInstance(ThreadPoolTaskExecutor.class, "threadPoolTaskExecutor");

        }

        return threadPoolTaskExecutor;
    }
}
