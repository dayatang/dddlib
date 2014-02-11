/*
 * Copyright 2014 Dayatang Open Source..
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dayatang.domain.repository;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import org.dayatang.domain.ArrayParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class ArrayParametersTest {
    
    private ArrayParameters instance;
    private Object[] params;
    
    @Before
    public void setUp() {
        params = new Object[] {"abc", 12, new Date()};
    }

    @Test
    public void testCreateWithoutParameters() {
        instance = ArrayParameters.create();
        assertNotNull(instance.getParams());
        assertTrue(instance.getParams().length == 0);
    }

    @Test
    public void testCreateWithArray() {
        instance = ArrayParameters.create(params);
        assertArrayEquals(params, instance.getParams());
    }

    @Test
    public void testCreateWithList() {
        instance = ArrayParameters.create(Arrays.asList(params));
        assertArrayEquals(params, instance.getParams());
    }

    @Test
    public void testEquals() {
        instance = ArrayParameters.create(params);
        assertTrue(instance.equals(instance));
        assertFalse(instance.equals(params));
        ArrayParameters other = ArrayParameters.create(params);
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
        
        other = ArrayParameters.create("name", "abc");
        assertFalse(instance.equals(other));
        assertFalse(other.equals(instance));
    }

    @Test
    public void testToString() {
        assertEquals(Arrays.toString(params), ArrayParameters.create(params).toString());
    }
    
}
