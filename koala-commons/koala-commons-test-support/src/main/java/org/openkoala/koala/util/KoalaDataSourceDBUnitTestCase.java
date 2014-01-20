package org.openkoala.koala.util;

import javax.sql.DataSource;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.IDatabaseTester;

import com.dayatang.domain.InstanceFactory;

public abstract class KoalaDataSourceDBUnitTestCase extends KoalaDBUnitTestCase {

	@Override
	public abstract String getDataXmlFile();

	@Override
	public IDatabaseTester getIDatabaseTester() {
		return new DataSourceDatabaseTester(InstanceFactory.getInstance(DataSource.class,
				getDataSourceName()));
	}

	public abstract String getDataSourceName();

}
