package com.dayatang.datasource4saas.dbtype;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.datasource4saas.dscreator.DbType;

public class DbTypeOracleTest extends AbstractDbTypeTest {
	
	@Before
	public void setUp() throws Exception {
		instance = DbType.ORACLE;
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void getDriverClassName() {
		assertEquals("oracle.jdbc.OracleDriver", instance.getDriverClassName());
	}

	@Test
	public void getUrlWithoutExtraString() {
		assertEquals("jdbc:oracle:thin:@localhost:3306:XE", instance.getUrl(dbInfo));
	}

	@Test
	public void getUrlWithExtraString() {
		dbInfo.setExtraUrlString("useUnicode=true&characterEncoding=utf-8");
		assertEquals("jdbc:oracle:thin:@localhost:3306:XE?useUnicode=true&characterEncoding=utf-8", instance.getUrl(dbInfo));
		dbInfo.setExtraUrlString("?useUnicode=true&characterEncoding=utf-8");
		assertEquals("jdbc:oracle:thin:@localhost:3306:XE?useUnicode=true&characterEncoding=utf-8", instance.getUrl(dbInfo));
	}
}
