package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by yyang on 14-6-11.
 */
public class ProviderWithXMLTest extends AbstractInstanceProviderTest {

    @Override
    protected InstanceProvider createInstanceProvider() {
        return new SpringInstanceProvider("applicationContext.xml");
    }

    @Test
    public void testGetInstanceByFactoryBean() {
        Service service = getProvider().getInstance(MyService1.class);
        assertEquals("I am Service 1", service.sayHello());
    }

    @Test
    public void testGetInstances() {
        List<Service> expected = Arrays.asList(
                new MyService1(),
                new MyService21(),
                new MyService22(),
                new MyService23(),
                new MyService3()
        );
        Set<Service> services = getProvider().getInstances(Service.class);
        assertTrue(services.containsAll(expected));
    }
}
