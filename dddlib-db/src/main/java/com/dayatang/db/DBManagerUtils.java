package com.dayatang.db;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBManagerUtils {
	
	public static Logger LOGGER = LoggerFactory.getLogger(DBManagerUtils.class);
	
	private DBManagerUtils() {
		super();
	}

	/**
	 * 从数据库中获取所有表的集合
	 * 
	 * @return
	 */
	public static List<DataObject> getAllTableNames(DBConnection jConn)
			throws Exception {

		List<DataObject> result = new ArrayList<DataObject>();

		ResultSet rs = jConn.queryTables();
		String username = PropertiesUtil.JDBC_USERNAME.toUpperCase();
		
		while (rs.next()) {
			
			String owner = rs.getString(2);
			
			if(owner != null){
				if(!owner.toUpperCase().equals(username)){
					continue;
				}
			}
			
			DataObject data = new DataObject();

			data.setName(rs.getString(3).toUpperCase());
			data.setType(rs.getString(4).toUpperCase());

			result.add(data);
		}
		rs.close();
		
		return result;
	}
	
	/**
	 * 根据传入的表名集合, 删除表或者视图
	 * 
	 * @param tableNames
	 * @throws Exception
	 */
	public static void dropTables(DBConnection jConn,List<DataObject> tableNames) throws Exception {

		if(tableNames != null && tableNames.size() > 0){
			
			for (int i = 0; i < tableNames.size(); i++) {
				
				DataObject data = tableNames.get(i);
			
				if(data == null || StringUtils.isBlank(data.getName())){
					continue;
				}
				
				if ("VIEW".equals(data.getType())) {
					//删除视图
					jConn.execute("drop view " + data.getName());
				
				}else if ("TABLE".equals(data.getType())) {
					
					//删除表
					jConn.execute("drop table " + data.getName());
				}
			}
		}
	}
	
	public static void colseRs(ResultSet rs) {
		try {
			if(rs != null) {
				rs.clearWarnings();
			}
		} catch (Exception e) {
			error("colseRs() ",e);
		}
	}

	
	/**
	 * 将集合中的表名转换成String形式，以','分隔
	 * @param list 表名集合
	 * @return
	 */
	public static String getString(List<String> list){
		return StringUtils.join(list, ", ");
	}
	
	/**
	 * 获取以指定前缀开头的表名
	 * @param includedTablePrefixs 表名前缀
	 * @return
	 */
	public static List<String> loadIncludedTableName(DBConnection jConn,List<String> includedTablePrefixs) throws Exception{
		
		List<DataObject> tableNames = DBManagerUtils.getAllTableNames(jConn);
		
		List<String> includedTable = new ArrayList<String>();
				
		for(DataObject data:tableNames){
					
			String tableName = data.getName();
			
			for(String prefixs:includedTablePrefixs){
				
				prefixs = prefixs.toUpperCase();
				
				if(tableName.startsWith(prefixs)){
					includedTable.add(tableName);
					break;
				}
			}
					
		}
		return includedTable;
	}
	
	/**
	 * 获取不以指定前缀开头的表名
	 * @param excludedTablePrefixs 表名前缀
	 * @return
	 */
	public static List<String> loadExcludedTableName(DBConnection jConn,List<String> excludedTablePrefixs) throws Exception{
		
		List<DataObject> tableNames = DBManagerUtils.getAllTableNames(jConn);
		List<String> excludedTable = new ArrayList<String>();
				
		for(DataObject data:tableNames){
					
			String tableName = data.getName();
			
			boolean isExclude = false;
			
			for(String excluded:excludedTablePrefixs){
				
				excluded = excluded.toUpperCase();
				
				if(tableName.startsWith(excluded)){
					isExclude = true;
					break;
				}
			}
			
			if(! isExclude){
				excludedTable.add(tableName);
			}
					
		}
		return excludedTable;
	}
	
	/**
	 * 生成日期格式的唯一字符串
	 * @return
	 */
	public static String generateUniqueName(){
		Calendar c = Calendar.getInstance();
		StringBuffer fileName = new StringBuffer();
		
		fileName.append(c.get(Calendar.YEAR));
		fileName.append(c.get(Calendar.MONTH) + 1);
		
		fileName.append(c.get(Calendar.DAY_OF_MONTH));
		fileName.append(c.get(Calendar.HOUR_OF_DAY));

		fileName.append(c.get(Calendar.MINUTE));
		fileName.append(c.get(Calendar.SECOND));
		fileName.append(c.get(Calendar.MILLISECOND));
		
		return fileName.toString();
	}

	private static void error(String message, Object... params) {
		if (LOGGER.isErrorEnabled()) {
			LOGGER.error(message, params);
		}
	}
}
