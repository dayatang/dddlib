package com.dayatang.utils;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class CollectionUtilsTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}


	@Test
	public void testSubstract() {
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item(1, "A"));
		items.add(new Item(2, "B"));
		items.add(new Item(1, "C"));
		assertEquals(Arrays.asList(1, 2, 1), CollectionUtils.substract(items, "id"));
		assertEquals(Arrays.asList("A", "B", "C"), CollectionUtils.substract(items, "name"));
	}

	@Test
	public void testSubstractNull() {
		assertNull(CollectionUtils.substract(null, "name"));
	}

	@Test
	public void testSubstractEmpty() {
		assertTrue(CollectionUtils.substract(Collections.EMPTY_LIST, "name").isEmpty());
	}

	@Test
	public void testSubstractSingleElement() {
		Collection<?> expected = Collections.singletonList(2);
		Collection<?> actual = CollectionUtils.substract(Collections.singletonList(new Item(2, "B")), "id");
		assertEquals(expected, actual);
	}
	
	@Test
	public void testJoin() {
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item(1, "A"));
		items.add(new Item(2, "B"));
		items.add(new Item(1, "C"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "id", separator);
		assertEquals("1, 2, 1", result);
	}

	@Test
	public void testJoinNull() {
		String separator = ", ";
		List<Item> items = null;
		String result = CollectionUtils.join(items, "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testJoinEmpty() {
		String separator = ", ";
		String result = CollectionUtils.join(new ArrayList<Object>(), "id", separator);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testJoinSingleElement() {
		Collection<Item> items = new ArrayList<Item>();
		items.add(new Item(1, "A"));
		String separator = ", ";
		String result = CollectionUtils.join(items, "name", separator);
		assertEquals("A", result);
	}
}
