package org.openkoala.gqc.application.impl;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openkoala.gqc.core.domain.DataSource;

public class DataSourceApplicationImplTest {
	
	private static List<DataSource> list;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		list = new ArrayList<DataSource>();
		for(int i=0; i<5; i++){
			
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDataSourceVoByDataSourceId() {
		fail("Not yet implemented");
	}

	@Test
	public void testSaveDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveDataSources() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testFindAllColumn() {
		fail("Not yet implemented");
	}

	@Test
	public void testPageQueryDataSource() {
		fail("Not yet implemented");
	}

	@Test
	public void testTestConnection() {
		fail("Not yet implemented");
	}

}
