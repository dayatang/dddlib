package org.openkoala.businesslog.config;

import org.openkoala.businesslog.BusinessLogContextQueryExecutor;

import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 11:26 AM
 */
public class BusinessLogConfig {


    private BusinessLogConfigAdapter configAdapter;

    private BusinessLogConfig() {
    }

    public BusinessLogConfig(BusinessLogConfigAdapter configAdapter) {
        this.configAdapter = configAdapter;
    }

    public String getPreTemplate() {
        return configAdapter.getPreTemplate();
    }

    public String getLogTemplateof(String businessOperator) {
        return configAdapter.findConfigByBusinessOperator(businessOperator).getTemplate();
    }

    public List<BusinessLogContextQuery> getQueries(String businessOperator) {
        return configAdapter.findConfigByBusinessOperator(businessOperator).getQueries();
    }


}
