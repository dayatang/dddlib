package org.dayatang.ioc.tapestry.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.test.ioc.AbstractInstanceProviderTest;
import org.dayatang.test.ioc.Service;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TapestryInstanceProviderTest extends AbstractInstanceProviderTest {

	@After
	public void tearDown() throws Exception {
		((TapestryInstanceProvider)provider).shutdown();
	}

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new TapestryInstanceProvider(WithAnnotationModule.class);
	}

	@Override
	public void testGetInstance() {
		provider = new TapestryInstanceProvider(WithoutAnnotationModule.class);
		super.testGetInstance();
	}

	@Test
	public void testConstructorFromModule() {
		provider = new TapestryInstanceProvider(WithoutAnnotationModule.class);
		Service service = provider.getInstance(Service.class);
		assertEquals("I am Service 1", service.sayHello());
	}

	@Test
	public void testConstructorFromRegistry() {
		provider = new TapestryInstanceProvider(createRegistry(WithoutAnnotationModule.class));
		Service service = provider.getInstance(Service.class);
		assertEquals("I am Service 1", service.sayHello());
	}

	private Registry createRegistry(Class<?>... moduleClass) {
		RegistryBuilder builder = new RegistryBuilder();
		for (Class<?> clazz : moduleClass) {
			builder.add(clazz);
		}
		return builder.build();
	}
}

