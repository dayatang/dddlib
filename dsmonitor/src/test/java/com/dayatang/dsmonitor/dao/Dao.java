package com.dayatang.dsmonitor.dao;

import java.util.List;

@SuppressWarnings("rawtypes")
public interface Dao {

	public List listResult(String queryStr, Object... values);

	public List listResultWithoutCloseConnection(String queryStr,
			Object... values);

}
