package com.dayatang.mysql.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

import com.dayatang.mysql.jdbc.GeminiReplicationDriver;

public class ReplicationDriverDemo {
	public static void main(String[] args) throws Exception {
		
		GeminiReplicationDriver driver = new GeminiReplicationDriver();

	    //
	    // Looks like a normal MySQL JDBC url, with a
	    // comma-separated list of hosts, the first
	    // being the 'master', the rest being any number
	    // of slaves that the driver will load balance against
	    //

	    Connection conn = driver.connect("jdbc:mysql://192.168.0.239/test", dbProps());

	    //
	    // Perform read/write work on the master
	    // by setting the read-only flag to "false"
	    //

//	    conn.setReadOnly(false);
//	    conn.setAutoCommit(false);
//	    conn.createStatement().executeUpdate("update cc set name='cc2' where id=1;");
//	    conn.commit();

	    //
	    // Now, do a query from a slave, the driver automatically picks one
	    // from the list
	    //

	    conn.setReadOnly(false);
	    ResultSet rs =
	      conn.createStatement().executeQuery("SELECT name FROM cc where id=1");

	    while(rs.next()) {
	    	System.out.println(rs.getString(1));
	    }
	    
	  }
	
	private static Properties dbProps() {

	    Properties props = new Properties();

	    // We want this for failover on the slaves
	    props.put("autoReconnect", "true");

	    // We want to load balance between the slaves
	    props.put("roundRobinLoadBalance", "true");

	    props.put("user", "root");
	    props.put("password", "123456");
	    return props;
		
	}
}
