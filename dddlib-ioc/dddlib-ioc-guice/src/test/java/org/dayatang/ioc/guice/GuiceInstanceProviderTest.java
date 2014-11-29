package org.dayatang.ioc.guice;

import com.google.inject.*;
import com.google.inject.name.Names;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GuiceInstanceProviderTest extends AbstractInstanceProviderTest {

	@Test
	public void testConstructorFromModule() {
		assertEquals("I am Service 1", getProvider().getInstance(Service.class, "service1").sayHello());
	}

	@Test
	public void testConstructorFromInjector() {
		InstanceProvider provider = new GuiceInstanceProvider(createInjector());
		assertEquals("I am Service 1", provider.getInstance(Service.class, "service1").sayHello());
	}

	private Injector createInjector() {
		return Guice.createInjector(createModule());
	}
	

	private Module createModule() {
		return new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Service2.class).to(MyService21.class).in(Scopes.SINGLETON);
				binder.bind(Service.class).annotatedWith(Names.named("service2")).to(MyService2.class).in(Scopes.SINGLETON);
				binder.bind(Service.class).annotatedWith(Names.named("service1")).to(MyService1.class).in(Scopes.SINGLETON);
				binder.bind(Service3.class).annotatedWith(MyBindingAnnotation.class).to(MyService31.class).in(Scopes.SINGLETON);
			}
		};
	}

	@Override
	protected InstanceProvider createInstanceProvider() {
		return new GuiceInstanceProvider(createModule());
	}

    @Override
    @Test
    public void testGetInstanceWithAnnotation() {
        Service3 service = getProvider().getInstance(Service3.class, MyBindingAnnotation.class);
        assertNotNull(service);
    }
}
