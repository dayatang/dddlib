package com.dayatang.datasource4saas;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.datasource4saas.SaasDataSource;
import com.dayatang.datasource4saas.springconf.SpringConfiguration;
import com.dayatang.datasource4saas.tenantservice.ThreadLocalTenantHolder;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.spring.factory.SpringIocUtils;

public class SaasDataSourceIntegrationTest {
	
	private SaasDataSource instance;

	@Before
	public void setUp() throws Exception {
		clearTenantIfExisted();
		//SpringIocUtils.initInstanceProvider("spring/applicationContext.xml");
		SpringIocUtils.initInstanceProvider(SpringConfiguration.class);
		instance = InstanceFactory.getInstance(SaasDataSource.class);
	}

	private void clearTenantIfExisted() {
		ThreadLocalTenantHolder.removeTenant();
	}

	@After
	public void tearDown() throws Exception {
		InstanceFactory.setInstanceProvider(null);
	}

	@Test
	public void test() throws Exception {
		switchTenant("abc");
		assertEquals("China Mobile", getDataFromDb());
		switchTenant("xyz");
		assertEquals("China Unicom", getDataFromDb());
		switchTenant("abc");
		assertEquals("China Mobile", getDataFromDb());
	}

	private void switchTenant(String tenant) {
		ThreadLocalTenantHolder.setTenant(tenant);
	}

	private String getDataFromDb() {
		Connection connection = null;
		try {
			connection = instance.getConnection();
			return useConnection(connection);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			try {
				if (connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}

	private String useConnection(Connection connection) {
		String sql = "SELECT name FROM users WHERE id = 1";
		PreparedStatement stmt = null;
		try {
			stmt = connection.prepareStatement(sql);
			return usePreparedStatement(stmt);
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}

	private String usePreparedStatement(PreparedStatement stmt) {
		ResultSet rs = null;
		try {
			rs = stmt.executeQuery();
			rs.next();
			return rs.getString("name");
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}
		}
	}


}