package org.openkoala.koala.deploy.db2entity;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.openkoala.koala.deploy.db2entity.vo.DBColumnVO;
import org.openkoala.koala.deploy.db2entity.vo.PrimaryKeyColumnVO;

/**
 * 从数据库生成实体的工具类
 * 
 * @author lingen
 * 
 */
public class DB2EntityUtil {

	private DBClassLoaderUtil classLoaderUtil;

	private String jdbcDriver;

	private String connectionURL;

	private String username;

	private String password;

	private Connection conn;

	private static DB2EntityUtil util = new DB2EntityUtil();
	
	public void init(String jdbcDriver,
			String connectionURL, String username, String password,String driverUrl) {
//		DB2EntityUtil dbUtil = new DB2EntityUtil(jdbcDriver, connectionURL,
//				username, password);
		this.jdbcDriver = jdbcDriver;
		this.connectionURL = connectionURL;
		this.username = username;
		this.password = password;
		classLoaderUtil = DBClassLoaderUtil.getCURDClassLoader(driverUrl);
	}
	
	public static DB2EntityUtil getDB2EntityUtil(){
		return util;
	}
	
	private DB2EntityUtil(){
		
	}

	
	public void refreshDriver(String urlPath){
		classLoaderUtil = DBClassLoaderUtil.getCURDClassLoader(urlPath);
	}

	public void connect() throws Exception {
		Class clazz = classLoaderUtil.forName(jdbcDriver);
		Driver driver = (Driver) clazz.newInstance();
		Properties p = new Properties();
		p.put("user", username);
		p.put("password", password);
		conn = driver.connect(connectionURL, p);
	}

	public void closeConn() throws Exception {
		if (conn != null)
			conn.close();
	}

	public List<DBColumnVO> getColumns(String tableName) throws Exception {

		ResultSet rs = null;
		List<DBColumnVO> columns = new ArrayList<DBColumnVO>();
		DatabaseMetaData metaData = conn.getMetaData();
	
		// 查询主键
		rs = metaData.getPrimaryKeys(null, null, tableName);
		Map<String, PrimaryKeyColumnVO> primaryKeys = new HashMap<String, PrimaryKeyColumnVO>();

		while (rs.next()) {
			PrimaryKeyColumnVO vo = new PrimaryKeyColumnVO();
			vo.setPrimarySeq(rs.getInt("KEY_SEQ"));
			vo.setPkName(rs.getString("PK_NAME"));

			primaryKeys.put(rs.getString("COLUMN_NAME"), vo);
		}

		rs = metaData.getColumns(null, null, tableName, null);
		while (rs.next()) {

			String columnName = rs.getString("COLUMN_NAME");
			DBColumnVO vo = null;
			if (primaryKeys.containsKey(columnName)) {
				vo = primaryKeys.get(columnName);
				((PrimaryKeyColumnVO) vo).setAutoIncrement(rs.getString(
						"IS_AUTOINCREMENT").equals("YES"));
			} else {
				vo = new DBColumnVO();
			}
			vo.setColumnName(columnName);
			vo.setDataType(rs.getInt("DATA_TYPE"));
			vo.setRemarks(rs.getString("REMARKS"));
			vo.setTypeName(rs.getString("TYPE_NAME"));
			columns.add(vo);
		}
		return columns;
	}

	/**
	 * 获取数据库下的所有表
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> getTables() throws Exception {
		List<String> tables = new ArrayList<String>();
		ResultSet rs = null;
		DatabaseMetaData metaData = conn.getMetaData();
		rs = metaData.getTables(null, null, null, new String[] { "TABLE" });
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			tables.add(tableName);
		}
		return tables;
	}

	/**
	 * 根据表格生成一个实体类
	 * 
	 * @param path
	 * @param tableName
	 * @param packageName
	 * @throws Exception
	 */
	public void generateEntity(String path, String tableName, String packageName)
			throws Exception {
		List<DBColumnVO> columns = getColumns(tableName);
		EntityGenerateUtil.process(path, tableName, packageName, columns);
	}

	public static void main(String args[]) throws Exception {
		DB2EntityUtil util =  DB2EntityUtil.getDB2EntityUtil();
		util.init("org.h2.Driver",
				"jdbc:h2:~/demo", "sa", "","/Users/lingen/.m2/repository/com/h2database/h2/1.3.171/h2-1.3.171.jar");
		util.connect();
		System.out.println(util.getTables());
		
	
		util.closeConn();
	}

}
