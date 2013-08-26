package com.dayatang.datasource4saas.dbtype;

import com.dayatang.datasource4saas.dscreator.DbInfo;
import com.dayatang.datasource4saas.dscreator.DbType;

public abstract class AbstractDbTypeTest {
	
	protected DbType instance;
	protected DbInfo dbInfo;
	

	public AbstractDbTypeTest() {
		dbInfo = new DbInfo();
		dbInfo.setHost("localhost");
		dbInfo.setPort("3306");
		dbInfo.setDbname("test_db");
		dbInfo.setInstance("XE");
		dbInfo.setUsername("root");
	}

}
