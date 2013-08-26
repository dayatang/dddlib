package com.dayatang.datasource4saas.dbtype;

import com.dayatang.datasource4saas.dscreator.DbType;

public abstract class AbstractDbTypeTest {
	
	protected DbType instance;
	protected String tenant = "abc";
	
	protected String host = "localhost";
	protected String port = "3306";
	protected String dbname = "test_db";
	protected String dbInstance = "XE";
	protected String username = "root";
	protected String extraUrlString = "useUnicode=true&characterEncoding=utf-8";
	

	public AbstractDbTypeTest() {
	}

}
