package org.openkoala.koala.auth.impl.ejb;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

public class JdbcSecurityConfig {

	private static String queryUser;

	private static String queryUserAuth;

	private static String queryResAuth;

	private static String queryAllRes;//

	private static String queryAllAuth;

	private static String dbdriver;

	private static String dbuser = "";

	private static String dbpassword = "";

	private static String dburl = "";

	private static String useAdmain = "false";

	private static String adminAccount = "";

	private static String adminPass = "";

	private static String adminRealName = "";
	
	private static final Logger LOGGER = Logger.getLogger("JdbcSecurityConfig"); 

	public String getQueryUser() {
		return queryUser;
	}

	static {
		try {
			String home = System.getProperty("user.home");
			Properties p = new Properties();
			if (new File(home + "/security.properties").exists()) {
				InputStream in = new BufferedInputStream(new FileInputStream(home + "/security.properties"));
				p.load(in);
			} else {
				InputStream dbIn = new BufferedInputStream(JdbcSecurityConfig.class.getClassLoader().getResourceAsStream(
						"META-INF/props/koala-security-db.properties"));
				InputStream sqlIn = new BufferedInputStream(JdbcSecurityConfig.class.getClassLoader().getResourceAsStream(
						"META-INF/props/koala-security-sql.properties"));
				p.load(dbIn);
				p.load(sqlIn);
			}

			queryUser = p.getProperty("security.db.jdbc.queryUser");
			queryUserAuth = p.getProperty("security.db.jdbc.queryUserAuth");
			queryResAuth = p.getProperty("security.db.jdbc.queryResAuth");
			queryAllRes = p.getProperty("security.db.jdbc.queryAllRes");
			queryAllAuth = p.getProperty("security.db.jdbc.queryAllAuth");
			dbdriver = p.getProperty("security.db.jdbc.driver");
			dburl = p.getProperty("security.db.jdbc.connection.url");
			dbuser = p.getProperty("security.db.jdbc.username");
			dbpassword = p.getProperty("security.db.jdbc.password");
			useAdmain = p.getProperty("security.db.jdbc.useAdmin");
			adminAccount = p.getProperty("security.db.jdbc.adminAccount");
			adminPass = p.getProperty("security.db.jdbc.adminPass");
			adminRealName = p.getProperty("security.db.jdbc.adminRealName");

		} catch (IOException e) {
			LOGGER.info(e.getMessage());
		}
	}

	public static String getQueryUserAuth() {
		return queryUserAuth;
	}

	public static void setQueryUserAuth(String queryUserAuth) {
		JdbcSecurityConfig.queryUserAuth = queryUserAuth;
	}

	public static String getQueryResAuth() {
		return queryResAuth;
	}

	public static void setQueryResAuth(String queryResAuth) {
		JdbcSecurityConfig.queryResAuth = queryResAuth;
	}

	public static String getQueryAllRes() {
		return queryAllRes;
	}

	public static void setQueryAllRes(String queryAllRes) {
		JdbcSecurityConfig.queryAllRes = queryAllRes;
	}

	public static String getQueryAllAuth() {
		return queryAllAuth;
	}

	public static void setQueryAllAuth(String queryAllAuth) {
		JdbcSecurityConfig.queryAllAuth = queryAllAuth;
	}

	public static String getDbdriver() {
		return dbdriver;
	}

	public static void setDbdriver(String dbdriver) {
		JdbcSecurityConfig.dbdriver = dbdriver;
	}

	public static String getDbuser() {
		return dbuser;
	}

	public static void setDbuser(String dbuser) {
		JdbcSecurityConfig.dbuser = dbuser;
	}

	public static String getDbpassword() {
		return dbpassword;
	}

	public static void setDbpassword(String dbpassword) {
		JdbcSecurityConfig.dbpassword = dbpassword;
	}

	public static String getDburl() {
		return dburl;
	}

	public static void setDburl(String dburl) {
		JdbcSecurityConfig.dburl = dburl;
	}

	public static String getUseAdmain() {
		return useAdmain;
	}

	public static void setUseAdmain(String useAdmain) {
		JdbcSecurityConfig.useAdmain = useAdmain;
	}

	public static String getAdminAccount() {
		return adminAccount;
	}

	public static void setAdminAccount(String adminAccount) {
		JdbcSecurityConfig.adminAccount = adminAccount;
	}

	public static String getAdminPass() {
		return adminPass;
	}

	public static void setAdminPass(String adminPass) {
		JdbcSecurityConfig.adminPass = adminPass;
	}

	public static String getAdminRealName() {
		return adminRealName;
	}

	public static void setAdminRealName(String adminRealName) {
		JdbcSecurityConfig.adminRealName = adminRealName;
	}

	public static void setQueryUser(String queryUser) {
		JdbcSecurityConfig.queryUser = queryUser;
	}
}
