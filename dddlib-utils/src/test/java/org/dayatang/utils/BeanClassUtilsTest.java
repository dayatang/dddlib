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

package org.dayatang.utils;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class BeanClassUtilsTest {
    
    private BeanClassUtils instance;
    
    @Before
    public void setUp() {
        instance = new BeanClassUtils(ConcretItem.class);
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
}
