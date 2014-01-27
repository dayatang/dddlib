package org.openkoala.gqc.web;

import java.sql.Connection;
import java.sql.DriverManager;

import org.openkoala.gqc.infra.util.DatabaseUtils;

public class Test {

	public static void main(String[] args) throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test", "root", "root");
		System.out.println(DatabaseUtils.getColumns(conn, "ks_identity"));
		System.out.println(DatabaseUtils.getDatabaseType(conn));
	}
}
