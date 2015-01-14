package org.dayatang.domain.internal.bean;

import org.dayatang.domain.InstanceLocator;
import org.dayatang.domain.InstanceProvider;
import org.dayatang.domain.IocInstanceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * Created by yyang on 15/1/14.
 */
public class InstanceProviderInstanceLocator implements InstanceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstanceProviderInstanceLocator.class);

    private InstanceProvider instanceProvider;

    public InstanceProviderInstanceLocator(InstanceProvider instanceProvider) {
        this.instanceProvider = instanceProvider;
    }

    @Override
    public <T> T getInstance(Class<T> beanType) {
        try {
            return instanceProvider.getInstance(beanType);
        } catch (IocInstanceNotFoundException e) {
            LOGGER.warn("InstanceProvider cannot found bean of type {}", beanType);
            return null;
        }
    }

    @Override
    public <T> T getInstance(Class<T> beanType, String beanName) {
        try {
            return instanceProvider.getInstance(beanType, beanName);
        } catch (IocInstanceNotFoundException e) {
            LOGGER.warn("InstanceProvider cannot found bean '{}' of type {}", beanName, beanType);
            return null;
        }
    }

    @Override
    public <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        try {
            return instanceProvider.getInstance(beanType, annotationType);
        } catch (IocInstanceNotFoundException e) {
            LOGGER.warn("InstanceProvider cannot found bean of type {} with annotation {}", beanType, annotationType);
            return null;
        }
    }
}
