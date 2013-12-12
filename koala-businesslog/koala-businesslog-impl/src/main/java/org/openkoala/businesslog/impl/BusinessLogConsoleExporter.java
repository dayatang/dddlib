package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.AbstractBusinessLogExporter;
import org.openkoala.businesslog.BusinessLogExporter;
import org.openkoala.businesslog.RenderResult;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/8/13
 * Time: 11:49 PM
 */
public class BusinessLogConsoleExporter extends AbstractBusinessLogExporter {


    @Override
    public void export(RenderResult renderResult) {
        System.out.println(renderResult.getLog());
    }
}
