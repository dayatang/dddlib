package org.openkoala.businesslog.config;

import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 11:26 AM
 */
public class BusinessLogConfig {


    private String template;

    private String category;

    private List<BusinessLogContextQuery> queries;

    public BusinessLogConfig() {
    }

    public String getTemplate() {
        return template;
    }

    public String getCategory() {
        return category;
    }

    public List<BusinessLogContextQuery> getQueries() {
        return queries;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setQueries(List<BusinessLogContextQuery> queries) {
        this.queries = queries;
    }
}
