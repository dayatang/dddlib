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
    public synchronized String render(Map<String, Object> context, String... templates) {
        if (null == templates) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (String template : templates) {
            builder.append(process(template, context));
        }
        return builder.toString();
    }


    private String process(String template, Map<String, Object> context) {
        if (null == template || "".equals(template.trim())) {
            return "";
        }
        return FreemarkerProcessor.process(template, context);
    }


}
