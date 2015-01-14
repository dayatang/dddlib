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

import java.util.Arrays;
import org.dayatang.domain.NamedParameters;
import org.dayatang.domain.internal.repo.NotInCriterion;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class NotInCriterionTest {
    
    private NotInCriterion instance;
    
    @Before
    public void setUp() {
        instance = new NotInCriterion("name", new String[] {"a", "b"});
    }

    @Test
    public void testGetValue() {
        assertTrue(instance.getValue().containsAll(Arrays.asList("a", "b")));
    }

    @Test
    public void testToQueryString() {
        assertEquals("rootEntity.name not in :rootEntity_name" + instance.hashCode(), 
                instance.toQueryString());
        NotInCriterion other = new NotInCriterion("name", Arrays.asList(1, 2));
        assertEquals("rootEntity.name not in :rootEntity_name" + other.hashCode(), 
        		other.toQueryString());
    }

    @Test
    public void testGetParameters() {
        assertEquals(NamedParameters.create().add("rootEntity_name" + instance.hashCode(), Arrays.asList("a", "b")), instance.getParameters());
    }

    @Test
    public void testEquals() {
        assertFalse(instance.equals(null));
        assertFalse(instance.equals("abc"));
        assertTrue(instance.equals(instance));
        
        NotInCriterion other = new NotInCriterion("name", Arrays.asList("a", "b"));
        assertTrue(instance.equals(other));
        assertTrue(other.equals(instance));
        
        assertFalse(instance.equals(new NotInCriterion("name", Arrays.asList("a", "b", "c"))));
    }
}
