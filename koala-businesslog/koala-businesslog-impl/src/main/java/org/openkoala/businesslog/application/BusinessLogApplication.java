package org.openkoala.businesslog.application;

import org.openkoala.businesslog.model.AbstractBusinessLog;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 3:11 PM
 */
public interface BusinessLogApplication {

    void save(AbstractBusinessLog log);

}
