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
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public class NotSpecificationTest {
    
    private Specification<String> specification;
    private String fact = "abc";
    private NotSpecification<String> instance;
    
    @Before
    public void setUp() {
        specification = mock(Specification.class);
        instance = new NotSpecification<String>(specification);
    }

    @Test
    public void testIsSatisfiedByTrue() {
        when(specification.isSatisfiedBy(fact)).thenReturn(true);
        assertFalse(instance.isSatisfiedBy(fact));
    }

    @Test
    public void testIsSatisfiedByFalse() {
        when(specification.isSatisfiedBy(fact)).thenReturn(false);
        assertTrue(instance.isSatisfiedBy(fact));
    }
    
}
