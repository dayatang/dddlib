package org.openkoala.gqc.vo;

import java.io.Serializable;

import org.openkoala.gqc.core.domain.DataSourceType;

/**
 * 
 * 与展示层交互的数据源对象
 *
 */
public class DataSourceVO implements Serializable {
    private static final long serialVersionUID = -4952281843127855419L;

    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * url
     */
    private String connectUrl;

    /**
     * 密码
     */
    private String password;

    /**
     * 驱动uri
     */
    private String driverUri;

    /**
     * 数据源描述
     */
    private String dataSourceDescription;

    //（springmvc对枚举类型在页面和后台会分别自动转换为字符串和枚举）
    private DataSourceType dataSourceType;

    //类型字符串形式
//    private String dataSourceTypeStr;

    //类型描述
    private String dataSourceTypeDesc;

    /**
     * jdbc驱动
     */
    private String jdbcDriver;

    /**
     * 数据源id
     */
    private String dataSourceId;

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public String getDataSourceTypeDesc() {
        return dataSourceTypeDesc;
    }

    public void setDataSourceTypeDesc(String dataSourceTypeDesc) {
        this.dataSourceTypeDesc = dataSourceTypeDesc;
    }

    public void setConnectUrl(String connectUrl) {
        this.connectUrl = connectUrl;
    }

    public String getConnectUrl() {
        return this.connectUrl;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setDriverUri(String driverUri) {
        this.driverUri = driverUri;
    }

    public String getDriverUri() {
        return this.driverUri;
    }

    public void setDataSourceDescription(String dataSourceDescription) {
        this.dataSourceDescription = dataSourceDescription;
    }

    public String getDataSourceDescription() {
        return this.dataSourceDescription;
    }

    public void setDataSourceType(DataSourceType dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public DataSourceType getDataSourceType() {
        return this.dataSourceType;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getJdbcDriver() {
        return this.jdbcDriver;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceId() {
        return this.dataSourceId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataSourceVO other = (DataSourceVO) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
