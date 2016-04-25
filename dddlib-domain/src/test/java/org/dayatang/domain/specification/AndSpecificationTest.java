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

package org.dayatang.domain.specification;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author yyang
 */
public class AndSpecificationTest {
    
    private Specification<String> specification1;
    
    private Specification<String> specification2;
    
    private String fact = "abc";
    
    private Specification<String> instance;
    
    @Before
    public void setUp() {
        specification1 = mock(Specification.class);
        specification2 = mock(Specification.class);
        instance = new AndSpecification<String>(specification1, specification2);
    }

    @Test
    public void testIsSatisfiedByTrueTrue() {
        when(specification1.isSatisfiedBy(fact)).thenReturn(true);
        when(specification2.isSatisfiedBy(fact)).thenReturn(true);
        assertTrue(instance.isSatisfiedBy(fact));
    }

    @Test
    public void testIsSatisfiedByTrueFalse() {
        when(specification1.isSatisfiedBy(fact)).thenReturn(true);
        when(specification2.isSatisfiedBy(fact)).thenReturn(false);
        assertFalse(instance.isSatisfiedBy(fact));
    }

    @Test
    public void testIsSatisfiedByFalseTrue() {
        when(specification1.isSatisfiedBy(fact)).thenReturn(false);
        when(specification2.isSatisfiedBy(fact)).thenReturn(true);
        assertFalse(instance.isSatisfiedBy(fact));
    }

    @Test
    public void testIsSatisfiedByFalseFalse() {
        when(specification1.isSatisfiedBy(fact)).thenReturn(false);
        when(specification2.isSatisfiedBy(fact)).thenReturn(false);
        assertFalse(instance.isSatisfiedBy(fact));
    }
    
}
