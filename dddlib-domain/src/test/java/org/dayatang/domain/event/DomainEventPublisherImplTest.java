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
package org.dayatang.domain.event;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author yyang
 */
public class DomainEventPublisherImplTest {

    private DomainEventPublisherImpl instance;
    private DomainEvent event1 = new SubDomainEvent();
    private DomainEvent event2 = new SubDomainEvent();
    private DomainEventSubscriber subscriber1;
    private DomainEventSubscriber subscriber2;
    private DomainEventSubscriber subscriber3;

    @Before
    public void setUp() {
        instance = new DomainEventPublisherImpl();
        subscriber1 = mock(DomainEventSubscriber.class);
        subscriber2 = mock(DomainEventSubscriber.class);
        subscriber3 = mock(DomainEventSubscriber.class);
    }

    /**
     * Test of publish method, of class DomainEventPublisherImpl.
     */
    @Test
    public void testPublish() {
        instance.subscribe(subscriber1);
        instance.subscribe(subscriber2);
        instance.publish(event1);
        verify(subscriber1).handleEvent(event1);
        verify(subscriber2).handleEvent(event1);
    }

    /**
     * Test of publishAll method, of class DomainEventPublisherImpl.
     */
    @Test
    public void testPublishAll() {
        instance.subscribe(subscriber1);
        instance.subscribe(subscriber2);
        instance.publishAll(Arrays.asList(event1, event2));
        verify(subscriber1).handleEvent(event1);
        verify(subscriber1).handleEvent(event2);
        verify(subscriber2).handleEvent(event1);
        verify(subscriber2).handleEvent(event2);
    }

    /**
     * Test of publishAll method, of class DomainEventPublisherImpl.
     */
    @Test
    public void testPublishAllNull() {
        instance.subscribe(subscriber1);
        instance.subscribe(subscriber2);
        instance.publishAll(null);
        verify(subscriber1, never()).handleEvent(event1);
        verify(subscriber1, never()).handleEvent(event2);
        verify(subscriber2, never()).handleEvent(event1);
        verify(subscriber2, never()).handleEvent(event2);
    }

    /**
     * Test of subscribe method, of class DomainEventPublisherImpl.
     */
    @Test
    public void testSubscribe() {
        instance.subscribe(subscriber1);
        instance.subscribe(subscriber2);
        assertTrue(instance.hasSubscriber(subscriber1));
        assertTrue(instance.hasSubscriber(subscriber2));
        assertFalse(instance.hasSubscriber(subscriber3));
    }

    /**
     * Test of unsubscribe method, of class DomainEventPublisherImpl.
     */
    @Test
    public void testUnsubscribe() {
        instance.subscribe(subscriber1);
        instance.subscribe(subscriber2);
        assertTrue(instance.hasSubscriber(subscriber1));
        assertTrue(instance.hasSubscriber(subscriber2));
        instance.unsubscribe(subscriber1);
        assertFalse(instance.hasSubscriber(subscriber1));
        assertTrue(instance.hasSubscriber(subscriber2));
    }

    /**
     * Test of clearSubscribers method, of class DomainEventPublisherImpl.
     */
    @Test
    public void testClearSubscribers() {
        instance.subscribe(subscriber1);
        instance.subscribe(subscriber2);
        assertTrue(instance.hasSubscriber(subscriber1));
        assertTrue(instance.hasSubscriber(subscriber2));
        instance.clearSubscribers();
        assertFalse(instance.hasSubscriber(subscriber1));
        assertFalse(instance.hasSubscriber(subscriber2));
        assertFalse(instance.hasSubscribers());
    }

    private class SubDomainEvent extends DomainEvent {
        public SubDomainEvent() {
        }

        public SubDomainEvent(Date occurredOn) {
            super(occurredOn);
        }

        public SubDomainEvent(Date occurredOn, int version) {
            super(occurredOn, version);
        }
    }

}
