package org.dayatang.ioc.tapestry.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.test.ioc.AbstractInstanceProviderTest;
import org.dayatang.test.ioc.Service;
import org.dayatang.test.ioc.Service2;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TapestryInstanceProviderTest extends AbstractInstanceProviderTest {

	@After
	public void tearDown() throws Exception {
        if (provider != null) {
            ((TapestryInstanceProvider)provider).shutdown();
        }
	}

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new TapestryInstanceProvider(TapestryModule.class);
	}

	@Test
	public void testConstructorFromModule() {
		Service service = provider.getInstance(Service.class, "service1");
		assertEquals("I am Service 1", service.sayHello());
	}

	@Test
	public void testConstructorFromRegistry() {
        TapestryInstanceProvider provider2 = new TapestryInstanceProvider(createRegistry(TapestryModule.class));
		Service2 service = provider2.getInstance(Service2.class);
		assertEquals("I am Service 21", service.sayHello());
        provider2.shutdown();
	}

	private Registry createRegistry(Class<?>... moduleClass) {
		RegistryBuilder builder = new RegistryBuilder();
		for (Class<?> clazz : moduleClass) {
			builder.add(clazz);
		}
		return builder.build();
	}
}

