package org.dayatang.datasource4saas.dscreator;

import com.alibaba.druid.pool.DruidDataSource;

import javax.sql.DataSource;

/**
 * Created by yyang on 2016/11/4.
 */
public class DruidDataSourceCreator extends AbstractDataSourceCreator {

    @Override
    protected DataSource createDataSource() {
        return new DruidDataSource();
    }

    @Override
    protected String getDriverClassPropName() {
        return "driverClassName";
    }

    @Override
    protected String getUrlPropName() {
        return "url";
    }

    @Override
    protected String getUsernamePropName() {
        return "username";
    }

    @Override
    protected String getPasswordPropName() {
        return "password";
    }
}
