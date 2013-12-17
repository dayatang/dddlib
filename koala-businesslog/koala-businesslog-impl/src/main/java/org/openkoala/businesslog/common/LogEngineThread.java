package org.openkoala.businesslog.common;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.BusinessLogEngine;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.impl.BusinessLogDefaultContextQueryExecutor;
import org.openkoala.businesslog.impl.BusinessLogFreemarkerDefaultRender;
import org.openkoala.businesslog.impl.BusinessLogXmlConfigDefaultAdapter;
import org.openkoala.businesslog.utils.ThreadLocalBusinessLogContext;

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

        synchronized (this) {

            System.out.println("第一次进入时的业务方法" + ThreadLocalBusinessLogContext.get().get(BUSINESS_METHOD));
            System.out.println("当前执行的业务方法" + joinPointSignature);
            if (isRecursionQuery(joinPointSignature)) {
                return;
            }
            BusinessLogConfig config = new BusinessLogConfig(new BusinessLogXmlConfigDefaultAdapter());
            BusinessLogEngine businessLogEngine1 = new BusinessLogEngine(config,
                    new BusinessLogFreemarkerDefaultRender(), new BusinessLogDefaultContextQueryExecutor());

            businessLogEngine1.setInitContext(context);
            BusinessLog log = businessLogEngine1.exportLogBy(joinPointSignature, businessLogExporter);

            if (isRecursionQuery(joinPointSignature)) {
                ThreadLocalBusinessLogContext.clear();
                return;
            }
            System.out.println("退出时的业务方法 " + joinPointSignature);
            System.out.println(log);

        }
    }


    private boolean isRecursionQuery(String joinPointSignature) {
        Object businessMethod = ThreadLocalBusinessLogContext.get().get(BUSINESS_METHOD);
        if (businessMethod == null) {
            ThreadLocalBusinessLogContext.put(BUSINESS_METHOD, joinPointSignature);
            return false;
        } else if (businessMethod != null && businessMethod.equals(joinPointSignature)) {
            return true;
        }
        return false;
    }
}
