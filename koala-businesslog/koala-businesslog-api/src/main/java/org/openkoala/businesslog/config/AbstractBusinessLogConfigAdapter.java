package org.openkoala.businesslog.config;

import java.util.List;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 2:51 PM
 */
public abstract class AbstractBusinessLogConfigAdapter implements BusinessLogConfigAdapter {

    private String category;

    private String template;

    private List<BusinessLogContextQuery> queries;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public List<BusinessLogContextQuery> getQueries() {
        return queries;
    }

    public void setQueries(List<BusinessLogContextQuery> queries) {
        this.queries = queries;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

}
