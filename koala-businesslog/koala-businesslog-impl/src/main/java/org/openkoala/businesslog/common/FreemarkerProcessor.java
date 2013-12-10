package org.openkoala.businesslog.common;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/7/13
 * Time: 8:28 PM
 */
public class FreemarkerProcessor {

    private static Configuration configuration = new Configuration();

    public static String process(String utf8Template, Map<String, Object> aContext) {
        return process(utf8Template, aContext, "UTF8");

    }

    public static String process(String template, Map<String, Object> aContext, String templateEncoding) {
        if (null == template || "".equals(template.trim())) {
            return "";
        }
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        stringLoader.putTemplate("template", template);
        configuration.setTemplateLoader(stringLoader);
        Template freemarkerTemplate = null;
        StringWriter out = new StringWriter(512);
        try {
            freemarkerTemplate = configuration.getTemplate("template", templateEncoding);
            freemarkerTemplate.process(aContext, out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (TemplateException e) {
            throw new RuntimeException(e);
        } finally {
            configuration.clearSharedVariables();
            configuration.clearTemplateCache();
        }
        return out.toString();
    }

    private FreemarkerProcessor() {
    }
}
