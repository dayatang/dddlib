package com.dayatang.dsrouter.dbtype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.DbType;

public class DbTypeSqlServerTest extends AbstractDbTypeTest {
	
	@Before
	public void setUp() throws Exception {
		instance = DbType.SQLSERVER;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getUrl() {
		assertEquals("jdbc:jtds:sqlserver://localhost:3306/test_db", instance.getUrl(jdbcConfiguration));
	}
}
