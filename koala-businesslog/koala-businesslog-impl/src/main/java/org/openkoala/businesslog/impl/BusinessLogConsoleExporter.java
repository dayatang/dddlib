package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogExporter;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 11:49 PM
 */
public class BusinessLogConsoleExporter implements BusinessLogExporter {


    @Override
    public void export(String log) {
        System.out.println(log);
    }
}
