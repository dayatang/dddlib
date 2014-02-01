package com.dayatang.utils;

import static org.junit.Assert.*;

import org.junit.*;

public class ArrayUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSubstract() {
		Item[] items = {
				new Item(1, "A"),
				new Item(2, "B"),
				new Item(1, "C")
		};
		assertArrayEquals(new Object[]{1, 2, 1}, ArrayUtils.substract(items, "id"));
		assertArrayEquals(new Object[]{"A", "B", "C"}, ArrayUtils.substract(items, "name"));
	}

	@Test
	public void testSubstractNull() {
		assertNull(ArrayUtils.substract(null, "name"));
	}

	@Test
	public void testSubstractEmpty() {
		assertEquals(ArrayUtils.substract(new Item[]{}, "name").length , 0);
	}

	@Test
	public void testSubstractSingleElement() {
		Item[] items = {
				new Item(1, "A")
		};
		assertArrayEquals(new Object[]{1}, ArrayUtils.substract(items, "id"));
	}

	@Test
	public void testJoin() {
		Item[] items = {
				new Item(1, "A"),
				new Item(2, "B"),
				new Item(1, "C")
		};
		String separator = ", ";
		String result = ArrayUtils.join(items, "id", separator);
		assertEquals("1, 2, 1", result);
	}

	@Test
	public void testJoinNull() {
		String separator = ", ";
		Item[] items = null;
		String result = ArrayUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testJoinEmpty() {
		Item[] items = {
		};
		String separator = ", ";
		String result = ArrayUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testJoinSingleElement() {
		Item[] items = {
				new Item(1, "A")
		};
		String separator = ", ";
		String result = ArrayUtils.join(items, "name", separator);
		assertEquals("A", result);
	}
}
