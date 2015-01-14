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

import org.dayatang.domain.internal.repo.GtPropCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class GtPropCriterionTest {
    
    private GtPropCriterion instance;
    
    @Before
    public void setUp() {
        instance = new GtPropCriterion("name", "name1");
    }

    @Test
    public void testGetOtherPropName() {
        assertEquals("name1", instance.getOtherPropName());
    }

    @Test
    public void testToQueryString() {
        assertEquals("rootEntity.name > rootEntity.name1", 
                instance.toQueryString());
    }

    @Test
    public void testGetParameters() {
        assertTrue(instance.getParameters().getParams().isEmpty());
    }

    @Test
    public void testEquals() {
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("abc"));
        assertTrue(instance.equals(instance));
        
        GtPropCriterion other = new GtPropCriterion("name", "name1");
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }
}
