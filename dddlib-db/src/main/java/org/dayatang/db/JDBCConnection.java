package org.dayatang.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JDBCConnection implements DBConnection{


	private static DBConnection dbConn;
	
	private Connection conn;
	private Statement stemt;
	
	public static DBConnection getJDBCConnection() throws Exception {
		if(dbConn == null){
			dbConn = new JDBCConnection();
		}
		
		return dbConn;
	}

	private JDBCConnection() throws Exception {
		this.conn = DriverManager.getConnection(PropertiesUtil.JDBC_URL, PropertiesUtil.JDBC_USERNAME, PropertiesUtil.JDBC_PASSWD);
		this.stemt = conn.createStatement();
	}

	public boolean execute(String sql) throws Exception {
		return stemt.execute(sql);
	}

	public ResultSet query(String sql) throws Exception {
		return stemt.executeQuery(sql);
	}

	public ResultSet queryRelations(String tableName) throws Exception {
		return conn.getMetaData().getImportedKeys(null, null, tableName);
	}

	public ResultSet queryTables() throws Exception {
		return conn.getMetaData().getTables(null,null,null,new String[] { "TABLE", "VIEW" });
	}

	public List<String> getTableNames() throws Exception {
		List<String> result = new ArrayList<String>();
		ResultSet rs = queryTables();
		while (rs.next()) {
			String tableName = rs.getString(3);
			result.add(tableName);
		}
		return result;
	}
}
