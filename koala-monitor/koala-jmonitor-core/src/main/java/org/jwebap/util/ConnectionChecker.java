package org.jwebap.util;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;

import org.openkoala.koala.monitor.common.Constant;
import org.openkoala.koala.monitor.jwebap.ThirdPartyServiceDef;

public class ConnectionChecker {
    
    private URL url;
    
    private HttpURLConnection connection;
    
    int count = 0;
    int state = -1;
    
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
    public boolean isConnect(ThirdPartyServiceDef service){
        if(Constant.SERVICE_TYPE_WEBSERVICE.equalsIgnoreCase(service.getType())){
            return this.checkHttp(service);
        }else if(Constant.SERVICE_TYPE_SOCKET.equalsIgnoreCase(service.getType())
                || Constant.SERVICE_TYPE_EJB.equalsIgnoreCase(service.getType())){
            return this.checkSocket(service);
        }else if(Constant.SERVICE_TYPE_DATABSSE.equalsIgnoreCase(service.getType())){
            this.checkDatabase(service);
        }
        return false;
    }
    
    /**
     * 检测http连接
     * @param service
     * @return
     */
    private boolean checkHttp(ThirdPartyServiceDef service){
        try {
            url = new URL(service.getTarget());
        } catch (MalformedURLException e) {
            //urlStr字符串指定未知的协议
//            System.out.println("urlStr字符串指定未知的协议");
            return false;
        }
        
        while( count < 5 ){
//            System.out.println("尝试第" + (count+1) + "次连接..");
            try {
                connection = (HttpURLConnection)url.openConnection();
                connection.setConnectTimeout(service.getTimeout());
                state = connection.getResponseCode();
                break;
            } catch (Exception e1) {
                //超时等异常
                count++;
                try {
                    //循环检测连接的时间间隔设置为1秒
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    //
                }
                continue;
            }
        }
        
        return state==200;
    }
    
    /**
     * 检测socket连接
     * @param service
     * @return
     */
    private boolean checkSocket(ThirdPartyServiceDef service){
        String[] urlStr = service.getTarget().split(":");
        try {
            Socket client = new Socket(urlStr[0],Integer.parseInt(urlStr[1]));
            client.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    
    /**
     * 检测database连接
     * @param service
     * @return
     */
    private boolean checkDatabase(ThirdPartyServiceDef service){
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
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static void main(String[] arg){
        ThirdPartyServiceDef service = new ThirdPartyServiceDef();
        service.setType(Constant.SERVICE_TYPE_DATABSSE);
        service.setTarget("jdbc:mysql://localhost:3309/opendb");
        service.setUsername("root");
        service.setPassword("root");
        
        ConnectionChecker uRLCheckUtil = new ConnectionChecker();
        boolean result = uRLCheckUtil.checkDatabase(service);
        System.out.println(result);
    }
    
}
