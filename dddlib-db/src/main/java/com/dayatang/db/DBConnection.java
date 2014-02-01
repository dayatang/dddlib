package com.dayatang.db;

import java.sql.ResultSet;
import java.util.List;

public interface DBConnection {

	boolean execute(String sql) throws Exception;
	
	ResultSet query(String sql) throws Exception;
	
	ResultSet queryRelations(String tableName) throws Exception;
	
	ResultSet queryTables() throws Exception;
	
	public List<String> getTableNames() throws Exception;
	
}
