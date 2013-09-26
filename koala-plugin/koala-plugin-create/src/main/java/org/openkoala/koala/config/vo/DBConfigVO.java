package org.openkoala.koala.config.vo;

import java.io.Serializable;

/**
 * 数据库配置所对应的VO
 * @author lingen
 *
 */
public class DBConfigVO implements Serializable {

    private static final long serialVersionUID = -5911054132124935779L;

    private String hbm2ddl;//如果更新数据库
	
	private String showSql;//是否显示SQL
	
	private String dialect;//方言
	
	private String jdbcDriver;//数据库驱动
	
	private String connectionURL;//连接URL
	
	private String username;//用户名
	
	private String password;//密码
	
	private String dbType = "H2";//数据库类型
	
	private String xmlPath;
	
	private String dbGroupId;
	
	private String dbArtifactId;

	public String getHbm2ddl() {
		return hbm2ddl;
	}

	public void setHbm2ddl(String hbm2ddl) {
		this.hbm2ddl = hbm2ddl;
	}

	public String getShowSql() {
		return showSql;
	}

	public void setShowSql(String showSql) {
		this.showSql = showSql;
	}

	public String getDialect() {
		return dialect;
	}

	public void setDialect(String dialect) {
		this.dialect = dialect;
	}

	public String getJdbcDriver() {
		return jdbcDriver;
	}

	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}

	public String getConnectionURL() {
		return connectionURL;
	}

	public void setConnectionURL(String connectionURL) {
		this.connectionURL = connectionURL;
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

	public String getDbType() {
		return dbType;
	}

	public void setDbType(String dbType) {
		this.dbType = dbType;
	}

	public String getXmlPath() {
		return xmlPath;
	}

	public void setXmlPath(String xmlPath) {
		this.xmlPath = xmlPath;
	}

	public String getDbGroupId() {
        return dbGroupId;
    }

    public void setDbGroupId(String dbGroupId) {
        this.dbGroupId = dbGroupId;
    }

    public String getDbArtifactId() {
        return dbArtifactId;
    }

    public void setDbArtifactId(String dbArtifactId) {
        this.dbArtifactId = dbArtifactId;
    }

    @Override
    public String toString() {
        return "DBConfigVO [hbm2ddl=" + hbm2ddl + ", showSql=" + showSql + ", dialect=" + dialect + ", jdbcDriver="
                + jdbcDriver + ", connectionURL=" + connectionURL + ", username=" + username + ", password=" + password
                + ", dbType=" + dbType + ", xmlPath=" + xmlPath + ", dbGroupId=" + dbGroupId + ", dbArtifactId="
                + dbArtifactId + "]";
    }
	
}
