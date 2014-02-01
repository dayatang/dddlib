package com.dayatang.springtest;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.db.DBManager;
import com.dayatang.db.PropertiesUtil;
import com.dayatang.domain.InstanceFactory;

public abstract class DeveloperTestSuite {

	private static final Logger logger = LoggerFactory
			.getLogger(DeveloperTestSuite.class);

	private static String initSQLFile = PropertiesUtil.INIT_SQL_FILE;

	private static DBManager dbMgr;

	public static DBManager getDbMgr() {
		if (dbMgr == null) {
			dbMgr = InstanceFactory.getInstance(DBManager.class);
		}
		return dbMgr;
	}

	public static void setDbMgr(DBManager dbMgr) {
		DeveloperTestSuite.dbMgr = dbMgr;
	}

	@BeforeClass
	public static void startSuite() throws Exception {
		// 我会在所有测试类运行之前执行
		logger.info("============这里是测试套件=============");
		// 该语句会执行SQL 重新插入数据
		Thread.sleep(3000);

		dbMgr.cleanDatabase();

		dbMgr.executeSQL(initSQLFile);

	}

	@AfterClass
	public static void endSuite() {
		// 我会在所有测试类运行之后执行
		logger.info("============测试套件执行完毕=============");
	}
}
