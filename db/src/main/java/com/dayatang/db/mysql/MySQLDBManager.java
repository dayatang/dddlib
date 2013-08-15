package com.dayatang.db.mysql;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dayatang.db.AbstractDBManager;
import com.dayatang.db.DBConnection;
import com.dayatang.db.DBManagerUtils;
import com.dayatang.db.DataObject;
import com.dayatang.db.JDBCConnection;

public class MySQLDBManager extends AbstractDBManager {

	private static Logger LOGGER = LoggerFactory.getLogger(MySQLDBManager.class);

	private DBConnection jConn;

	public MySQLDBManager() throws Exception {
		super();
		jConn = JDBCConnection.getJDBCConnection();
	}

	@Override
	public void cleanDatabase() {

		ResultSet rs = null;
		try {
			dropConstraint();

			// 获取所有表
			List<DataObject> allTableNames = DBManagerUtils.getAllTableNames(jConn);

			// 删除表
			DBManagerUtils.dropTables(jConn, allTableNames);

			info("清理数据库，已删除所有表和视图  !");

		} catch (Exception e) {
			error("cleanDatabase()", e);

		} finally {
			DBManagerUtils.colseRs(rs);
		}
	}

	/**
	 * 删除关联
	 * 
	 * @throws Exception
	 */
	private void dropConstraint() throws Exception {
		jConn.execute("SET FOREIGN_KEY_CHECKS=0;");
	}

	@Override
	public void executeSQL(String sqlFile) {

		StringBuffer sb = new StringBuffer();

		sb.append("cmd /c mysql");
		sb.append(" -h " + hostName);

		sb.append(" -u" + username);
		sb.append(" -p" + password);

		sb.append(" " + databaseName + " < ");
		sb.append(sqlFile);

		try {
			Process process = Runtime.getRuntime().exec(sb.toString());
			process.waitFor();

			info("已执行完SQL脚本：" + sqlFile);

		} catch (IOException e) {
			error("executeSQL() ", e);
		} catch (InterruptedException e) {
			error("executeSQL() ", e);
		}

	}

	@Override
	public void exportAll(String filePath) {

		StringBuffer commond = getBaseMySQLDumpCommond();

		String file = exportFileName(filePath);
		commond.append(" > " + file);

		try {
			Runtime.getRuntime().exec(commond.toString());

			info("已导出数据库中所有表(包括视图)到：" + file);

		} catch (IOException e) {
			error("exportAll() ", e);
		}

	}

	@Override
	public void exportExcludes(String filePath, List<String> excludedTablePrefixs) {

		try {

			List<String> loadExcludedTableName = DBManagerUtils.loadExcludedTableName(jConn, excludedTablePrefixs);

			if (loadExcludedTableName == null || loadExcludedTableName.size() <= 0) {
				info("没有任何表需要导出.");
				return;
			}

			StringBuffer commond = getBaseMySQLDumpCommond();
			commond.append(" --tables ");

			for (String name : loadExcludedTableName) {
				commond.append(name + " ");
			}

			String file = exportFileName(filePath);
			commond.append(" > " + file);

			Runtime.getRuntime().exec(commond.toString());

			info("已导出数据库中不是以[ " + DBManagerUtils.getString(excludedTablePrefixs) + " ]前缀开头的表(包括视图)到：" + file);

		} catch (IOException e) {
			error("exportIncludes() ", e);
		} catch (Exception e1) {
			error(e1.getMessage());
		}
	}

	@Override
	public void exportIncludes(String filePath, List<String> includedTablePrefixs) {
		try {
			List<String> includedTableName = DBManagerUtils.loadIncludedTableName(jConn, includedTablePrefixs);

			if (includedTableName == null || includedTableName.size() <= 0) {
				info("没有任何表需要导出.");
				return;
			}

			StringBuffer commond = getBaseMySQLDumpCommond();
			commond.append(" --tables ");

			for (String name : includedTableName) {
				commond.append(name + " ");
			}

			String file = exportFileName(filePath);
			commond.append(" > " + file);

			Runtime.getRuntime().exec(commond.toString());
			info("已导出数据库中以[ " + DBManagerUtils.getString(includedTablePrefixs) + " ]前缀开头的表(包括视图)到：" + file);

		} catch (IOException e) {
			error("exportIncludes() ", e);
		} catch (Exception e1) {
			error("exportIncludes() ", e1);
		}

	}

	/**
	 * 生成的sql文件名
	 * 
	 * @param filePath
	 * @return
	 */
	private String exportFileName(String filePath) {

		if (filePath.endsWith(".sql")) {
			return filePath;
		}

		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}

		String fileName = DBManagerUtils.generateUniqueName();

		return filePath + fileName + ".sql";
	}

	/**
	 * MySQl基础导出命令
	 * 
	 * @return
	 */
	private StringBuffer getBaseMySQLDumpCommond() {

		StringBuffer commond = new StringBuffer();

		commond.append("cmd /c mysqldump");
		commond.append(" -h " + hostName);

		commond.append(" -u" + username);
		commond.append(" -p" + password);

		commond.append(" " + databaseName);

		return commond;
	}

	private static void info(String message, Object... params) {
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info(message, params);
		}
	}

	private static void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}

}
