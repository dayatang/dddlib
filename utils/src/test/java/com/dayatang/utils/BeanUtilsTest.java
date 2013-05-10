package com.dayatang.utils;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class BeanUtilsTest {

	@Test
	public void testGetPropTypes() {
		Map<String, Class<?>> types = new HashMap<String, Class<?>>();
		types.put("id", int.class);
		types.put("name", String.class);
		types.put("disabled", boolean.class);
		assertEquals(types, BeanUtils.getPropTypes(Item.class));
	}

	@Test
	public void testGetPropNames() {
		List<String> names = Arrays.asList("id", "name", "disabled");
		assertEquals(new HashSet<String>(names), BeanUtils.getPropNames(Item.class));
	}

	@Test
	public void testGetPropValues() {
		Item bean = new Item(1, "zhang", true);
		Map<String, Object> values = new HashMap<String, Object>();
		values.put("id", 1);
		values.put("name", "zhang");
		values.put("disabled", true);
		assertEquals(values, BeanUtils.getPropValues(bean));
	}

}
