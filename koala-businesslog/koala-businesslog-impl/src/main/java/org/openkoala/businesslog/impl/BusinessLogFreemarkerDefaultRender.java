package org.openkoala.businesslog.impl;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.openkoala.businesslog.AbstractBusinessLogBuild;
import org.openkoala.businesslog.BusinessLogRender;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 11/29/13
 * Time: 4:23 PM
 */
public class BusinessLogFreemarkerDefaultRender extends AbstractBusinessLogBuild {

    @Override
    public BusinessLogRender render(Map<String, Object> context, String... templates) {
        if (null == templates) {
            return this;
        }
        for (String template : templates) {
            builder.append(process(context, template));
        }

        return this;
    }

    private String process(Map<String, Object> context, String template) {
        Configuration configuration = new Configuration();
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template", template);
        configuration.setTemplateLoader(stringLoader);
        Template freemarkerTemplate = null;
        StringWriter out = new StringWriter(512);
        try {
            freemarkerTemplate = configuration.getTemplate("template", "UTF-8");
            freemarkerTemplate.process(context, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        }
        return out.toString();

    }


}
