package org.openkoala.businesslog.impl;

import com.dayatang.domain.InstanceFactory;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.BusinessLog;
import org.openkoala.businesslog.application.BusinessLogApplication;
import org.openkoala.businesslog.model.DefaultBusinessLog;

import javax.inject.Inject;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 4:52 PM
 */
public class BusinessLogJpaDefaultExporter implements BusinessLogExporter {

    private BusinessLogApplication businessLogApplication;

    /**
     * TODO 需要实现智能化
     * 用户只需要继承自DefaultBusinessLog，并在配置文件中配置好，就可以实现对日志持久化内容的定制
     *
     * @param businessLog
     */
    @Override
    public void export(BusinessLog businessLog) {
        DefaultBusinessLog log = DefaultBusinessLog.createBy(businessLog);
        getBusinessLogApplication().save(log);
        assert log.getId() != null;
    }

    public BusinessLogApplication getBusinessLogApplication() {
        if (null == businessLogApplication) {
            businessLogApplication = InstanceFactory.getInstance(BusinessLogApplication.class, "businessLogApplication");
        }
        return businessLogApplication;
    }
}
