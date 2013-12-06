package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.model.BusinessLog;

/**
 * User: zjzhai
 * Date: 12/5/13
 * Time: 4:52 PM
 */
public class BusinessLogJpaDefaultExporter implements BusinessLogExporter {
    @Override
    public void export(String log) {

        BusinessLog businessLog = new BusinessLog(log);

        businessLog.save();

    }
}
