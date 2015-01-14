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

package org.dayatang.domain.internal.repo;

import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.internal.repo.SizeNotEqCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class SizeNotEqCriterionTest {
    
    private SizeNotEqCriterion instance;

    @Before
    public void setUp() {
        instance = new SizeNotEqCriterion("name", 3);
    }

    @Test
    public void testGetValue() {
        assertEquals(3, instance.getValue());
    }

    @Test
    public void testToQueryString() {
        assertEquals("size(rootEntity.name) != :rootEntity_name" + instance.hashCode(), 
                instance.toQueryString());
    }

    @Test
    public void testGetParameters() {
        assertEquals(NamedParameters.create().add("rootEntity_name" + instance.hashCode(), 3), 
                instance.getParameters());
    }

    @Test
    public void testEquals() {
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("abc"));
        assertTrue(instance.equals(instance));
        
        SizeNotEqCriterion other = new SizeNotEqCriterion("name", 3);
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
        
        assertFalse(instance.equals(new SizeNotEqCriterion("name", 4)));
    }
    
}
