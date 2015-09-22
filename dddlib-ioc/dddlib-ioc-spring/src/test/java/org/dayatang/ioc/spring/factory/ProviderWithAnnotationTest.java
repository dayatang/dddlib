package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ProviderWithAnnotationTest extends AbstractInstanceProviderTest {
	
	private SpringInstanceProvider instance;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringInstanceProvider(SpringConfiguration.class);
	}

    @Test
    public void testGetInstanceByFactoryBean() {
        Service1 service = getProvider().getInstance(Service1.class);
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

