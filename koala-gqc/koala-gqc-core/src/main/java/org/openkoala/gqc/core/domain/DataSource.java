package org.openkoala.gqc.core.domain;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.apache.commons.dbutils.DbUtils;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.InstanceFactory;

/**
 * 查询数据源
 * @author xmfang
 *
 */
@Entity
@Table(name = "DATA_SOURCES")
public class DataSource extends GeneralQueryEntity {

	private static final long serialVersionUID = 7435451055102038439L;
	
	/**
	 * 数据源类型
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "DATA_SOURCE_TYPE")
	private DataSourceType dataSourceType;
	
	/**
	 * 数据源ID
	 */
	@Column(name = "DATA_SOURCE_ID")
	private String dataSourceId;

	/**
	 * 数据源描述
	 */
	@Column(name = "DATA_SOURCE_DESCRIPTION")
	private String dataSourceDescription;
	
	/**
	 * 连接URL
	 */
	@Column(name = "CONNECT_URL")
	private String connectUrl;
	
	/**
	 * 数据源驱动
	 */
	@Column(name = "JDBC_DRIVER")
	private String jdbcDriver;
	
	/**
	 * 用户名
	 */
	@Column(name = "USERNAME")
	private String username;
	
	/**
	 * 密码
	 */
	@Column(name = "PASSWORD")
	private String password;

	public DataSourceType getDataSourceType() {
		return dataSourceType;
	}

	public void setDataSourceType(DataSourceType dataSourceType) {
		this.dataSourceType = dataSourceType;
	}

	public String getDataSourceId() {
		return dataSourceId;
	}

	public void setDataSourceId(String dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	public String getDataSourceDescription() {
		return dataSourceDescription;
	}

	public void setDataSourceDescription(String dataSourceDescription) {
		this.dataSourceDescription = dataSourceDescription;
	}

	public String getConnectUrl() {
		return connectUrl;
	}

	public void setConnectUrl(String connectUrl) {
		this.connectUrl = connectUrl;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	/**
	 * 获取系统数据源
	 * @return
	 * @throws SQLException 
	 */
	public static DataSource getSystemDataSource(String dataSourceId) throws SQLException {
		DataSource result = null;
		javax.sql.DataSource dataSource = InstanceFactory.getInstance(javax.sql.DataSource.class, dataSourceId);
		
		if (dataSource != null) {
            Connection conn = dataSource.getConnection();
		    result = new DataSource();
		    result.setDataSourceId(dataSourceId);
		    result.setDataSourceType(DataSourceType.SYSTEM_DATA_SOURCE);
			result.setConnectUrl(conn.getMetaData().getURL());
			result.setUsername(conn.getMetaData().getUserName());
			conn.close();
		}
		
		return result;
	}

	/**
	 * 测试数据源连接
	 * @return
	 */
	public boolean testConnection() {
		boolean result = false;
        Connection connection = null;
		
		if (dataSourceType.equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
		    javax.sql.DataSource dataSource = InstanceFactory.getInstance(javax.sql.DataSource.class, dataSourceId);
		    try {
                connection = dataSource.getConnection();
                if (connection != null) {
                    result = true;
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
		    
		    return result;
		}
		
        DbUtils.loadDriver(jdbcDriver);
        try {
            connection = DriverManager.getConnection(connectUrl, username, password);
            if (connection != null) {
            	result = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(connection);
        }
		
        return result;
	}
	
	/**
     * 获取数据源连接
     * @return
     */
    public Connection generateConnection() {
        Connection connection = null;
        
        if (dataSourceType.equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
            javax.sql.DataSource dataSource = InstanceFactory.getInstance(javax.sql.DataSource.class, dataSourceId);
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            DbUtils.loadDriver(jdbcDriver);
            try {
                connection = DriverManager.getConnection(connectUrl, username, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return connection;
    }
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((connectUrl == null) ? 0 : connectUrl.hashCode());
		result = prime
				* result
				+ ((dataSourceDescription == null) ? 0 : dataSourceDescription
						.hashCode());
		result = prime * result
				+ ((dataSourceId == null) ? 0 : dataSourceId.hashCode());
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
		DataSource other = (DataSource) obj;
		if (connectUrl == null) {
			if (other.connectUrl != null)
				return false;
		} else if (!connectUrl.equals(other.connectUrl))
			return false;
		if (dataSourceDescription == null) {
			if (other.dataSourceDescription != null)
				return false;
		} else if (!dataSourceDescription.equals(other.dataSourceDescription))
			return false;
		if (dataSourceId == null) {
			if (other.dataSourceId != null)
				return false;
		} else if (!dataSourceId.equals(other.dataSourceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return dataSourceId;
	}

}
