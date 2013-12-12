package org.openkoala.businesslog.impl;

import static org.openkoala.businesslog.common.ContextKeyConstant.*;

import com.dayatang.domain.InstanceFactory;
import org.openkoala.businesslog.AbstractBusinessLogExporter;
import org.openkoala.businesslog.RenderResult;
import org.openkoala.businesslog.application.BusinessLogApplication;
import org.openkoala.businesslog.model.SimpleBusinessLog;

import javax.inject.Inject;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 4:52 PM
 */
public class BusinessLogJpaDefaultExporter extends AbstractBusinessLogExporter {

    @Inject
    private BusinessLogApplication businessLogApplication;

    @Override
    public void export(RenderResult renderResult) {
        SimpleBusinessLog log = new SimpleBusinessLog();
        log.setBusinessMethod((String) renderResult.getContext().get(BUSINESS_METHOD));
        log.setLog(getLog());
        businessLogApplication.save(log);
        assert log.getId() != null;

    }


}
