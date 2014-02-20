package org.dayatang.utils;

import java.beans.Transient;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import org.junit.Before;

public class BeanUtilsTest {
    private BeanUtils instance;
    
    @Before
    public void setUp() {
        ConcretItem item = new ConcretItem(1, "abc", true);
        item.setPrice(12.5);
        instance = new BeanUtils(item);
    }

    @Test
    public void testGetPropTypes() {
        Map<String, Class<?>> types = instance.getPropTypes();
        assertEquals(int.class, types.get("id"));
        assertEquals(String.class, types.get("name"));
        assertEquals(boolean.class, types.get("disabled"));
        assertEquals(double.class, types.get("price"));
    }

    @Test
    public void testGetPropValues() {
        Map<String, Object> values = instance.getPropValues();
        assertEquals(1, values.get("id"));
        assertEquals("abc", values.get("name"));
        assertNull(values.get("disabled"));
        assertEquals(12.5, (Double) values.get("price"), 0.001);
    }

    @Test
    public void testGetPropNames() {
        assertTrue(instance.getPropNames().containsAll(
                Arrays.asList("id", "price", "name", "disabled")));
    }

    @Test
    public void testGetReadablePropNames() {
        Set<String> results = instance.getReadablePropNames();
        assertTrue(results.containsAll(
                Arrays.asList("id", "price", "name")));
        assertFalse(results.contains("disabled"));
    }

    @Test
    public void testGetPropValuesExcludeName() {
        Map<String, Object> values = instance.getPropValuesExclude("id");
        assertFalse(values.containsKey("id"));
        assertEquals("abc", values.get("name"));
        assertNull(values.get("disabled"));
        assertEquals(12.5, (Double) values.get("price"), 0.001);
    }

    @Test
    public void testGetPropValuesExcludeAnnotation() {
        Map<String, Object> values = instance.getPropValuesExclude(Transient.class);
        assertEquals(1, values.get("id"));
        assertEquals("abc", values.get("name"));
        assertNull(values.get("disabled"));
        assertFalse(values.containsKey("price"));
    }
    
    @Test
    public void testGetPropValue() {
        assertEquals("abc", instance.getPropValue("name"));
    }

}
