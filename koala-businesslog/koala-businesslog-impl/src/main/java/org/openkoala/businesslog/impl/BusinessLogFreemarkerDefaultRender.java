package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.BusinessLogRender;
import org.openkoala.businesslog.common.FreemarkerProcessor;

import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:23 PM
 */
public class BusinessLogFreemarkerDefaultRender implements BusinessLogRender {

    public BusinessLogFreemarkerDefaultRender() {
    }

    @Override
    public synchronized String render(Map<String, Object> context, String template) {
        if (null == template || "".equals(template.trim())) {
            return "";
        }
        return FreemarkerProcessor.process(template, context);
    }
}
