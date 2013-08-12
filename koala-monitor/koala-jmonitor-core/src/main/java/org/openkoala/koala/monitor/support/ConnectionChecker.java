package org.openkoala.koala.monitor.support;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import org.openkoala.koala.monitor.common.Constant;
import org.openkoala.koala.monitor.jwebap.ThirdPartyServiceDef;

public class ConnectionChecker {
    
	
	/**
	 * 检查连接状态
	 * @param service  服务定义
	 * @return  int[状态,耗时](状态：成功=1，失败=2；耗时：单位毫秒)
	 */
	public static int[] checkConnectionStatus(ThirdPartyServiceDef service){
        if(Constant.SERVICE_TYPE_WEBSERVICE.equalsIgnoreCase(service.getType())){
            return checkHttp(service);
        }else if(Constant.SERVICE_TYPE_SOCKET.equalsIgnoreCase(service.getType())
                || Constant.SERVICE_TYPE_EJB.equalsIgnoreCase(service.getType())){
            return checkSocket(service);
        }else if(Constant.SERVICE_TYPE_DATABSSE.equalsIgnoreCase(service.getType())){
        	return checkDatabase(service);
        }
        return new int[2];
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
    public static boolean isConnect(ThirdPartyServiceDef service){
        if(Constant.SERVICE_TYPE_WEBSERVICE.equalsIgnoreCase(service.getType())){
            return checkHttp(service)[0] == 1;
        }else if(Constant.SERVICE_TYPE_SOCKET.equalsIgnoreCase(service.getType())
                || Constant.SERVICE_TYPE_EJB.equalsIgnoreCase(service.getType())){
            return checkSocket(service)[0] == 1;
        }else if(Constant.SERVICE_TYPE_DATABSSE.equalsIgnoreCase(service.getType())){
        	return checkDatabase(service)[0] == 1;
        }
        return false;
    }
    
    
    
    /**
     * 检测http连接
     * @param service
     * @return
     */
    private static int[] checkHttp(ThirdPartyServiceDef service){
    	long beginTime = System.currentTimeMillis();
    	int count = 1;
    	int[] states = new int[2];
    	URL url = null;
        try {
            url = new URL(service.getTarget());
        } catch (MalformedURLException e) {
            //urlStr字符串指定未知的协议
//            System.out.println("urlStr字符串指定未知的协议");
            return states;
        }
        
        while( count <= 3 ){
            try {
            	HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(service.getTimeout());
                states[0] = connection.getResponseCode() == 200 ? 1 : 0;
                if(states[0] == 1)break;
            } catch (Exception e1) {
                //超时等异常
                count++;
                try {
                    //循环检测连接的时间间隔设置为1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {}

            }
        }
        
        states[1] = (int) (System.currentTimeMillis() - beginTime - 1000*(count - 1))/count;
        return states;
    }
    
    /**
     * 检测socket连接
     * @param service
     * @return
     */
    private static int[] checkSocket(ThirdPartyServiceDef service){
    	long beginTime = System.currentTimeMillis();
    	int[] states = new int[2];
        String[] urlStr = service.getTarget().split(":");
        try {
            String host = urlStr[0];
            int port = urlStr.length == 2 ? Integer.parseInt(urlStr[1]) : 80;
			Socket client = new Socket(host,port);
            client.close();
            states[0] = 1;
        } catch (Exception e) {
        	states[0] = 0;
        }
        states[1] = (int) (System.currentTimeMillis() - beginTime);
        return states;
    }
    
    
    /**
     * 检查数据库连接
     * @param service
     * @return int[状态,耗时]
     */
    private static int[] checkDatabase(ThirdPartyServiceDef service){
    	long beginTime = System.currentTimeMillis();
    	int[] states = new int[2];
        try {
            String[] url = service.getTarget().split(":");
            //加载数据驱动
            if(Constant.DATABASE_MYSQL.equalsIgnoreCase(url[1])){
                Class.forName("com.mysql.jdbc.Driver").newInstance();
            }else if(Constant.DATABASE_ORACLE.equalsIgnoreCase(url[1])){
                Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
            }else if(Constant.DATABASE_SQLSERVER.equalsIgnoreCase(url[2])){
                try{
                    //3.0驱动
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver").newInstance();
                }catch(Exception e){
                    //2.0驱动
                    Class.forName("com.microsoft.jdbc.sqlserver.SQLServerDriver").newInstance();
                }
            }else if(Constant.DATABASE_DB2.equalsIgnoreCase(url[1])){
                try{
                    //连接具有DB2客户端的Provider实例
                    Class.forName("com.ibm.db2.jdbc.app.DB2.Driver").newInstance();
                }catch(Exception e){
                    //连接不具有DB2客户端的Provider实例
                    Class.forName("com.ibm.db2.jdbc.net.DB2.Driver").newInstance();
                }
            }/*else if(Constant.DATABASE_SYSBASE.equalsIgnoreCase(url[1])){
                Class.forName("com.sybase.jdbc.SybDriver").newInstance();
            }*/else if(Constant.DATABASE_POSTGRESQL.equalsIgnoreCase(url[1])){
                Class.forName("org.postgresql.Driver").newInstance();
            }else if(Constant.DATABASE_INFORMIX.equalsIgnoreCase(url[1].split("-")[0])){
                Class.forName("com.informix.jdbc.IfxDriver").newInstance();
            }else if(Constant.DATABASE_ODBC.equalsIgnoreCase(url[1])){
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver").newInstance();
            }
            
            //获取数据库连接
            Connection conn = DriverManager.getConnection(service.getTarget(), service.getUsername(), service.getPassword());
            //关闭连接
            conn.close();
            states[0] = 1;
        } catch (Exception e) {
        	states[0] = 0;
        }
        states[1] = (int) (System.currentTimeMillis() - beginTime);
        return states;
    }
    
    public static void main(String[] arg){
        ThirdPartyServiceDef service = new ThirdPartyServiceDef();
        service.setType(Constant.SERVICE_TYPE_DATABSSE);
        service.setTarget("jdbc:mysql://localhost:3309/opendb");
        service.setUsername("root");
        service.setPassword("root");
        
        ConnectionChecker uRLCheckUtil = new ConnectionChecker();
       uRLCheckUtil.checkDatabase(service);
    }
    
}
