package com.dayatang.domain;

import static org.junit.Assert.*;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.dayatang.domain.AbstractEntity;

public class AbstractEntityTest {

	private AbstractEntity abstractEntity;
	
	@Before
	public void setUp() throws Exception {
		abstractEntity = new AbstractEntity() {

			private static final long serialVersionUID = 6071678807201286598L;

			@Override
			public boolean equals(Object other) {
				return false;
			}

			@Override
			public int hashCode() {
				return 0;
			}

			@Override
			public String toString() {
				return null;
			}
			
		};
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIdAccessor() {
		Long id = 3L;
		abstractEntity.setId(id);
		assertEquals(id, abstractEntity.getId());
	}

	@Test
	public void testVersionAccessor() {
		int version = 10;
		abstractEntity.setVersion(version);
		assertEquals(version, abstractEntity.getVersion());
	}

	@Test
	public void testIsNew() {
		assertTrue(abstractEntity.isNew());
		abstractEntity.setId(3L);
		assertFalse(abstractEntity.isNew());
	}

	@Test
	public void testValidateFailure() {
		
	}
}
