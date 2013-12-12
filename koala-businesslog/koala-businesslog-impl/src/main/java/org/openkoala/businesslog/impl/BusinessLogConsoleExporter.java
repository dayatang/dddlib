package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.AbstractBusinessLogExporter;
import org.openkoala.businesslog.BusinessLogExporter;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 11:49 PM
 */
public class BusinessLogConsoleExporter extends AbstractBusinessLogExporter {

    @Override
    public void export() {
        System.out.println(getLog());
    }


}
