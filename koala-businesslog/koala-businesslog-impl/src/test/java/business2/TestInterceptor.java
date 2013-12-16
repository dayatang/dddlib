package business2;

import org.aspectj.lang.JoinPoint;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.inject.Inject;

/**
 * User: zjzhai
 * Date: 11/28/13
 * Time: 11:38 AM
 */
public class TestInterceptor {


    @Inject
    private BusinessLogEngine businessLogEngine;

    @Inject
    private BusinessLogExporter businessLogExporter;

    @Inject
    private ThreadPoolTaskExecutor executor;

    public void logAfter(JoinPoint joinPoint, Object result) {
        new IThread().start();

    }


}
