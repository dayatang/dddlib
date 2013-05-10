package com.dayatang.dsrouter.datasource.examples;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;

public class TestTable extends JdbcDaoSupport {
	public List<Item> getItems() {
		String query = "select ID, NAME from TEST_TABLE";
		return getJdbcTemplate().query(query,
				new ParameterizedRowMapper<Item>() {
					public Item mapRow(ResultSet rs, int row)
							throws SQLException {
						Long id = rs.getLong(1);
						String name = rs.getString(2);
						return new Item(id, name);
					}
				});
	}
}
