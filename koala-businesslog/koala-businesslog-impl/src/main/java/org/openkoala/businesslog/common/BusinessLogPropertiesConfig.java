package org.openkoala.businesslog.common;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.openkoala.businesslog.BusinessLogPropertiesConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: zjzhai
 * Date: 12/12/13
 * Time: 3:10 PM
 */
public class BusinessLogPropertiesConfig {
    private final static Logger LOGGER = LoggerFactory.getLogger(BusinessLogPropertiesConfig.class);

    private final static String BUSINESS_LOG_CONFIG_PROPERTIES_NAME = "koala-businesslog.properties";

    private final static String LOG_ENABLE = "kaola.businesslog.enable";


    private static BusinessLogPropertiesConfig ourInstance = new BusinessLogPropertiesConfig();

    private static PropertiesConfiguration configuration;

    public static BusinessLogPropertiesConfig getInstance() {
        try {
            configuration = new PropertiesConfiguration(BUSINESS_LOG_CONFIG_PROPERTIES_NAME);
        } catch (ConfigurationException e) {
            LOGGER.error(BUSINESS_LOG_CONFIG_PROPERTIES_NAME + " read have some error", e);
            throw new BusinessLogPropertiesConfigException(e);
        }
        return ourInstance;
    }

    private BusinessLogPropertiesConfig() {
    }

    public boolean getLogEnableConfig() {
        return configuration.getBoolean(LOG_ENABLE, false);
    }

}
