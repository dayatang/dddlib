package org.openkoala.koala.monitor.support;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import org.openkoala.koala.monitor.def.ServiceDef;
import org.openkoala.koala.monitor.model.CheckResult;

public class ConnectionChecker {
    
	/** 检测连接的服务类型   **/
	public static final String SERVICE_TYPE_SOCKET = "socket";
    public static final String SERVICE_TYPE_WEBSERVICE = "webservice";
    public static final String SERVICE_TYPE_EJB = "ejb";
    public static final String SERVICE_TYPE_DATABSSE = "databsse";
	
    /** 数据库种类   **/
    public static final String DATABASE_MYSQL = "mysql";
    public static final String DATABASE_ORACLE = "oracle";
    public static final String DATABASE_DB2 = "db2";
    public static final String DATABASE_SQLSERVER = "sqlserver";
    public static final String DATABASE_SYSBASE = "sysbase";
    public static final String DATABASE_POSTGRESQL = "postgresql";
    public static final String DATABASE_INFORMIX = "informix";
    public static final String DATABASE_ODBC = "odbc";
	
    
    /**
     * 检查服务运行状态
     * @param service 
     * @param errLoopCount  发生错误循环检测次数
     * @param loopWait  循环等待时间（毫秒）
     * @return
     */
    public static CheckResult checkConnectionStatus(ServiceDef service,int errLoopCount,long loopWait){
    	int count = 1;
    	CheckResult result = null;
    	while(count <= errLoopCount){
    		result = checkConnectionStatus(service);
    		//TODO 如果不是无法访问，就不循环了（该规则待确认）
    		if(!result.isUnavailable())break;
    	}
    	return result;
    }
    
	/**
	 * 检查连接状态
	 * @param service  服务定义
	 * @return  CheckResult
	 */
	public static CheckResult checkConnectionStatus(ServiceDef service){
        if(SERVICE_TYPE_WEBSERVICE.equalsIgnoreCase(service.getType())){
            return checkHttp(service);
        }else if(SERVICE_TYPE_SOCKET.equalsIgnoreCase(service.getType())
                || SERVICE_TYPE_EJB.equalsIgnoreCase(service.getType())){
            return checkSocket(service);
        }else if(SERVICE_TYPE_DATABSSE.equalsIgnoreCase(service.getType())){
        	return checkDatabase(service);
        }
        throw new RuntimeException("not support");
	}
	
    /**
     * 功能描述 : 检测当前URL是否可连接或是否有效,
     * 最多连接网络 5 次, 如果 5 次都不成功说明该地址不存在或视为无效地址.
     *
     * @param type
     *            服务类型：socket,webservice,ejb,databsse
     * @param urlStr
     *            指定URL网络地址
     *
     * @return true:连接成功    false:连接失败
     */
    public static boolean isConnect(ServiceDef service){
        if(SERVICE_TYPE_WEBSERVICE.equalsIgnoreCase(service.getType())){
            return checkHttp(service).isSuccess();
        }else if(SERVICE_TYPE_SOCKET.equalsIgnoreCase(service.getType())
                || SERVICE_TYPE_EJB.equalsIgnoreCase(service.getType())){
            return checkSocket(service).isSuccess();
        }else if(SERVICE_TYPE_DATABSSE.equalsIgnoreCase(service.getType())){
        	return checkDatabase(service).isSuccess();
        }
        return false;
    }
    
    
    
    /**
     * 检测http连接
     * @param service
     * @return
     */
    private static CheckResult checkHttp(ServiceDef service){
    	long beginTime = System.currentTimeMillis();
    	CheckResult result = new CheckResult();
    	URL url = null;
        try {
            url = new URL(service.getTarget());
        } catch (MalformedURLException e) {
        	result.setSuccess(false);
    		result.setDetails("非法URL");
            return result;
        }

        try {
        	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setConnectTimeout(service.getTimeout());
            result.setSuccess(connection.getResponseCode() == 200);
        } catch (Exception e1) {
        	exceptionAction(result, e1);
        }
    
        
        result.setTimeConsuming(System.currentTimeMillis() - beginTime);
        return result;
    }
    
    /**
     * 检测socket连接
     * @param service
     * @return
     */
    private static CheckResult checkSocket(ServiceDef service){
    	long beginTime = System.currentTimeMillis();
    	CheckResult result = new CheckResult();
        String[] urlStr = service.getTarget().split(":");
        try {
            String host = urlStr[0];
            int port = urlStr.length == 2 ? Integer.parseInt(urlStr[1]) : 80;
			Socket client = new Socket(host,port);
            client.close();
            result.setSuccess(true);
        } catch (Exception e) {
        	exceptionAction(result, e);
        }
        result.setTimeConsuming(System.currentTimeMillis() - beginTime);
        return result;
    }
    
    
    /**
     * 检查数据库连接
     * @param service
     * @return int[状态,耗时]
     */
    private static CheckResult checkDatabase(ServiceDef service){
    	long beginTime = System.currentTimeMillis();
    	CheckResult result = new CheckResult();
    	
    	Connection conn = null;
        try {
            String[] url = service.getTarget().split(":");
            //加载数据驱动
            if(DATABASE_MYSQL.equalsIgnoreCase(url[1])){
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }else if(DATABASE_ORACLE.equalsIgnoreCase(url[1])){
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            }else if(DATABASE_SQLSERVER.equalsIgnoreCase(url[2])){
                try{
                    //3.0驱动
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                }catch(Exception e){
                    //2.0驱动
                    Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
                }
            }else if(DATABASE_DB2.equalsIgnoreCase(url[1])){
                try{
                    //连接具有DB2客户端的Provider实例
                    Class.forName("com.ibm.db2.jdbc.app.DB2.Driver").newInstance();
                }catch(Exception e){
                    //连接不具有DB2客户端的Provider实例
                    Class.forName("com.ibm.db2.jdbc.net.DB2.Driver").newInstance();
                }
            }/*else if(DATABASE_SYSBASE.equalsIgnoreCase(url[1])){
                Class.forName("com.sybase.jdbc.SybDriver").newInstance();
            }*/else if(DATABASE_POSTGRESQL.equalsIgnoreCase(url[1])){
                Class.forName("org.postgresql.Driver").newInstance();
            }else if(DATABASE_INFORMIX.equalsIgnoreCase(url[1].split("-")[0])){
                Class.forName("com.informix.jdbc.IfxDriver").newInstance();
            }else if(DATABASE_ODBC.equalsIgnoreCase(url[1])){
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            }
            
            //获取数据库连接
            conn = DriverManager.getConnection(service.getTarget(), service.getUsername(), service.getPassword());
            /*String testSql = service.getParameter("testSQL");
            if(testSql != null){
            	Statement statement = conn.createStatement();
            	ResultSet rs = statement.executeQuery(testSql);
            	rs.next();
            	rs.close();
            	statement.close();
            }*/
            result.setSuccess(true);
        } catch (Exception e) {
        	exceptionAction(result, e);
        }finally{
        	try {conn.close();} catch (Exception e2) {}
        }
        result.setTimeConsuming(System.currentTimeMillis() - beginTime);
        return result;
    }

	private static void exceptionAction(CheckResult result, Exception e) {
		result.setSuccess(false);
		result.setDetails(e.getMessage());
	}
    
    public static void main(String[] arg){
        ServiceDef service = new ServiceDef();
        service.setType(SERVICE_TYPE_DATABSSE);
        service.setTarget("jdbc:mysql://localhost:3309/opendb");
        service.setUsername("root");
        service.setPassword("root");

    }
    
}
