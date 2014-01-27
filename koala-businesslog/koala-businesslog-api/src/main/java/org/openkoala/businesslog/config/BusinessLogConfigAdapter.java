package org.openkoala.businesslog.config;

import java.util.List;

/**
 * User: zjzhai
 * Date: 12/4/13
 * Time: 10:30 AM
 */
public interface BusinessLogConfigAdapter {

    BusinessLogConfig findConfigBy(String businessOperation);
}
