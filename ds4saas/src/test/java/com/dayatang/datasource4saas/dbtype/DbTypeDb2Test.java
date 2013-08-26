package com.dayatang.datasource4saas.dbtype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.datasource4saas.dscreator.DbType;

public class DbTypeDb2Test extends AbstractDbTypeTest {
	
	@Before
	public void setUp() throws Exception {
		instance = DbType.DB2;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void getDriverClaaaName() {
		assertEquals("com.ibm.db2.jcc.DB2Driver", instance.getDriverClassName());
	}

	@Test
	public void getUrlWithoutExtraString() {
		assertEquals("jdbc:db2://localhost:3306/test_db", instance.getUrl(host, port, dbname, dbInstance, username, null));
	}

	@Test
	public void getUrlWithExtraString() {
		assertEquals("jdbc:db2://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8", 
				instance.getUrl(host, port, dbname, dbInstance, username, extraUrlString));
		assertEquals("jdbc:db2://localhost:3306/test_db?useUnicode=true&characterEncoding=utf-8", 
				instance.getUrl(host, port, dbname, dbInstance, username, "?" + extraUrlString));
	}
}
