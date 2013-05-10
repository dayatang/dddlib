package com.dayatang.utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IntRangeTest {
	
	private IntRange range;

	@Before
	public void setUp() throws Exception {
		range = new IntRange(1, 10);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testContains() {
		assertTrue(range.contains(7));
		assertTrue(range.contains(1));
		assertTrue(range.contains(10));
		assertFalse(range.contains(0));
		assertFalse(range.contains(-1));
		assertFalse(range.contains(11));
	}
	
	@Test
	public void testEqualsObject() {
		IntRange range1 = new IntRange(1, 10);
		assertEquals(range1, range);
		range1 = new IntRange(1, 11);
		assertFalse(range1.equals(range));
		range1 = new IntRange(2, 10);
		assertFalse(range1.equals(range));
	}

	@Test
	public void testToString() {
		assertEquals("[1 - 10]", range.toString());
	}

}
