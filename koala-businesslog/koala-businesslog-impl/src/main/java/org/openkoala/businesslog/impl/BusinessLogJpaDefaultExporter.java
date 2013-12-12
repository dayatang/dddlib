package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.RenderResult;
import org.openkoala.businesslog.application.BusinessLogApplication;
import org.openkoala.businesslog.model.DefaultBusinessLog;

import javax.inject.Inject;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 4:52 PM
 */
public class BusinessLogJpaDefaultExporter implements BusinessLogExporter {

    @Inject
    private BusinessLogApplication businessLogApplication;

    @Override
    public void export(RenderResult renderResult) {
        DefaultBusinessLog log = DefaultBusinessLog.createBy(renderResult);


        businessLogApplication.save(log);

        assert log.getId() != null;

    }


}
