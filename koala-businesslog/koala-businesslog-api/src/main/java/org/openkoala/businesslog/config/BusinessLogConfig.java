package org.openkoala.businesslog.config;

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

    public String getLogTemplateof(String businessOperation) {
        return configAdapter.findConfigByBusinessOperation(businessOperation).getTemplate();
    }

    public List<BusinessLogContextQuery> getQueries(String businessOperation) {
        return configAdapter.findConfigByBusinessOperation(businessOperation).getQueries();
    }


}
