package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.AbstractBusinessLogRender;
import org.openkoala.businesslog.BusinessLogRender;
import org.openkoala.businesslog.common.FreemarkerProcessor;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:23 PM
 */
public class BusinessLogFreemarkerDefaultRender extends AbstractBusinessLogRender {

    @Override
    public BusinessLogRender render(Map<String, Object> context, String... templates) {
        if (null == templates) {
            return this;
        }
        for (String template : templates) {
            getBuilder().append(process(template, context));
        }

        return this;
    }

    private String process(String template, Map<String, Object> context) {
        return FreemarkerProcessor.process(template, context);
    }


}
