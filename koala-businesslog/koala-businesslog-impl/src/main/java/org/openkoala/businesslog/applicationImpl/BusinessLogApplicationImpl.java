package org.openkoala.businesslog.applicationImpl;

import org.openkoala.businesslog.application.BusinessLogApplication;
import org.openkoala.businesslog.model.AbstractBusinessLog;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Named;

/**
 * User: zjzhai
 * Date: 12/11/13
 * Time: 3:12 PM
 */
@Named
@Transactional
public class BusinessLogApplicationImpl implements BusinessLogApplication {
    @Override
    public void save(AbstractBusinessLog log) {
        log.save();
    }
}
