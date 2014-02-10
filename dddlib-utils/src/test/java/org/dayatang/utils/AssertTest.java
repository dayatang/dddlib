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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author yyang
 */
public class AssertTest {
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /*
    isTrue
    */
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueWithMessageFailure() {
        Assert.isTrue(false, "Failure!");
    }

    @Test()
    public void testIsTrueWithMessageSuccess() {
        Assert.isTrue(3 > 2, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsTrueFailure() {
        Assert.isTrue(false);
    }

    @Test()
    public void testIsTrueSuccess() {
        Assert.isTrue(3 > 2);
    }

    /*
    isFalse
    */
    

    @Test(expected = IllegalArgumentException.class)
    public void testIsFalseWithMessageFailure() {
        Assert.isFalse(true, "Failure!");
    }

    @Test()
    public void testIsFalseWithMessageSuccess() {
        Assert.isFalse(3 < 2, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsFalseFailure() {
        Assert.isFalse(true);
    }

    @Test()
    public void testIsFalseSuccess() {
        Assert.isFalse(3 < 2);
    }

    /*
    isNull
    */

    @Test(expected = IllegalArgumentException.class)
    public void testIsNullWithMessageFailure() {
        Assert.isNull("Hi", "Failure!");
    }

    @Test()
    public void testIsNullWithMessageSuccess() {
        Assert.isNull(null, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNullFailure() {
        Assert.isNull("Hi");
    }

    @Test()
    public void testIsNullSuccess() {
        Assert.isNull(null);
    }

    /*
    notNull
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotNullWithMessageFailure() {
        Assert.notNull(null, "Failure!");
    }

    @Test()
    public void testNotNullWithMessageSuccess() {
        Assert.notNull("Hi", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotNullFailure() {
        Assert.notNull(null);
    }

    @Test()
    public void testNotNullSuccess() {
        Assert.notNull("Hi");
    }

    /*
    isEmptyString
    */

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyStringWithMessageFailure() {
        Assert.isEmpty("Hi", "Failure!");
    }

    @Test()
    public void testIsEmptyStringWithMessageSuccess() {
        Assert.isEmpty((String) null, "Failure!");
        Assert.isEmpty("", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyStringFailure() {
        Assert.isEmpty("Hi");
    }

    @Test()
    public void testIsEmptyStringSuccess() {
        Assert.isEmpty((String) null);
    }

    /*
    notEmptyString
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyStringWithMessageFailure() {
        Assert.notEmpty((String) null, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyStringWithMessageFailure1() {
        Assert.notEmpty("", "Failure!");
    }

    @Test()
    public void testNotEmptyStringWithMessageSuccess() {
        Assert.notEmpty("Hi", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyStringFailure() {
        Assert.notEmpty((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyStringFailure1() {
        Assert.notEmpty("");
    }

    @Test()
    public void testNotEmptyStringSuccess() {
        Assert.notEmpty("Hi");
    }

    /*
    isEmptyCollection
    */

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyCollectionWithMessageFailure() {
        Assert.isEmpty(new String[] {"abc"}, "Failure!");
    }

    @Test()
    public void testIsEmptyCollectionWithMessageSuccess() {
        String[] array = null;
        Assert.isEmpty(array, "Failure!");
        Assert.isEmpty(new String[] {}, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyCollectionFailure() {
        Assert.isEmpty(new String[] {"abc"});
    }

    @Test()
    public void testIsEmptyCollectionSuccess() {
        Assert.isEmpty((String) null);
    }

    /*
    notEmptyCollection
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithMessageFailure() {
        String[] array = null;
        Assert.notEmpty(array, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionWithMessageFailure1() {
        Assert.notEmpty(new String[] {}, "Failure!");
    }

    @Test()
    public void testNotEmptyCollectionWithMessageSuccess() {
        Assert.notEmpty(new String[] {"abc"}, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionFailure() {
        String[] array = null;
        Assert.notEmpty(array);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyCollectionFailure1() {
        Assert.notEmpty(new String[] {});
    }

    @Test()
    public void testNotEmptyCollectionSuccess() {
        Assert.notEmpty(new String[] {"abc"});
    }
    
    /*
    isEmptyMap
    */

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyMapWithMessageFailure() {
        Assert.isEmpty(Collections.singletonMap("abc", "xyz"), "Failure!");
    }

    @Test()
    public void testIsEmptyMapWithMessageSuccess() {
        Assert.isEmpty((Map) null, "Failure!");
        Assert.isEmpty(Collections.EMPTY_MAP, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsEmptyMapFailure() {
        Assert.isEmpty(Collections.singletonMap("abc", "xyz"));
    }

    @Test()
    public void testIsEmptyMapSuccess() {
        Assert.isEmpty((Map) null);
        Assert.isEmpty(Collections.EMPTY_MAP);
    }

    /*
    notEmptyMap
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithMessageFailure() {
        Assert.notEmpty((Map) null, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapWithMessageFailure1() {
        Assert.notEmpty(Collections.EMPTY_MAP, "Failure!");
    }

    @Test()
    public void testNotEmptyMapWithMessageSuccess() {
        Assert.notEmpty(Collections.singletonMap("abc", "xyz"), "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapFailure() {
        Assert.notEmpty((Map) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEmptyMapFailure1() {
        Assert.notEmpty(Collections.EMPTY_MAP);
    }

    @Test()
    public void testNotEmptyMapSuccess() {
        Assert.notEmpty(Collections.singletonMap("abc", "xyz"));
    }

    /*
    isBlank
    */

    @Test(expected = IllegalArgumentException.class)
    public void testIsBlankWithMessageFailure() {
        Assert.isBlank("Hi", "Failure!");
    }

    @Test()
    public void testIsBlankWithMessageSuccess() {
        Assert.isBlank((String) null, "Failure!");
        Assert.isBlank("", "Failure!");
        Assert.isBlank("   ", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsBlankFailure() {
        Assert.isBlank("Hi");
    }

    @Test()
    public void testIsBlankSuccess() {
        Assert.isBlank((String) null);
        Assert.isBlank("");
        Assert.isBlank("   ");
    }

    /*
    notBlank
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankWithMessageFailure() {
        Assert.notBlank((String) null, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankWithMessageFailure1() {
        Assert.notBlank("", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankWithMessageFailure2() {
        Assert.notBlank("   ", "Failure!");
    }

    @Test()
    public void testNotBlankWithMessageSuccess() {
        Assert.notBlank("Hi", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankFailure() {
        Assert.notBlank((String) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankFailure1() {
        Assert.notBlank("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotBlankFailure2() {
        Assert.notBlank("   ");
    }

    @Test()
    public void testNotBlankSuccess() {
        Assert.notBlank("Hi");
    }

    /*
    containsText
    */

    @Test(expected = IllegalArgumentException.class)
    public void testContainsTextWithMessageFailure() {
        Assert.containsText("Hi", "Haha", "Failure!");
    }

    @Test()
    public void testContainsTextWithMessageSuccess() {
        Assert.containsText("abc", "a", "Failure!");
        Assert.containsText("abc", "b", "Failure!");
        Assert.containsText("abc", "c", "Failure!");
        Assert.containsText("abc", "ab", "Failure!");
        Assert.containsText("abc", "abc", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testContainsTextFailure() {
        Assert.containsText("Hi", "Haha");
    }

    @Test()
    public void testContainsTextSuccess() {
        Assert.containsText("abc", "a");
        Assert.containsText("abc", "b");
        Assert.containsText("abc", "c");
        Assert.containsText("abc", "ab");
        Assert.containsText("abc", "abc");
    }

    /*
    notContainsText
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextWithMessageFailure1() {
        Assert.notContainsText("abc", "a", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextWithMessageFailure2() {
        Assert.notContainsText("abc", "b", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextWithMessageFailure3() {
        Assert.notContainsText("abc", "c", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextWithMessageFailure4() {
        Assert.notContainsText("abc", "ab", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextWithMessageFailure5() {
        Assert.notContainsText("abc", "abc", "Failure!");
    }

    @Test()
    public void testNotContainsTextWithMessageSuccess() {
        Assert.notContainsText("abc", "ac", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextFailure1() {
        Assert.notContainsText("abc", "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextFailure2() {
        Assert.notContainsText("abc", "b");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextFailure3() {
        Assert.notContainsText("abc", "c");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextFailure4() {
        Assert.notContainsText("abc", "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotContainsTextFailure5() {
        Assert.notContainsText("abc", "abc");
    }

    @Test()
    public void testNotContainsTextSuccess() {
        Assert.notContainsText("abc", "ac");
    }

    /*
    startsWithText
    */

    @Test(expected = IllegalArgumentException.class)
    public void testStartsWithTextWithMessageFailure() {
        Assert.startsWithText("abc", "ac", "Failure!");
    }

    @Test()
    public void testStartsWithTextWithMessageSuccess() {
        Assert.startsWithText("abc", "a", "Failure!");
        Assert.startsWithText("abc", "ab", "Failure!");
        Assert.startsWithText("abc", "abc", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testStartsWithTextFailure() {
        Assert.startsWithText("abc", "ac");
    }

    @Test()
    public void testStartsWithTextSuccess() {
        Assert.startsWithText("abc", "a");
        Assert.startsWithText("abc", "ab");
        Assert.startsWithText("abc", "abc");
    }

    /*
    notStartsWithText
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotStartsWithTextWithMessageFailure1() {
        Assert.notStartsWithText("abc", "a", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotStartsWithTextWithMessageFailure2() {
        Assert.notStartsWithText("abc", "ab", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotStartsWithTextWithMessageFailure3() {
        Assert.notStartsWithText("abc", "abc", "Failure!");
    }

    @Test()
    public void testNotStartsWithTextWithMessageSuccess() {
        Assert.notStartsWithText("abc", "ac", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotStartsWithTextFailure1() {
        Assert.notStartsWithText("abc", "a");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotStartsWithTextFailure2() {
        Assert.notStartsWithText("abc", "ab");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotStartsWithTextFailure3() {
        Assert.notStartsWithText("abc", "abc");
    }

    @Test()
    public void testNotStartsWithTextSuccess() {
        Assert.notStartsWithText("abc", "ac");
    }

    /*
    endsWithText
    */

    @Test(expected = IllegalArgumentException.class)
    public void testEndsWithTextWithMessageFailure() {
        Assert.endsWithText("abc", "ac", "Failure!");
    }

    @Test()
    public void testEndsWithTextWithMessageSuccess() {
        Assert.endsWithText("abc", "c", "Failure!");
        Assert.endsWithText("abc", "bc", "Failure!");
        Assert.endsWithText("abc", "abc", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEndsWithTextFailure() {
        Assert.endsWithText("abc", "ac");
    }

    @Test()
    public void testEndsWithTextSuccess() {
        Assert.endsWithText("abc", "c");
        Assert.endsWithText("abc", "bc");
        Assert.endsWithText("abc", "abc");
    }

    /*
    notEndsWithText
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNotEndsWithTextWithMessageFailure1() {
        Assert.notEndsWithText("abc", "c", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEndsWithTextWithMessageFailure2() {
        Assert.notEndsWithText("abc", "bc", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEndsWithTextWithMessageFailure3() {
        Assert.notEndsWithText("abc", "abc", "Failure!");
    }

    @Test()
    public void testNotEndsWithTextWithMessageSuccess() {
        Assert.notEndsWithText("abc", "ac", "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEndsWithTextFailure1() {
        Assert.notEndsWithText("abc", "c");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEndsWithTextFailure2() {
        Assert.notEndsWithText("abc", "bc");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNotEndsWithTextFailure3() {
        Assert.notEndsWithText("abc", "abc");
    }

    @Test()
    public void testNotEndsWithTextSuccess() {
        Assert.notEndsWithText("abc", "ac");
    }

    /*
    noNullElementsArray
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayWithMessageFailure() {
        Object[] results = {"abc", null};
        Assert.noNullElements(results, "Failure!");
    }

    @Test
    public void testNoNullElementsArrayWithMessageSuccess() {
        Object[] results = {"abc", 123};
        Assert.noNullElements(results, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsArrayFailure() {
        Object[] results = {"abc", null};
        Assert.noNullElements(results);
    }

    @Test
    public void testNoNullElementsArraySuccess() {
        Object[] results = {"abc", 123};
        Assert.noNullElements(results);
    }

    /*
    noNullElementsCollection
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionWithMessageFailure() {
        List<Object> results = Arrays.asList(new Object[] {"abc", null});
        Assert.noNullElements(results, "Failure!");
    }

    @Test
    public void testNoNullElementsCollectionWithMessageSuccess() {
        List<Object> results = Arrays.asList(new Object[] {"abc", 123});
        Assert.noNullElements(results, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsCollectionFailure() {
        List<Object> results = Arrays.asList(new Object[] {"abc", null});
        Assert.noNullElements(results);
    }

    @Test
    public void testNoNullElementsCollectionSuccess() {
        List<Object> results = Arrays.asList(new Object[] {"abc", 123});
        Assert.noNullElements(results);
    }

    /*
    noNullElementsMap
    */

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsMapWithMessageFailure() {
        Map results = new HashMap();
        results.put("a", "abc");
        results.put("b", null);
        Assert.noNullElements(results, "Failure!");
    }

    @Test
    public void testNoNullElementsMapWithMessageSuccess() {
        Map results = new HashMap();
        results.put("a", "abc");
        results.put("b", 123);
        Assert.noNullElements(results, "Failure!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoNullElementsMapFailure() {
        Map results = new HashMap();
        results.put("a", "abc");
        results.put("b", null);
        Assert.noNullElements(results);
    }

    @Test
    public void testNoNullElementsMapSuccess() {
        Map results = new HashMap();
        results.put("a", "abc");
        results.put("b", 123);
        Assert.noNullElements(results);
    }
    
    /*
    isInstanceOf
    */
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfWithMessageFailure() {
        Assert.isInstanceOf(String.class, 123, "Failure");
    }
    
    @Test
    public void testIsInstanceOfWithMessageSuccess() {
        Assert.isInstanceOf(Number.class, 12.5, "Failure");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsInstanceOfFailure() {
        Assert.isInstanceOf(String.class, 123);
    }
    
    @Test
    public void testIsInstanceOfSuccess() {
        Assert.isInstanceOf(Number.class, 12.5);
    }

    /*
    isAssignableFrom
    */
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromWithMessageFailure() {
        Assert.isAssignableFrom(String.class, Integer.class, "Failure");
    }
    
    @Test
    public void testIsAssignableFromWithMessageSuccess() {
        Assert.isAssignableFrom(Number.class, Double.class, "Failure");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIsAssignableFromFailure() {
        Assert.isAssignableFrom(String.class, Integer.class);
    }
    
    @Test
    public void testIsAssignableFromSuccess() {
        Assert.isAssignableFrom(Number.class, Double.class);
    }
}
