package org.openkoala.gqc.core.domain.utils;

/**
 * 分页查询方言解析
 * @author xmfang
 *
 */
public class PagingQueryDialectResolver {
	
	private PagingQueryDialectResolver() {
		
	}
	
	/**
	 * 根据数据库名称获得一个对应的分页查询方言的实例
	 * @param databaseName
	 * @return
	 */
	public static PagingQueryDialect getPagingQuerierInstance(String databaseName) {

		if ( "H2".equals( databaseName ) ) {
			return new H2PagingQueryDialect();
		}

		if ( "MySQL".equals( databaseName ) ) {
			return new MySqlPagingQueryDialect();
		}

		if ( "Oracle".equals( databaseName ) ) {
			return new OraclePagingQueryDialect();
		}

		if ( databaseName.startsWith( "Microsoft SQL Server" ) ) {
			return new SqlServerPagingQueryDialect();
		}
		
		if ( databaseName.startsWith( "DB2/" ) ) {
			return new DB2PagingQueryDialect();
		}

		/*if ( "Sybase SQL Server".equals( databaseName ) || "Adaptive Server Enterprise".equals( databaseName ) ) {
			return new SybaseASE15Dialect();
		}

		if ( databaseName.startsWith( "Adaptive Server Anywhere" ) ) {
			return new SybaseAnywhereDialect();
		}*/

		return null;
		
	}

}
