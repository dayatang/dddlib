package org.openkoala.koala.auth.impl.jdbc;

public class JdbcSecurityConfig {

	private String dburl;
	private String queryUser;
	private String queryAllUser;
	private String queryUserAuth;
	private String queryResAuth;
	private String queryAllRes;
	private String queryAllAuth;
	private String queryPrivilege;
	private String dbdriver;
	private String dbuser = "";
	private String dbpassword = "";
	private String useAdmain = "false";
	private String adminAccount = "";
	private String adminPass = "";
	private String adminRealName = "";

	public String getQueryPrivilege() {
		return queryPrivilege;
	}

	public void setQueryPrivilege(String queryPrivilege) {
		this.queryPrivilege = queryPrivilege;
	}

	public String getAdminAccount() {
		return adminAccount;
	}

	public void setAdminAccount(String adminAccount) {
		this.adminAccount = adminAccount;
	}

	public String getAdminPass() {
		return adminPass;
	}

	public void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}

	public String getAdminRealName() {
		return adminRealName;
	}

	public void setAdminRealName(String adminRealName) {
		this.adminRealName = adminRealName;
	}

	public String getQueryAllAuth() {
		return queryAllAuth;
	}

	public void setQueryAllAuth(String queryAllAuth) {
		this.queryAllAuth = queryAllAuth;
	}

	public String getUseAdmain() {
		return useAdmain;
	}

	public void setUseAdmain(String useAdmain) {
		this.useAdmain = useAdmain;
	}

	public String getDbdriver() {
		return dbdriver;
	}

	public void setDbdriver(String dbdriver) {
		this.dbdriver = dbdriver;
	}

	public String getDbuser() {
		return dbuser;
	}

	public void setDbuser(String dbuser) {
		this.dbuser = dbuser;
	}

	public String getDbpassword() {
		return dbpassword;
	}

	public void setDbpassword(String dbpassword) {
		this.dbpassword = dbpassword;
	}

	public String getQueryUser() {
		return queryUser;
	}

	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}

	public String getQueryUserAuth() {
		return queryUserAuth;
	}

	public void setQueryUserAuth(String queryUserAuth) {
		this.queryUserAuth = queryUserAuth;
	}

	public String getQueryAllUser() {
		return queryAllUser;
	}

	public void setQueryAllUser(String queryAllUser) {
		this.queryAllUser = queryAllUser;
	}

	public String getQueryResAuth() {
		return queryResAuth;
	}

	public void setQueryResAuth(String queryResAuth) {
		this.queryResAuth = queryResAuth;
	}

	public String getQueryAllRes() {
		return queryAllRes;
	}

	public void setQueryAllRes(String queryAllRes) {
		this.queryAllRes = queryAllRes;
	}

	public String getDburl() {
		return dburl;
	}

	public void setDburl(String dburl) {
		this.dburl = dburl;
	}
}
