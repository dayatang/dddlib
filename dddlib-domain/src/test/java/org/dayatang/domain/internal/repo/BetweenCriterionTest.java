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
import org.dayatang.domain.internal.repo.BetweenCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class BetweenCriterionTest {

    private BetweenCriterion instance;
    
    @Before
    public void setUp() {
        instance = new BetweenCriterion("age", 18, 22);
    }

    @Test
    public void testGetPropName() {
        assertEquals("age", instance.getPropName());
    }

    @Test
    public void testGetFrom() {
        assertEquals(18, instance.getFrom());
    }

    @Test
    public void testGetTo() {
        assertEquals(22, instance.getTo());
    }

    @Test
    public void testToQueryString() {
        assertEquals("rootEntity.age between :rootEntity_age" + instance.hashCode() + 
        		"_from and :rootEntity_age" + instance.hashCode() + "_to", instance.toQueryString());
    }

    @Test
    public void testGetParameters() {
        assertEquals(NamedParameters.create().add("rootEntity_age" + instance.hashCode() + "_from", 18)
                .add("rootEntity_age" + instance.hashCode() + "_to", 22), instance.getParameters());
    }

    @Test
    public void testEquals() {
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("abc"));
        assertTrue(instance.equals(instance));
        
        BetweenCriterion other = new BetweenCriterion("age", 18, 22);
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
    }

}
