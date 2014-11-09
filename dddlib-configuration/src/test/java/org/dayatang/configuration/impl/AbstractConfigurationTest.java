package org.dayatang.configuration.impl;

import org.dayatang.configuration.Configuration;
import org.dayatang.utils.DateUtils;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Properties;

import static org.junit.Assert.*;

public abstract class AbstractConfigurationTest {

	protected AbstractConfiguration instance;

	@Test
	public void testGetStringStringString() {
		assertEquals("yyyy-M-d", instance.getString("date.format", "yyyy-MM-dd"));
		assertEquals("yyyy-MM-dd", instance.getString("format", "yyyy-MM-dd"));
	}

	@Test
	public void testGetStringString() {
		assertEquals("yyyy-M-d", instance.getString("date.format"));
		assertEquals("", instance.getString("format"));
		assertEquals("张三", instance.getString("name"));
	}

	@Test
	public void testSetString() {
		instance.setString("date.format", "yyyy-MM-dd");
		assertEquals("yyyy-MM-dd", instance.getString("date.format"));
	}

	@Test
	public void testGetIntStringInt() {
		assertEquals(15, instance.getInt("size", 20));
		assertEquals(20, instance.getInt("size1", 20));
	}

	@Test
	public void testGetIntString() {
		assertEquals(15, instance.getInt("size"));
		assertEquals(0, instance.getInt("size1"));
	}

	@Test
	public void testSetInt() {
		instance.setInt("size", 150);
		assertEquals(150, instance.getInt("size"));
	}

	@Test
	public void testGetLongStringLong() {
		assertEquals(15L, instance.getLong("size", 20L));
		assertEquals(20L, instance.getLong("size1", 20L));
	}

	@Test
	public void testGetLongString() {
		assertEquals(15L, instance.getLong("size"));
		assertEquals(0L, instance.getLong("size1"));
	}

	@Test
	public void testSetLong() {
		instance.setLong("size", 150L);
		assertEquals(150L, instance.getLong("size"));
	}

	@Test
	public void testGetDoubleStringDouble() {
		assertEquals(15D, instance.getDouble("size", 20D), 0.001);
		assertEquals(20D, instance.getDouble("size1", 20D), 0.001);
	}

	@Test
	public void testGetDoubleString() {
		assertEquals(15D, instance.getDouble("size"), 0.001);
		assertEquals(0D, instance.getDouble("size1"), 0.001);
	}

	@Test
	public void testSetDouble() {
		instance.setDouble("size", 150D);
		assertEquals(150D, instance.getDouble("size"), 0.001);
	}

	@Test
	public void testGetBooleanStringBoolean() {
		assertTrue(instance.getBoolean("closed", false));
		assertTrue(instance.getBoolean("closed1", true));
	}

	@Test
	public void testGetBooleanString() {
		assertTrue(instance.getBoolean("closed"));
		assertFalse(instance.getBoolean("closed1"));
	}

	@Test
	public void testSetBoolean() {
		instance.setBoolean("closed", false);
		assertFalse(instance.getBoolean("size"));
	}

	@Test
	public void testGetDateStringDate() {
		Date orig = DateUtils.parseDate("2002-05-11");
		Date defaultDate = DateUtils.parseDate("2008-05-11");
		assertEquals(orig, instance.getDate("birthday", defaultDate));
		assertEquals(defaultDate, instance.getDate("birthday1", defaultDate));
	}

	@Test
	public void testGetDateString() {
		Date orig = DateUtils.parseDate("2002-05-11");
		assertEquals(orig, instance.getDate("birthday"));
		assertEquals(null, instance.getDate("birthday1"));
	}

	@Test
	public void testSetDate() {
		Date newDate = DateUtils.parseDate("2008-05-11");
		instance.setDate("birthday", newDate);
		assertEquals(newDate, instance.getDate("birthday"));
	}

    @Test
    public void testGetObjectWithDefault() {
        Item orig = new Item(1, "abc", true);
        Item defaultItem = new Item(2, "xyz", false);
        assertEquals(orig, instance.getObject("theItem", Item.class, defaultItem));
        assertEquals(defaultItem, instance.getObject("theItem1", Item.class, defaultItem));
    }

    @Test
    public void testGetObjectWithoutDefault() {
        Item orig = new Item(1, "abc", true);
        assertEquals(orig, instance.getObject("theItem", Item.class));
        assertNull(instance.getObject("theItem1", Item.class));
    }

    @Test
    public void testSetObject() {
        Date newDate = DateUtils.parseDate("2008-05-11");
        instance.setObject("theDay", newDate);
        assertEquals(newDate, instance.getObject("theDay", Date.class));
    }

	@Test
	public void testGetProperties() {
		Properties properties = instance.getProperties();
		assertEquals("15", properties.get("size"));
	}

    private class Item {

        private int id;
        private String name;
        private boolean disabled;

        private Item() {
        }

        private Item(int id, String name, boolean disabled) {
            this.id = id;
            this.name = name;
            this.disabled = disabled;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Item item = (Item) o;

            if (disabled != item.disabled) return false;
            if (id != item.id) return false;
            if (!name.equals(item.name)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = id;
            result = 31 * result + name.hashCode();
            result = 31 * result + (disabled ? 1 : 0);
            return result;
        }

        @Override
        public String toString() {
            return "Item{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", disabled=" + disabled +
                    '}';
        }
    }

}
