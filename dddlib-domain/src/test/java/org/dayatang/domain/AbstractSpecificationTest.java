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

package org.dayatang.domain;

import org.dayatang.domain.specification.LengthSpecification;
import org.dayatang.domain.specification.StartsSpecification;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class AbstractSpecificationTest {

    String data = "Ha! Ha! Ha!";

    @Test
    public void testAnd() {
        assertTrue(new StartsSpecification("Ha! ")
                .and(new LengthSpecification(2, 20))
                .isSatisfiedBy(data));
       assertFalse(new StartsSpecification("Ha! ")
                .and(new LengthSpecification(5))
                .isSatisfiedBy(data));
       assertFalse(new StartsSpecification("Ha ")
                .and(new LengthSpecification(2, 20))
                .isSatisfiedBy(data));     
    }

    @Test
    public void testOr() {
       assertTrue(new StartsSpecification("Ha! ")
                .or(new LengthSpecification(5))
                .isSatisfiedBy(data));
       assertTrue(new StartsSpecification("Ha ")
                .or(new LengthSpecification(2, 20))
                .isSatisfiedBy(data));     
       assertFalse(new StartsSpecification("Ha ")
                .or(new LengthSpecification(5))
                .isSatisfiedBy(data));
    }

    @Test
    public void testNot() {
        Specification<String> specification = new LengthSpecification(5);
        assertTrue(specification.isSatisfiedBy(data));
        assertFalse(specification.not().isSatisfiedBy(data));
    }
    
}
