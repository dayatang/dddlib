package org.dayatang.domain.datatype;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import org.dayatang.domain.DataType;
import org.dayatang.domain.Value;
import org.dayatang.utils.DateUtils;

import static org.junit.Assert.*;

public class ValueTest {

    private final String[] DATE_FORMAT = new String[]{
        "yyyy-MM-dd",
        "hh:mm:ss",
        "yyyy-MM-dd hh:mm:ss"
    };

    private Value value;

    @Test
    public void testValueDataTypeString() {
        value = Value.intValue("12");
        assertEquals(DataType.INT, value.getDataType());
        assertEquals("12", value.getStringValue());
    }

    @Test
    public void testGetDataType() {
        assertEquals(DataType.BIG_DECIMAL, Value.bigDecimalValue("12").getDataType());
        assertEquals(DataType.BOOLEAN, Value.booleanValue("12").getDataType());
        assertEquals(DataType.DATE, Value.dateValue("12").getDataType());
        assertEquals(DataType.DATE_TIME, Value.dateTimeValue("12").getDataType());
        assertEquals(DataType.DOUBLE, Value.doubleValue("12").getDataType());
        assertEquals(DataType.INT, Value.intValue("12").getDataType());
        assertEquals(DataType.LONG, Value.longValue("12").getDataType());
        assertEquals(DataType.STRING, Value.stringValue("12").getDataType());
        assertEquals(DataType.TIME, Value.timeValue("12").getDataType());
    }

    @Test
    public void testGetStringValue() {
        assertEquals("12", Value.intValue("12").getStringValue());
    }

    @Test
    public void testGetString() {
        assertEquals("12", Value.stringValue("12").getString());
        assertEquals("", Value.stringValue(null).getString());
    }

    @Test
    public void testGetInt() {
        assertEquals(12, Value.intValue("12").getInt());
        assertEquals(0, Value.intValue(null).getInt());
    }

    @Test
    public void testGetLong() {
        assertEquals(12L, Value.longValue("12").getLong());
        assertEquals(0, Value.longValue(null).getLong());
    }

    @Test
    public void testGetDouble() {
        assertEquals(12.0, Value.doubleValue("12").getDouble(), 0.001);
        assertEquals(0.0, Value.doubleValue(null).getDouble(), 0.001);
    }

    @Test
    public void testGetBigDecimal() {
        assertEquals(new BigDecimal(12), Value.bigDecimalValue("12").getBigDecimal());
        assertEquals(BigDecimal.ZERO, Value.bigDecimalValue(null).getBigDecimal());
    }

    @Test
    public void testGetBoolean() {
        assertTrue(Value.booleanValue("true").getBoolean());
        assertFalse(Value.booleanValue(null).getBoolean());
        assertFalse(Value.booleanValue("").getBoolean());
    }

    @Test
    public void testGetDate() {
        assertEquals(DateUtils.date(2000, 1, 15), Value.dateValue("2000-01-15").getDate());
        assertEquals(DateUtils.date(2000, 1, 15, 11, 5, 30), 
                Value.dateTimeValue("2000-01-15 11:05:30").getDate());
        assertNull(Value.dateValue("").getDate());
    }

    @Test
    public void testEquals() {
        Value value1 = Value.intValue("12");
        Value value2 = Value.bigDecimalValue("12");
        Value value3 = Value.intValue("13");
        assertTrue(value1.equals(Value.intValue("12")));
        assertFalse(value1.equals("12"));
        assertFalse(value1.equals(value2));
        assertFalse(value1.equals(value3));
    }

    @Test
    
    public void testToString() {
        value = Value.dateValue("2000-1-5");
        assertEquals("2000-1-5", value.toString());
    }

    @Test
    public void testGetValue() throws ParseException {
        assertEquals("12", Value.stringValue("12").getValue());
        assertEquals("", Value.stringValue("").getValue());

        assertEquals(12, Value.intValue("12").getValue());
        assertEquals(0, Value.intValue("").getValue());
        
        assertEquals(12L, Value.longValue("12").getValue());
        assertEquals(0L, Value.longValue(null).getValue());

        assertEquals(12.5, (Double) Value.doubleValue("12.5").getValue(), 0.001);
        assertEquals(0.0, (Double) Value.doubleValue(null).getValue(), 0.001);

        assertTrue((Boolean) Value.booleanValue("true").getValue());
        assertFalse((Boolean) Value.booleanValue("false").getValue());
        assertFalse((Boolean) Value.booleanValue("").getValue());

        assertEquals(DateUtils.date(2000, 1, 15), Value.dateValue("2000-01-15").getValue());
        assertEquals(DateUtils.date(2000, 1, 15, 11, 5, 30), 
                Value.dateTimeValue("2000-01-15 11:05:30").getValue());
        assertNull(Value.dateValue("").getValue());
    }
}
