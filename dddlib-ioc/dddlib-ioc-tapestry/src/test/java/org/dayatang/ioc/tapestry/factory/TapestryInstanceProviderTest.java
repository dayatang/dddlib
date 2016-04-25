package org.dayatang.ioc.tapestry.factory;

import org.apache.tapestry5.ioc.Registry;
import org.apache.tapestry5.ioc.RegistryBuilder;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.AbstractInstanceProviderTest;
import org.dayatang.ioc.test.Service1;
import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TapestryInstanceProviderTest extends AbstractInstanceProviderTest {

    private Registry registry;

    @After
    public void tearDown() throws Exception {
        registry.shutdown();
        registry = null;
    }

    @Override
    protected InstanceProvider createInstanceProvider() {
        registry = RegistryBuilder.buildAndStartupRegistry(TapestryModule.class);
        return new TapestryInstanceProvider(registry);
    }

    @Test
    public void testConstructorFromModule() {
        Service1 service = getProvider().getInstance(Service1.class);
        assertEquals("MyService1", service.name());
    }

    @Test
    public void testConstructorFromRegistry() {
        TapestryInstanceProvider provider2 = new TapestryInstanceProvider(createRegistry(TapestryModule.class));
        Service1 service = provider2.getInstance(Service1.class);
        assertEquals("MyService1", service.name());
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
