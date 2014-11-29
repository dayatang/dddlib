package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.AbstractInstanceProviderTest;
import org.dayatang.ioc.test.MyService1;
import org.dayatang.ioc.test.Service;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ProviderWithAnnotationTest extends AbstractInstanceProviderTest {
	
	private SpringInstanceProvider instance;

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new SpringInstanceProvider(SpringConfiguration.class);
	}

    @Test
    public void testGetInstanceByFactoryBean() {
        Service service = getProvider().getInstance(MyService1.class);
        assertEquals("I am Service 1", service.sayHello());
    }

}

