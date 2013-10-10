package org.openkoala.gqc.infra.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库工具类
 * @author zyb
 * @since 2013-7-9 上午9:41:00
 */
public class DatabaseUtils {

	/**
	 * 获取数据库下的所有表
	 * @param conn
	 * @return
	 */
	public static List<String> getTables(Connection conn) {
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		List<String> result = new ArrayList<String>();
		try {
			if (getDatabaseMetaData(conn).getDatabaseProductName().equalsIgnoreCase("oracle")) {
				pstmt = conn.prepareStatement("select table_name from user_tables");
				rs = pstmt.executeQuery();
				while (rs.next()) {
					result.add(rs.getString("TABLE_NAME"));
				}
				return result;
			}
			
			rs = getDatabaseMetaData(conn).getTables(null, null, null, new String[] { "TABLE" });
			while (rs.next()) {
				result.add(rs.getString("TABLE_NAME"));
			}
		} catch (Exception e) {
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
				if (pstmt != null) {
					pstmt.close();
				}
				if(conn != null){
					conn.close();
				}
			} catch (SQLException e) {
			}
		}
		return result;
	}

	/**
	 * 获取表下的所有列
	 * @param conn
	 * @param tableName
	 * @return
	 */
	public static Map<String, Integer> getColumns(Connection conn, String tableName) {
		ResultSet rs = null;
		Map<String, Integer> result = new HashMap<String, Integer>();
		try {
			rs = getDatabaseMetaData(conn).getColumns(null, null, tableName, null);
			while (rs.next()) {
				result.put(rs.getString("COLUMN_NAME"), rs.getInt("DATA_TYPE"));
			}
		} catch (SQLException e) {
		} finally {
			try {
				if(rs != null){
					rs.close();
				}
			} catch (SQLException e) {
			}
		}
		return result;
	}
	
	/**
	 * 获取数据库类型
	 * @param conn
	 * @return
	 */
	public static String getDatabaseType(Connection conn) {
		try {
			return getDatabaseMetaData(conn).getDatabaseProductName();
		} catch (SQLException e) {
		}
		return null;
	}
	
	/**
	 * 获取数据库元数据
	 * @param conn
	 * @return
	 * @throws SQLException
	 */
	private static DatabaseMetaData getDatabaseMetaData(Connection conn) throws SQLException {
		return conn.getMetaData();
	}
	
}
