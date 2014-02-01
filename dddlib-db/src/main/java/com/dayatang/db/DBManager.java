package com.dayatang.db;

import java.util.List;

/**
 * @author chencao
 *
 */
public interface DBManager {

	/**
	 * 执行SQL脚本
	 * 
	 * @param sqlFile SQL脚本路径
	 */
	public void executeSQL(String sqlFile);

	/**
	 * 清理数据库，删除所有表和视图
	 */
	public void cleanDatabase();

	/**
	 * 导出数据库中所有表（包括视图）到指定路径
	 * 
	 * @param filePath 指定路径
	 */
	public void exportAll(String filePath);

	/**
	 * 导出数据库中不是以excludedTablePrefixs前缀开头的表（包括视图）到指定路径
	 * 
	 * @param filePath 指定导出路径
	 * @param excludedTablePrefixs 被排除的前缀
	 */
	public void exportExcludes(String filePath, List<String> excludedTablePrefixs);

	/**
	 * 导出数据库中以includedTablePrefixs前缀开头的表（包括视图）到指定路径
	 * 
	 * @param filePath 指定导出路径
	 * @param includedTablePrefixs 包含的前缀
	 */
	public void exportIncludes(String filePath, List<String> includedTablePrefixs);

}
