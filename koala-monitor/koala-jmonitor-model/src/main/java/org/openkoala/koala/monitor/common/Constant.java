package org.openkoala.koala.monitor.common;

public class Constant {

	public static final String TYPE_ONE = "1";

	public static final String TIME_NEGATIVE_ONE = "-1";
	
	/**  按小时/天/月查询  **/
	public static final String UNIT_HOUR = "hour";
	public static final String UNIT_DAY = "day";
	public static final String UNIT_MONTH = "month";
	
	/** 监控类型   **/
	public static final String MONITOR_TYPE_HTTP = "http";
	public static final String MONITOR_TYPE_METHOD = "method";
	public static final String MONITOR_TYPE_JDBC = "jdbc";

	/** 需要统计的项   **/
	/** HTTP统计   **/
	public static final String COUNT_HTTP = "httpCount";
	/** METHOD统计：调用次数、平均耗时、出错次数   **/
	public static final String COUNT_METHOD_COUNT = "methodCount";
	public static final String COUNT_METHOD_AVG_TIME_CONSUME = "avgTimeConsume";
	public static final String COUNT_METHOD_EXCEPTIONS = "methodExceptionCount";
	/** JDBC报表统计：connection数、preparedStatement数、statement数   **/
	public static final String COUNT_CONNECTION = "connectionCount";
	public static final String COUNT_PREPAREDSTATEMENT = "preparedStatementCount";
	public static final String COUNT_STATEMENT = "statementCount";
	/** JDBC表格统计：connection打开/关闭数、preparedStatement创建/关闭数、statement创建/关闭数   **/
	public static final String COUNT_CONNECTION_OPEN = "connectionOpenCount";
	public static final String COUNT_CONNECTION_CLOSE = "connectionCloseCount";
	public static final String COUNT_PREPAREDSTATEMENT_CREATE = "preparedStatementCreateCount";
	public static final String COUNT_PREPAREDSTATEMENT_CLOSE = "preparedStatementCloseCount";
	public static final String COUNT_STATEMENT_CREATE = "statementCreateCount";
	public static final String COUNT_STATEMENT_CLOSE = "statementCloseCount";
	
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
//    public static final String DATABASE_SYSBASE = "sysbase";
    public static final String DATABASE_POSTGRESQL = "postgresql";
    public static final String DATABASE_INFORMIX = "informix";
    public static final String DATABASE_ODBC = "odbc";
	
}
