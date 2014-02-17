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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dayatang.domain.CriteriaQuery;
import org.dayatang.domain.EntityRepository;
import org.dayatang.domain.OrderSetting;
import org.dayatang.domain.QueryCriterion;
import org.dayatang.domain.entity.MyEntity;
import org.dayatang.domain.internal.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        List<OrderSetting> orderSettings = new ArrayList<OrderSetting>();
        orderSettings.add(OrderSetting.asc("id"));
        orderSettings.add(OrderSetting.desc("name"));
        instance.asc("id").desc("name");
        assertEquals(orderSettings, instance.getOrderSettings());
    }

    /**
     * Test of eq method, of class CriteriaQuery.
     */
    @Test
    public void testEq() {
        assertTrue(instance.eq("name", "abc").getQueryCriterions()
                .contains(new EqCriterion("name", "abc")));
    }

    /**
     * Test of notEq method, of class CriteriaQuery.
     */
    @Test
    public void testNotEq() {
        assertTrue(instance.notEq("name", "abc").getQueryCriterions()
                .contains(new NotEqCriterion("name", "abc")));
    }

    /**
     * Test of gt method, of class CriteriaQuery.
     */
    @Test
    public void testGt() {
        assertTrue(instance.gt("id", 5).getQueryCriterions()
                .contains(new GtCriterion("id", 5)));
    }

    /**
     * Test of ge method, of class CriteriaQuery.
     */
    @Test
    public void testGe() {
        assertTrue(instance.ge("id", 5).getQueryCriterions()
                .contains(new GeCriterion("id", 5)));
    }

    /**
     * Test of lt method, of class CriteriaQuery.
     */
    @Test
    public void testLt() {
        assertTrue(instance.lt("id", 5).getQueryCriterions()
                .contains(new LtCriterion("id", 5)));
    }

    /**
     * Test of le method, of class CriteriaQuery.
     */
    @Test
    public void testLe() {
        assertTrue(instance.le("id", 5).getQueryCriterions()
                .contains(new LeCriterion("id", 5)));
    }

    /**
     * Test of eqProp method, of class CriteriaQuery.
     */
    @Test
    public void testEqProp() {
        assertTrue(instance.eqProp("id", "name").getQueryCriterions()
                .contains(new EqPropCriterion("id", "name")));
    }

    /**
     * Test of notEqProp method, of class CriteriaQuery.
     */
    @Test
    public void testNotEqProp() {
        assertTrue(instance.notEqProp("id", "name").getQueryCriterions()
                .contains(new NotEqPropCriterion("id", "name")));
    }

    /**
     * Test of gtProp method, of class CriteriaQuery.
     */
    @Test
    public void testGtProp() {
        assertTrue(instance.gtProp("id", "name").getQueryCriterions()
                .contains(new GtPropCriterion("id", "name")));
    }

    /**
     * Test of geProp method, of class CriteriaQuery.
     */
    @Test
    public void testGeProp() {
        assertTrue(instance.geProp("id", "name").getQueryCriterions()
                .contains(new GePropCriterion("id", "name")));
    }

    /**
     * Test of ltProp method, of class CriteriaQuery.
     */
    @Test
    public void testLtProp() {
        assertTrue(instance.ltProp("id", "name").getQueryCriterions()
                .contains(new LtPropCriterion("id", "name")));
    }

    /**
     * Test of leProp method, of class CriteriaQuery.
     */
    @Test
    public void testLeProp() {
        assertTrue(instance.leProp("id", "name").getQueryCriterions()
                .contains(new LePropCriterion("id", "name")));
    }

    /**
     * Test of sizeEq method, of class CriteriaQuery.
     */
    @Test
    public void testSizeEq() {
        assertTrue(instance.sizeEq("id", 3).getQueryCriterions()
                .contains(new SizeEqCriterion("id", 3)));
    }

    /**
     * Test of sizeNotEq method, of class CriteriaQuery.
     */
    @Test
    public void testSizeNotEq() {
        assertTrue(instance.sizeNotEq("id", 3).getQueryCriterions()
                .contains(new SizeNotEqCriterion("id", 3)));
    }

    /**
     * Test of sizeGt method, of class CriteriaQuery.
     */
    @Test
    public void testSizeGt() {
        assertTrue(instance.sizeGt("id", 3).getQueryCriterions()
                .contains(new SizeGtCriterion("id", 3)));
    }

    /**
     * Test of sizeGe method, of class CriteriaQuery.
     */
    @Test
    public void testSizeGe() {
        assertTrue(instance.sizeGe("id", 3).getQueryCriterions()
                .contains(new SizeGeCriterion("id", 3)));
    }

    /**
     * Test of sizeLt method, of class CriteriaQuery.
     */
    @Test
    public void testSizeLt() {
        assertTrue(instance.sizeLt("id", 3).getQueryCriterions()
                .contains(new SizeLtCriterion("id", 3)));
    }

    /**
     * Test of sizeLe method, of class CriteriaQuery.
     */
    @Test
    public void testSizeLe() {
        assertTrue(instance.sizeLe("id", 3).getQueryCriterions()
                .contains(new SizeLeCriterion("id", 3)));
    }

    /**
     * Test of containsText method, of class CriteriaQuery.
     */
    @Test
    public void testContainsText() {
        assertTrue(instance.containsText("name", "a").getQueryCriterions()
                .contains(new ContainsTextCriterion("name", "a")));
    }

    /**
     * Test of startsWithText method, of class CriteriaQuery.
     */
    @Test
    public void testStartsWithText() {
        assertTrue(instance.startsWithText("name", "a").getQueryCriterions()
                .contains(new StartsWithTextCriterion("name", "a")));
    }

    /**
     * Test of in method, of class CriteriaQuery.
     */
    @Test
    public void testInCollection() {
        List<?> criterions = Arrays.asList("a", "b");
        assertTrue(instance.in("name", criterions).getQueryCriterions()
                .contains(new InCriterion("name", criterions)));
    }

    /**
     * Test of in method, of class CriteriaQuery.
     */
    @Test
    public void testIn_Array() {
        Object[] criterions = new Object[] {"a", "b"};
        assertTrue(instance.in("name", criterions).getQueryCriterions()
                .contains(new InCriterion("name", criterions)));
    }

    /**
     * Test of notIn method, of class CriteriaQuery.
     */
    @Test
    public void testNotInCollection() {
        List<?> criterions = Arrays.asList("a", "b");
        assertTrue(instance.notIn("name", criterions).getQueryCriterions()
                .contains(new NotInCriterion("name", criterions)));
    }

    /**
     * Test of notIn method, of class CriteriaQuery.
     */
    @Test
    public void testNotIn_Array() {
        Object[] criterions = new Object[] {"a", "b"};
        assertTrue(instance.notIn("name", criterions).getQueryCriterions()
                .contains(new NotInCriterion("name", criterions)));
    }

    /**
     * Test of between method, of class CriteriaQuery.
     */
    @Test
    public void testBetween() {
        assertTrue(instance.between("name", "a", "b").getQueryCriterions()
                .contains(new BetweenCriterion("name", "a", "b")));
    }

    /**
     * Test of isNull method, of class CriteriaQuery.
     */
    @Test
    public void testIsNull() {
        assertTrue(instance.isNull("name").getQueryCriterions()
                .contains(new IsNullCriterion("name")));
    }

    /**
     * Test of notNull method, of class CriteriaQuery.
     */
    @Test
    public void testNotNull() {
        assertTrue(instance.notNull("name").getQueryCriterions()
                .contains(new NotNullCriterion("name")));
    }

    /**
     * Test of isEmpty method, of class CriteriaQuery.
     */
    @Test
    public void testIsEmpty() {
        assertTrue(instance.isEmpty("name").getQueryCriterions()
                .contains(new IsEmptyCriterion("name")));
    }

    /**
     * Test of notEmpty method, of class CriteriaQuery.
     */
    @Test
    public void testNotEmpty() {
        assertTrue(instance.notEmpty("name").getQueryCriterions()
                .contains(new NotEmptyCriterion("name")));
    }

    /**
     * Test of isTrue method, of class CriteriaQuery.
     */
    @Test
    public void testIsTrue() {
        assertTrue(instance.isTrue("name").getQueryCriterions()
                .contains(new EqCriterion("name", true)));
    }

    /**
     * Test of isFalse method, of class CriteriaQuery.
     */
    @Test
    public void testIsFalse() {
        assertTrue(instance.isFalse("name").getQueryCriterions()
                .contains(new EqCriterion("name", false)));
    }

    /**
     * Test of isBlank method, of class CriteriaQuery.
     */
    @Test
    public void testIsBlank() {
        QueryCriterion criterion1 = new IsNullCriterion("name");
        QueryCriterion criterion2 = new EqCriterion("name", "");
        QueryCriterion criterion3 = new OrCriterion(criterion1, criterion2);
        
        assertTrue(instance.isBlank("name").getQueryCriterions()
                .contains(criterion3));
    }

    /**
     * Test of notBlank method, of class CriteriaQuery.
     */
    @Test
    public void testNotBlank() {
        QueryCriterion criterion1 = new NotNullCriterion("name");
        QueryCriterion criterion2 = new NotEqCriterion("name", "");
        QueryCriterion criterion3 = new AndCriterion(criterion1, criterion2);
        
        assertTrue(instance.notBlank("name").getQueryCriterions()
                .contains(criterion3));
    }

    /**
     * Test of not method, of class CriteriaQuery.
     */
    @Test
    public void testNot() {
        QueryCriterion criterion1 = new EqCriterion("name", "abc");
        QueryCriterion criterion2 = new NotCriterion(criterion1);
        
        assertTrue(instance.not(criterion1).getQueryCriterions()
                .contains(criterion2));
    }

    /**
     * Test of and method, of class CriteriaQuery.
     */
    @Test
    public void testAnd() {
        QueryCriterion criterion1 = new EqCriterion("name", "abc");
        QueryCriterion criterion2 = new GtCriterion("id", 5);
        QueryCriterion criterion3 = new AndCriterion(criterion1, criterion2);
        
        assertTrue(instance.and(criterion1, criterion2).getQueryCriterions()
                .contains(criterion3));
    }

    /**
     * Test of or method, of class CriteriaQuery.
     */
    @Test
    public void testOr() {
        QueryCriterion criterion1 = new EqCriterion("name", "abc");
        QueryCriterion criterion2 = new GtCriterion("id", 5);
        QueryCriterion criterion3 = new OrCriterion(criterion1, criterion2);
        
        assertTrue(instance.or(criterion1, criterion2).getQueryCriterions()
                .contains(criterion3));
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
    
}
