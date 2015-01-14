package org.dayatang.domain.internal.bean;

import org.dayatang.domain.InstanceLocator;
import org.dayatang.domain.InstanceLocatorFactory;
import org.dayatang.domain.InstanceProvider;

import java.util.Map;

/**
 * Created by yyang on 15/1/14.
 */
public class InstanceLocatorFactoryImpl implements InstanceLocatorFactory {
    @Override
    public InstanceLocator create(InstanceProvider instanceProvider) {
        return new InstanceProviderInstanceLocator(instanceProvider);
    }

    @Override
    public InstanceLocator createByServiceLoader() {
        return new ServiceLoaderInstanceLocator();
    }

    @Override
    public InstanceLocator create(Map<Object, Object> instances) {
        return new MapInstanceLocator(instances);
    }
}
