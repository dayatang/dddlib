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

import org.dayatang.domain.*;
import org.dayatang.domain.entity.MyEntity;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author yyang
 */
public class CriteriaQueryTest {
    private CriteriaQuery instance;
    private EntityRepository repository;
    private Class<MyEntity> entityClass = MyEntity.class;

    
    @Before
    public void setUp() {
        repository = mock(EntityRepository.class);
        instance = new CriteriaQuery(repository, entityClass);
    }

    /**
     * Test of getEntityClass method, of class CriteriaQuery.
     */
    @Test
    public void testGetEntityClass() {
        assertEquals(entityClass, instance.getEntityClass());
    }

    /**
     * Test of getFirstResult method, of class JpqlQuery.
     */
    @Test
    public void testFirstResult() {
        assertEquals(3, instance.setFirstResult(3).getFirstResult());
    }

    /**
     * Test of getMaxResults method, of class JpqlQuery.
     */
    @Test
    public void testMaxResults() {
        assertEquals(10, instance.setMaxResults(10).getMaxResults());
    }

    /**
     * Test of getOrderSettings method, of class CriteriaQuery.
     */
    @Test
    public void testOrderSettings() {
        OrderSettings orderSettings = new OrderSettings();
        orderSettings.asc("id");
        orderSettings.desc("name");
        instance.asc("id").desc("name");
        assertEquals(orderSettings, instance.getOrderSettings());
    }

    /**
     * Test of eq method, of class CriteriaQuery.
     */
    @Test
    public void testEq() {
    	assertEquals(Criteria.eq("name", "abc"), instance.eq("name", "abc").getQueryCriterion());
    }

    /**
     * Test of notEq method, of class CriteriaQuery.
     */
    @Test
    public void testNotEq() {
    	assertEquals(Criteria.notEq("name", "abc"), instance.notEq("name", "abc").getQueryCriterion());
    }

    /**
     * Test of gt method, of class CriteriaQuery.
     */
    @Test
    public void testGt() {
    	assertEquals(Criteria.gt("id", 5), instance.gt("id", 5).getQueryCriterion());
    }

    /**
     * Test of ge method, of class CriteriaQuery.
     */
    @Test
    public void testGe() {
    	assertEquals(Criteria.ge("id", 5), instance.ge("id", 5).getQueryCriterion());
    }

    /**
     * Test of lt method, of class CriteriaQuery.
     */
    @Test
    public void testLt() {
    	assertEquals(Criteria.lt("id", 5), instance.lt("id", 5).getQueryCriterion());
    }

    /**
     * Test of le method, of class CriteriaQuery.
     */
    @Test
    public void testLe() {
    	assertEquals(Criteria.le("id", 5), instance.le("id", 5).getQueryCriterion());
    }

    /**
     * Test of eqProp method, of class CriteriaQuery.
     */
    @Test
    public void testEqProp() {
    	assertEquals(Criteria.eqProp("id", "name"), instance.eqProp("id", "name").getQueryCriterion());
    }

    /**
     * Test of notEqProp method, of class CriteriaQuery.
     */
    @Test
    public void testNotEqProp() {
    	assertEquals(Criteria.notEqProp("id", "name"), instance.notEqProp("id", "name").getQueryCriterion());
    }

    /**
     * Test of gtProp method, of class CriteriaQuery.
     */
    @Test
    public void testGtProp() {
    	assertEquals(Criteria.gtProp("id", "name"), instance.gtProp("id", "name").getQueryCriterion());
    }

    /**
     * Test of geProp method, of class CriteriaQuery.
     */
    @Test
    public void testGeProp() {
    	assertEquals(Criteria.geProp("id", "name"), instance.geProp("id", "name").getQueryCriterion());
    }

    /**
     * Test of ltProp method, of class CriteriaQuery.
     */
    @Test
    public void testLtProp() {
    	assertEquals(Criteria.ltProp("id", "name"), instance.ltProp("id", "name").getQueryCriterion());
    }

    /**
     * Test of leProp method, of class CriteriaQuery.
     */
    @Test
    public void testLeProp() {
    	assertEquals(Criteria.leProp("id", "name"), instance.leProp("id", "name").getQueryCriterion());
    }

    /**
     * Test of sizeEq method, of class CriteriaQuery.
     */
    @Test
    public void testSizeEq() {
    	assertEquals(Criteria.sizeEq("id", 3), instance.sizeEq("id", 3).getQueryCriterion());
    }

    /**
     * Test of sizeNotEq method, of class CriteriaQuery.
     */
    @Test
    public void testSizeNotEq() {
    	assertEquals(Criteria.sizeNotEq("id", 3), instance.sizeNotEq("id", 3).getQueryCriterion());
    }

    /**
     * Test of sizeGt method, of class CriteriaQuery.
     */
    @Test
    public void testSizeGt() {
    	assertEquals(Criteria.sizeGt("id", 3), instance.sizeGt("id", 3).getQueryCriterion());
    }

    /**
     * Test of sizeGe method, of class CriteriaQuery.
     */
    @Test
    public void testSizeGe() {
    	assertEquals(Criteria.sizeGe("id", 3), instance.sizeGe("id", 3).getQueryCriterion());
    }

    /**
     * Test of sizeLt method, of class CriteriaQuery.
     */
    @Test
    public void testSizeLt() {
    	assertEquals(Criteria.sizeLt("id", 3), instance.sizeLt("id", 3).getQueryCriterion());
    }

    /**
     * Test of sizeLe method, of class CriteriaQuery.
     */
    @Test
    public void testSizeLe() {
    	assertEquals(Criteria.sizeLe("id", 3), instance.sizeLe("id", 3).getQueryCriterion());
    }

    /**
     * Test of containsText method, of class CriteriaQuery.
     */
    @Test
    public void testContainsText() {
    	assertEquals(Criteria.containsText("name", "a"), instance.containsText("name", "a").getQueryCriterion());
    }

    /**
     * Test of startsWithText method, of class CriteriaQuery.
     */
    @Test
    public void testStartsWithText() {
    	assertEquals(Criteria.startsWithText("name", "a"), instance.startsWithText("name", "a").getQueryCriterion());
    }

    /**
     * Test of in method, of class CriteriaQuery.
     */
    @Test
    public void testInCollection() {
        List<?> criteria = Arrays.asList("a", "b");
    	assertEquals(Criteria.in("name", criteria), instance.in("name", criteria).getQueryCriterion());
    }

    /**
     * Test of in method, of class CriteriaQuery.
     */
    @Test
    public void testInArray() {
        Object[] criteria = new Object[] {"a", "b"};
    	assertEquals(Criteria.in("name", criteria), instance.in("name", criteria).getQueryCriterion());
    }

    /**
     * Test of notIn method, of class CriteriaQuery.
     */
    @Test
    public void testNotInCollection() {
        List<?> criteria = Arrays.asList("a", "b");
    	assertEquals(Criteria.notIn("name", criteria), instance.notIn("name", criteria).getQueryCriterion());
    }

    /**
     * Test of notIn method, of class CriteriaQuery.
     */
    @Test
    public void testNotInArray() {
        Object[] criteria = new Object[] {"a", "b"};
    	assertEquals(Criteria.notIn("name", criteria), instance.notIn("name", criteria).getQueryCriterion());
    }

    /**
     * Test of between method, of class CriteriaQuery.
     */
    @Test
    public void testBetween() {
    	assertEquals(Criteria.between("name", "a", "b"), instance.between("name", "a", "b").getQueryCriterion());
    }

    /**
     * Test of isNull method, of class CriteriaQuery.
     */
    @Test
    public void testIsNull() {
    	assertEquals(Criteria.isNull("name"), instance.isNull("name").getQueryCriterion());
    }

    /**
     * Test of notNull method, of class CriteriaQuery.
     */
    @Test
    public void testNotNull() {
    	assertEquals(Criteria.notNull("name"), instance.notNull("name").getQueryCriterion());
    }

    /**
     * Test of isEmpty method, of class CriteriaQuery.
     */
    @Test
    public void testIsEmpty() {
    	assertEquals(Criteria.isEmpty("name"), instance.isEmpty("name").getQueryCriterion());
    }

    /**
     * Test of notEmpty method, of class CriteriaQuery.
     */
    @Test
    public void testNotEmpty() {
    	assertEquals(Criteria.notEmpty("name"), instance.notEmpty("name").getQueryCriterion());
    }

    /**
     * Test of isTrue method, of class CriteriaQuery.
     */
    @Test
    public void testIsTrue() {
    	assertEquals(Criteria.eq("name", true), instance.isTrue("name").getQueryCriterion());
    }

    /**
     * Test of isFalse method, of class CriteriaQuery.
     */
    @Test
    public void testIsFalse() {
    	assertEquals(Criteria.eq("name", false), instance.isFalse("name").getQueryCriterion());
    }

    /**
     * Test of isBlank method, of class CriteriaQuery.
     */
    @Test
    public void testIsBlank() {
        QueryCriterion criterion1 = Criteria.isNull("name");
        QueryCriterion criterion2 = Criteria.eq("name", "");
        QueryCriterion criterion3 = Criteria.or(criterion1, criterion2);
        
        assertEquals(criterion3, instance.isBlank("name").getQueryCriterion());
    }

    /**
     * Test of notBlank method, of class CriteriaQuery.
     */
    @Test
    public void testNotBlank() {
        QueryCriterion criterion1 = Criteria.notNull("name");
        QueryCriterion criterion2 = Criteria.notEq("name", "");
        QueryCriterion criterion3 = Criteria.and(criterion1, criterion2);
        
        assertEquals(criterion3, instance.notBlank("name").getQueryCriterion());
    }

    /**
     * Test of not method, of class CriteriaQuery.
     */
    @Test
    public void testNot() {
        QueryCriterion criterion1 = Criteria.eq("name", "abc");
        QueryCriterion criterion2 = Criteria.not(criterion1);
        
        assertEquals(criterion2, instance.not(criterion1).getQueryCriterion());
    }

    /**
     * Test of and method, of class CriteriaQuery.
     */
    @Test
    public void testAnd() {
        QueryCriterion criterion1 = Criteria.eq("name", "abc");
        QueryCriterion criterion2 = Criteria.gt("id", 5);
        QueryCriterion criterion3 = Criteria.and(criterion1, criterion2);
        assertEquals(criterion3, instance.and(criterion1).and(criterion2).getQueryCriterion());
    }

    /**
     * Test of or method, of class CriteriaQuery.
     */
    @Test
    public void testOr() {
        QueryCriterion criterion1 = Criteria.eq("name", "abc");
        QueryCriterion criterion2 = Criteria.gt("id", 5);
        QueryCriterion criterion3 = Criteria.or(criterion1, criterion2);
        assertEquals(criterion3, instance.or(criterion1).or(criterion2).getQueryCriterion());
    }

    /**
     * Test of list method, of class CriteriaQuery.
     */
    @Test
    public void testList() {
        List<Object> results = Arrays.asList(new Object[] {"a", "b"});
        when(repository.find(instance)).thenReturn(results);
        assertEquals(results, instance.list());
    }

    /**
     * Test of singleResult method, of class CriteriaQuery.
     */
    @Test
    public void testSingleResult() {
        when(repository.getSingleResult(instance)).thenReturn("abc");
        assertEquals("abc", instance.singleResult());
    }
    
    @Test
    public void testGetQueryString() {
    	QueryCriterion criterion1 = Criteria.eq("name", "abc");
        QueryCriterion criterion2 = Criteria.in("age", Arrays.asList(1, 2));
        instance.eq("name", "abc")
                        .isEmpty("post")
                        .notNull("birthday")
                        .in("age", Arrays.asList(1, 2))
                        .getQueryString();        
        assertEquals("select distinct(rootEntity) from org.dayatang.domain.entity.MyEntity as rootEntity  "
                + "where rootEntity.name = :rootEntity_name" + criterion1.hashCode() + " "
                + "and rootEntity.post is empty "
                + "and rootEntity.birthday is not null "
                + "and rootEntity.age in :rootEntity_age" + criterion2.hashCode(),
                instance.getQueryString());
        assertEquals(NamedParameters.create()
                .add("rootEntity_name" + criterion1.hashCode(), "abc")
                .add("rootEntity_age" + criterion2.hashCode(), Arrays.asList(1, 2)),
                instance.getParameters());
    }
    
    @Test
    public void testGetQueryString2() {
    	QueryCriterion criterion1 = Criteria.eq("name", "abc");
    	QueryCriterion criterion2 = Criteria.in("age", Arrays.asList(1, 2));
    	
    	
        assertEquals("select distinct(rootEntity) from org.dayatang.domain.entity.MyEntity as rootEntity  "
                + "where rootEntity.name = :rootEntity_name" + criterion1.hashCode() + " "
                + "and rootEntity.post is empty "
                + "and rootEntity.birthday is not null "
                + "and rootEntity.age in :rootEntity_age" + criterion2.hashCode() + " "
                + "order by rootEntity.name asc", 
                instance.eq("name", "abc")
                        .isEmpty("post")
                        .notNull("birthday")
                        .in("age", Arrays.asList(1, 2))
                        .asc("name")
                        .getQueryString());
    }
}
