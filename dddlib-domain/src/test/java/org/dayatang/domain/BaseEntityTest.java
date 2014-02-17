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

import org.dayatang.domain.entity.Company;
import org.dayatang.domain.entity.Dept;
import org.dayatang.domain.entity.Organization;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 * @author yyang
 */
public class BaseEntityTest {

    private EntityRepository repository;

    private final Organization guangdong = new Company("Guangdong", 1);

    private final Organization guangzhou = new Company("Guangdong", 2);

    private final Organization financial = new Dept("Guangdong", 1);

    @Before
    public void setUp() {
        repository = mock(EntityRepository.class);
        BaseEntity.setRepository(repository);
    }

    @After
    public void tearDown() {
        reset(repository);
        BaseEntity.setRepository(null);
    }

    @Test
    public void testExistedIdIsNull() {
        guangdong.setId(null);
        assertFalse(guangdong.existed());
    }

    @Test
    public void testExistedIdIsZero() {
        guangdong.setId(0);
        assertFalse(guangdong.existed());
    }

    @Test
    public void testExistedRepositoryNotFound() {
        guangdong.setId(3);
        when(repository.exists(Organization.class, 3)).thenReturn(false);
        assertFalse(guangdong.existed());
    }

    @Test
    public void testExistedRepositoryFound() {
        guangdong.setId(3);
        when(repository.exists(Company.class, 3)).thenReturn(true);
        assertTrue(guangdong.existed());
    }

    @Test
    public void testNotExistedIdIsNull() {
        guangdong.setId(null);
        assertTrue(guangdong.notExisted());
    }

    @Test
    public void testNotExistedIdIsZero() {
        guangdong.setId(0);
        assertTrue(guangdong.notExisted());
    }

    @Test
    public void testNotExistedRepositoryNotFound() {
        guangdong.setId(3);
        when(repository.exists(Company.class, 3)).thenReturn(false);
        assertTrue(guangdong.notExisted());
    }

    @Test
    public void testNotExistedRepositoryFound() {
        guangdong.setId(3);
        when(repository.exists(Company.class, 3)).thenReturn(true);
        assertFalse(guangdong.notExisted());
    }

    @Test
    public void testNotExisted() {
        guangdong.setId(null);
        assertTrue(guangdong.notExisted());

        guangdong.setId(0);
        assertTrue(guangdong.notExisted());

        guangdong.setId(3);
        when(repository.exists(Company.class, 3)).thenReturn(false);
        assertTrue(guangdong.notExisted());

        reset(repository);
        when(repository.exists(Company.class, 3)).thenReturn(true);
        assertFalse(guangdong.notExisted());
    }

    @Test
    public void testHashCode() {
        assertEquals(guangdong.hashCode(), new Company("Guangdong", 1).hashCode());
    }

    @Test
    public void testEquals() {
        //对象永远等价于自身
        assertTrue(guangdong.equals(guangdong));
        
        //类型相同、业务主键不同的对象不等价
        assertFalse(guangdong.equals(guangzhou));
        
        //类型不同、业务主键相同的对象不等价
        assertFalse(guangdong.equals(financial));
        
        //非业务主键不影响等价性
        guangdong.disable();
        assertTrue(guangdong.equals(new Company("Guangdong", 1)));
        
        //如果a.equals(b)，那么b.equals(a)
        assertTrue(new Company("Guangdong", 1).equals(guangdong));
    }

}
