package com.dayatang.dsrouter.dbtype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.DbType;

public class DbTypeMySqlTest extends AbstractDbTypeTest {
	
	@Before
	public void setUp() throws Exception {
		instance = DbType.MYSQL;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void withoutExtraUrlString() {
		assertEquals("jdbc:mysql://localhost:3306/test_db", instance.getUrl(jdbcConfiguration));
	}

	@Test
	public void withExtraUrlString() {
		jdbcConfiguration.setExtraUrlString("useUnicode=true&encoding=UTF-8");
		assertEquals("jdbc:mysql://localhost:3306/test_db?useUnicode=true&encoding=UTF-8", instance.getUrl(jdbcConfiguration));
	}
}
