package com.dayatang.db.oracle;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.dayatang.db.AbstractDBManager;
import com.dayatang.db.DBConnection;
import com.dayatang.db.DBManagerUtils;
import com.dayatang.db.DataObject;
import com.dayatang.db.JDBCConnection;

public class OracleDBManager extends AbstractDBManager {

	private Log logger = LogFactory.getLog(OracleDBManager.class);

	private DBConnection jConn;

	public OracleDBManager() throws Exception {
		super();
		jConn = JDBCConnection.getJDBCConnection();
	}

	@Override
	public void cleanDatabase() {
		ResultSet rs = null;
		try {

			// 删除约束关系
			dropConstraint();

			// 获取所有表
			List<DataObject> allTableNames = DBManagerUtils
					.getAllTableNames(jConn);

			// 删除表
			DBManagerUtils.dropTables(jConn, allTableNames);

			logger.info("清理数据库，已删除所有表和视图 !");

		} catch (Exception e) {
			logger.error("cleanDatabase() ", e);
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
		jConn.execute("select 'alter table '||table_name||' drop constraint '||constraint_name||';' from user_constraints where constraint_type='R'");
	}

	@Override
	public void executeSQL(String sqlFile) {

		StringBuffer command = new StringBuffer();

		command.append("cmd /c sqlplus ");
		command.append(username + "/" + password);
		command.append("@" + databaseName);

		command.append(" @" + sqlFile);

		try {
			logger.info("执行SQL文件：" + sqlFile);
			Process process = Runtime.getRuntime().exec(command.toString());
			process.waitFor();

		} catch (IOException e) {
			logger.error("执行SQL文件出错：" + sqlFile, e);
		} catch (InterruptedException e) {
			logger.error("执行SQL文件出错：" + sqlFile, e);
		}

	}

	@Override
	public void exportAll(String filePath) {

		String file = exportFileName(filePath);

		StringBuffer command = getBaseOracleExpSQL(file);
		command.append(" file=" + file);
		command.append(" owner=" + username);

		try {

			logger.info("执行命令：" + command.toString());
			Runtime.getRuntime().exec(command.toString());
			logger.info("已导出数据库中所有表(包括视图)到：" + file);

		} catch (IOException e) {
			logger.error("", e);
		}

	}

	@Override
	public void exportExcludes(String filePath,
			List<String> excludedTablePrefixs) {

		try {
			List<String> excludeds = DBManagerUtils.loadIncludedTableName(
					jConn, excludedTablePrefixs);
			String tables = DBManagerUtils.getString(excludeds);

			if (StringUtils.isBlank(tables)) {
				logger.info("没有任何表或者视图需要导出.");
				return;
			}

			String file = exportFileName(filePath);
			StringBuffer command = getBaseOracleExpSQL(file);

			command.append(" tables=(" + tables + ")");
			command.append(" file=" + file);

			logger.info("执行命令:" + command.toString());
			Runtime.getRuntime().exec(command.toString());
			logger.info("已导出数据库中不是以[ "
					+ DBManagerUtils.getString(excludedTablePrefixs)
					+ " ]前缀开头的表(包括视图)到：" + file);

		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e1) {
			logger.error("", e1);
		}

	}

	@Override
	public void exportIncludes(String filePath,
			List<String> includedTablePrefixs) {

		try {
			List<String> excludeds = DBManagerUtils.loadExcludedTableName(
					jConn, includedTablePrefixs);
			String tables = DBManagerUtils.getString(excludeds);

			if (StringUtils.isBlank(tables)) {
				logger.info("没有任何表或者视图需要导出.");
				return;
			}

			String file = exportFileName(filePath);
			StringBuffer command = getBaseOracleExpSQL(file);

			command.append(" tables=(" + tables + ")");
			command.append(" file=" + file);

			logger.info("执行命令:" + command.toString());
			Runtime.getRuntime().exec(command.toString());
			logger.info("已导出数据库中以[ "
					+ DBManagerUtils.getString(includedTablePrefixs)
					+ " ]前缀开头的表(包括视图)到：" + file);

		} catch (IOException e) {
			logger.error("", e);
		} catch (Exception e1) {
			logger.error("", e1);
		}

	}

	/**
	 * 生成的sql文件名
	 * 
	 * @param filePath
	 * @return
	 */
	private String exportFileName(String filePath) {

		if (filePath.endsWith(".dmp")) {
			return filePath;
		}

		if (!filePath.endsWith("/")) {
			filePath = filePath + "/";
		}

		String fileName = DBManagerUtils.generateUniqueName();

		return filePath + fileName + ".dmp";
	}

	/**
	 * Oracle 基础导出命令
	 * 
	 * @param file
	 * @return
	 */
	private StringBuffer getBaseOracleExpSQL(String file) {

		StringBuffer command = new StringBuffer();

		command.append("cmd /c exp ");
		command.append(username + "/" + password);

		command.append("@" + databaseName);
		command.append(" rows=y");

		return command;

	}

}
