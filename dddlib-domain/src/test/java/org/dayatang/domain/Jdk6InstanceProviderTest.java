package org.dayatang.domain;

import org.dayatang.domain.ioc.Service;
import org.dayatang.domain.ioc.Service2;
import org.dayatang.domain.ioc.TheAnnotation;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class Jdk6InstanceProviderTest {

    private InstanceProvider provider = new Jdk6InstanceProvider();

    @Test
    public void testGetInstance() {
        Service2 service = provider.getInstance(Service2.class);
        assertEquals("I am Service 21", service.sayHello());
    }

    @Test
    public void testGetInstanceWithName() {
        Service service = provider.getInstance(Service.class, "service2");
        assertEquals("I am Service 2", service.sayHello());
    }

    @Test
    public void testGetInstanceWithAnnotation() {
        Service service = provider.getInstance(Service.class, TheAnnotation.class);
        assertEquals("I am Service 3", service.sayHello());
    }

    @Test
    public void testNotFound() {
        assertNull(provider.getInstance(Long.class));
    }

}
