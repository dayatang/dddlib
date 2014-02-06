package org.dayatang.domain;

import org.dayatang.domain.ioc.Service;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Jdk6InstanceProviderTest {

    private InstanceProvider provider = new Jdk6InstanceProvider();

    @Test
    public void testGetInstance() {
        Service service = provider.getInstance(Service.class);
        assertEquals("I am Service 1", service.sayHello());
    }

    @Test
    public void testGetInstanceWithName() {
        Service service = provider.getInstance(Service.class, "service2");
        assertEquals("I am Service 2", service.sayHello());
    }

}
