package org.dayatang.ioc.test;

import org.dayatang.domain.InstanceProvider;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * 公共的InstanceProvider测试
 * @author Administrator
 *
 */
public abstract class AbstractInstanceProviderTest {

	private InstanceProvider provider;

	abstract protected InstanceProvider createInstanceProvider();

	@Before
	public void setUp() {
		provider = createInstanceProvider();
	}
	
	@Test
	public void testGetInstance() {
		Service1 service = provider.getInstance(Service1.class);
		assertNotNull(service);
		assertEquals("MyService1", service.name());
	}

	@Test
	public void testGetInstanceWithName() {
		assertEquals("MyService21", provider.getInstance(Service2.class, "service21").name());
		assertEquals("MyService22", provider.getInstance(Service2.class, "service22").name());
	}

    @Test
    public void testGetInstanceWithAnnotation() {
        Service2 service = provider.getInstance(Service2.class, TheAnnotation.class);
		assertEquals("MyService23", service.name());
    }

    @Test
    public void testNotFound() {
        assertNull(provider.getInstance(Long.class));
    }

	public InstanceProvider getProvider() {
		return provider;
	}
}
