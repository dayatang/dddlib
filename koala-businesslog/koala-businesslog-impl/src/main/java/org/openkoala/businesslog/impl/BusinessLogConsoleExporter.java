package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.BusinessLog;


/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 11:49 PM
 */
public class BusinessLogConsoleExporter implements BusinessLogExporter {


    @Override
    public void export(BusinessLog businessLog) {
        System.out.println(businessLog);
    }
}
