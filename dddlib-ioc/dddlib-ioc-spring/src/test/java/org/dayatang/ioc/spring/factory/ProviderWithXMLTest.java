package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.AbstractInstanceProviderTest;
import org.dayatang.ioc.test.MyService1;
import org.dayatang.ioc.test.Service;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
