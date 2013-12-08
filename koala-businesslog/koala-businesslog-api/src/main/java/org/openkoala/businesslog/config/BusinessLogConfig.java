package org.openkoala.businesslog.config;

import org.openkoala.businesslog.BusinessLogContextQueryExecutor;

import java.util.List;

/**
 * User: zjzhai
 * Date: 12/3/13
 * Time: 11:26 AM
 */
public class BusinessLogConfig {

    private String businessOperator;

    private BusinessLogConfigAdapter configAdapter;

    private BusinessLogConfig() {
    }

    public BusinessLogConfig(BusinessLogConfigAdapter configAdapter, String businessOperator) {
        this.configAdapter = configAdapter;
        this.businessOperator = businessOperator;
    }

    public static BusinessLogConfig createByAdapterAndBusinessOperator(BusinessLogConfigAdapter configAdapter, String businessOperator) {
        return new BusinessLogConfig(configAdapter, businessOperator);
    }

    public String getBusinessOperator() {
        return businessOperator;
    }

    public String getPreTemplate() {
        return configAdapter.findConfigByBusinessOperator(businessOperator).getPreTemplate();
    }

    public String getTemplate() {
        return configAdapter.findConfigByBusinessOperator(businessOperator).getTemplate();
    }

    public List<BusinessLogContextQuery> getQueries() {
        return configAdapter.findConfigByBusinessOperator(businessOperator).getQueries();
    }


}
