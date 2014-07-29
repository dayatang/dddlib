package org.dayatang.utils;


import org.dayatang.utils.beans.ConcreteItem;
import org.dayatang.utils.beans.Item;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class BeanUtilsTest {
    private BeanUtils instance;
    private ConcreteItem item;
    
    @Before
    public void setUp() {
        item = new ConcreteItem(1, "abc", true);
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
        Map<String, Object> values = instance.getPropValuesExclude(Deprecated.class);
        assertEquals(1, values.get("id"));
        assertEquals("abc", values.get("name"));
        assertNull(values.get("disabled"));
        assertFalse(values.containsKey("price"));
    }
    
    @Test
    public void testGetPropValue() {
        assertEquals("abc", instance.getPropValue("name"));
    }

    @Test
    public void testSetPropValue() {
        instance.setPropValue("id", 1000);
        assertEquals(1, item.getId());
        instance.setPropValue("price", 2015);
        assertEquals(2015, item.getPrice(), 0.0001);
    }
    
    @Test
    public void testPopulateWithMap() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("id", 1000);
        properties.put("name", "aaaa");
        instance.populate(properties);
        assertEquals(1, item.getId());
        assertEquals("aaaa", item.getName());
        assertEquals(12.5, item.getPrice(), 0.0001);
    }

    @Test
    public void testCopyPropertiesFrom() {
        Item item1 = new Item(12, "abc", true);
        ConcreteItem item2 = new ConcreteItem(20, "xyz", false);
        item2.setPrice(15.5);
        instance = new BeanUtils(item2);
        instance.copyPropertiesFrom(item1);
        assertEquals("abc", item2.getName());
        assertEquals(15.5, item2.getPrice(), 0.0001);
    }

    @Test
    public void testCopyPropertiesTo() {
        Item item1 = new Item(12, "abc", true);
        ConcreteItem item2 = new ConcreteItem(20, "xyz", false);
        item2.setPrice(15.5);
        instance = new BeanUtils(item2);
        instance.copyPropertiesTo(item1);
        assertEquals("xyz", item1.getName());
    }

    @Test
    public void testCopyPropertiesFromWithExcludes() {
        Item item1 = new Item(12, "abc", true);
        ConcreteItem item2 = new ConcreteItem(20, "xyz", false);
        item2.setPrice(15.5);
        instance = new BeanUtils(item2);
        instance.copyPropertiesFrom(item1, "id");
        assertEquals(20, item2.getId());
        assertEquals("abc", item2.getName());
        assertEquals(15.5, item2.getPrice(), 0.0001);
    }

    @Test
    public void testCopyPropertiesToWithExcludes() {
        Item item1 = new Item(12, "abc", true);
        ConcreteItem item2 = new ConcreteItem(20, "xyz", false);
        item2.setPrice(15.5);
        instance = new BeanUtils(item2);
        instance.copyPropertiesTo(item1, "id");
        assertEquals(20, item2.getId());
        assertEquals("xyz", item1.getName());
    }
}
