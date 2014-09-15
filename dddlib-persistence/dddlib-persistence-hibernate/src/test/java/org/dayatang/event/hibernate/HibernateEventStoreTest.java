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
package org.dayatang.event.hibernate;

import org.dayatang.domain.event.DomainEvent;
import org.dayatang.domain.event.StoredEvent;
import org.junit.*;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author yyang
 */
public class HibernateEventStoreTest {


    public HibernateEventStoreTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of findStoredEventsBetween method, of class HibernateEventStore.
     */
    @Test
    public void testFindStoredEventsBetween() {
        System.out.println("findStoredEventsBetween");
        Date occurredFrom = null;
        Date occurredTo = null;
        HibernateEventStore instance = new HibernateEventStore();
        List<StoredEvent> expResult = null;
        List<StoredEvent> result = instance.findStoredEventsBetween(occurredFrom, occurredTo);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of findStoredEventsSince method, of class HibernateEventStore.
     */
    @Test
    public void testFindStoredEventsSince() {
        System.out.println("findStoredEventsSince");
        Date occurredFrom = null;
        HibernateEventStore instance = new HibernateEventStore();
        List<StoredEvent> expResult = null;
        List<StoredEvent> result = instance.findStoredEventsSince(occurredFrom);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of append method, of class HibernateEventStore.
     */
    @Test
    public void testAppend() {
        System.out.println("append");
        DomainEvent domainEvent = null;
        HibernateEventStore instance = new HibernateEventStore();
        StoredEvent expResult = null;
        StoredEvent result = instance.append(domainEvent);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of close method, of class HibernateEventStore.
     */
    @Test
    public void testClose() {
        System.out.println("close");
        HibernateEventStore instance = new HibernateEventStore();
        instance.close();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of countStoredEvents method, of class HibernateEventStore.
     */
    @Test
    public void testCountStoredEvents() {
        System.out.println("countStoredEvents");
        HibernateEventStore instance = new HibernateEventStore();
        long expResult = 0L;
        long result = instance.countStoredEvents();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
