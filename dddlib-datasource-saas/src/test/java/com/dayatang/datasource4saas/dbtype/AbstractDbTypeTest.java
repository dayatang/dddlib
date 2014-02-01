package com.dayatang.datasource4saas.dbtype;


import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

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

	@Test
	public void testOf() {
		assertThat(DbType.of("mysql"), is(DbType.MYSQL));
		assertThat(DbType.of("Db2"), is(DbType.DB2));
		assertThat(DbType.of("ORACLE"), is(DbType.ORACLE));
		assertThat(DbType.of("postGresqL"), is(DbType.POSTGRESQL));
		assertThat(DbType.of("SqlServer"), is(DbType.SQLSERVER));
	}
}
