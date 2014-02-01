package com.dayatang.dbunit;

import org.dbunit.database.IDatabaseConnection;

/**
 * DBUnit回调接口。使用DbUnitTemplate提供的数据连接执行DBUnit操作。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface DbUnitCallback{
	void doInDbUnit(IDatabaseConnection connection) throws Exception;
}
