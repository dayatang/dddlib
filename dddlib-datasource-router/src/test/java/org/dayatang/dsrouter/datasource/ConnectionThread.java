package org.dayatang.dsrouter.datasource;

import java.sql.Connection;

import javax.sql.DataSource;

import org.dayatang.dsrouter.context.memory.ContextHolder;
import org.springframework.context.ApplicationContext;

public class ConnectionThread implements Runnable {

	private ApplicationContext context;

	public ConnectionThread(ApplicationContext context) {
		this.context = context;
	}

	public void run() {
		System.out.println("我运行啦。。。");
		ContextHolder.setContextType("1");
		DataSource ds = (DataSource) context.getBean("dataSource");
		try {
			Connection connection = ds.getConnection();
			connection.setReadOnly(true);
			System.out.println(connection.getCatalog());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
