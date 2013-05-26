package com.dayatang.dsrouter.urltranslator;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.dsrouter.dscreator.DbType;

public class DbTypeOracleTest extends AbstractDbTypeTest {
	
	@Before
	public void setUp() throws Exception {
		instance = DbType.ORACLE;
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void withoutExtraUrlString() {
		assertEquals("jdbc:oracle:thin:@localhost:3306:XE", instance.getUrl("a", jdbcConfiguration));
	}
}
