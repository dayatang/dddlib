package org.dayatang.ioc.guice;

import com.google.inject.*;
import com.google.inject.name.Names;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class GuiceInstanceProviderTest extends AbstractInstanceProviderTest {

	@Test
	public void testConstructorFromModule() {
		assertEquals("I am Service 1", getProvider().getInstance(Service1.class).sayHello());
	}

	@Test
	public void testConstructorFromInjector() {
		InstanceProvider provider = new GuiceInstanceProvider(createInjector());
		assertEquals("I am Service 1", provider.getInstance(Service1.class).sayHello());
	}

	private Injector createInjector() {
		return Guice.createInjector(createModule());
	}

	private Module createModule() {
		return new Module() {
			@Override
			public void configure(Binder binder) {
				binder.bind(Service1.class).to(MyService1.class).in(Scopes.SINGLETON);
				binder.bind(Service2.class).annotatedWith(Names.named("service21")).to(MyService21.class).in(Scopes.SINGLETON);
				binder.bind(Service2.class).annotatedWith(Names.named("service22")).to(MyService22.class).in(Scopes.SINGLETON);
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

	@Test
	public void testGetInstances() {
		List<Service> expected = Arrays.asList(
				new MyService1(),
				new MyService21(),
				new MyService22(),
				new MyService31()
		);
		Set<Service> services = getProvider().getInstances(Service.class);
		assertTrue(services.containsAll(expected));
	}
}
