package org.openkoala.businesslog.impl;

import org.openkoala.businesslog.common.BusinessLogConfigPathProcessor;
import org.openkoala.businesslog.common.BusinessLogConfigXmlParser;
import org.openkoala.businesslog.config.BusinessLogConfig;
import org.openkoala.businesslog.config.BusinessLogConfigAdapter;

import java.io.File;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 10:36 AM
 */
public class BusinessLogXmlConfigDefaultAdapter implements BusinessLogConfigAdapter {



    public   BusinessLogConfig findConfigBy(String businessOperation) {
        BusinessLogConfigXmlParser xmlParser = null;
        for (File xmlFile : BusinessLogConfigPathProcessor.getAllConfigFiles()) {
            BusinessLogConfigXmlParser parser = BusinessLogConfigXmlParser.parsing(xmlFile);
            if (parser.exists(businessOperation)) {
                xmlParser = parser;
                break;
            }
        }
        if (null == xmlParser) {
            return null;
        }
        BusinessLogConfig config = new BusinessLogConfig();

        config.setCategory(xmlParser.getCategory(businessOperation));
        config.setQueries(xmlParser.getQueriesFrom(businessOperation));
        config.setTemplate(xmlParser.getTemplateFrom(businessOperation));

        return config;
    }
}