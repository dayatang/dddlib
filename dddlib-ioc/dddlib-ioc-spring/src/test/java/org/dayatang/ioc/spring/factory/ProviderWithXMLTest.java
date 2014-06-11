package org.dayatang.ioc.spring.factory;

import org.dayatang.domain.InstanceProvider;
import org.dayatang.ioc.test.AbstractInstanceProviderTest;

/**
 * Created by yyang on 14-6-11.
 */
public class ProviderWithXMLTest extends AbstractInstanceProviderTest {

    @Override
    protected InstanceProvider createInstanceProvider() {
        return new SpringInstanceProvider("applicationContext.xml");
    }
}
