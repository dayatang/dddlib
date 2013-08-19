package org.openkoala.koala.auth.ss3adapter;


public class SecurityConfig {
	//@Value("#{properties['db.jdbc.driver']}")
	private String driverClassName;
	//@Value("#{properties['db.jdbc.connection.url']}")
	private String url;
	//@Value("#{properties['db.jdbc.username']}")
	private String username;
	//@Value("#{properties['db.jdbc.password']}")
	private String password;
	//@Value("#{properties['hibernate.dialect']}")
	private String hibernateDialect;
	//@Value("#{properties['hibernate.hbm2ddl.auto']}")
	private String hibernateAuto;
	
	private boolean useCache;
	

	public boolean getUseCache() {
		return useCache;
	}
	public void setUseCache(boolean useCache) {
		this.useCache = useCache;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
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
	public String getHibernateDialect() {
		return hibernateDialect;
	}
	public void setHibernateDialect(String hibernateDialect) {
		this.hibernateDialect = hibernateDialect;
	}
	public String getHibernateAuto() {
		return hibernateAuto;
	}
	public void setHibernateAuto(String hibernateAuto) {
		this.hibernateAuto = hibernateAuto;
	}
	
	public SecurityConfig()
	{
		
	}
	
}
