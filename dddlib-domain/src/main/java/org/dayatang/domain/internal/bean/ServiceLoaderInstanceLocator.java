package org.dayatang.domain.internal.bean;

import org.dayatang.domain.InstanceLocator;
import org.dayatang.domain.IocInstanceNotUniqueException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Created by yyang on 15/1/14.
 */
public class ServiceLoaderInstanceLocator implements InstanceLocator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceLoaderInstanceLocator.class);

    @Override
    public <T> T getInstance(Class<T> beanType) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            results.add(instance);
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '" + beanType + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        LOGGER.warn("ServiceLoader cannot found bean of type {}", beanType);
        return null;
    }

    @Override
    public <T> T getInstance(Class<T> beanType, String beanName) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            Named named = instance.getClass().getAnnotation(Named.class);
            if (named != null && beanName.equals(named.value())) {
                results.add(instance);
            }
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '"
                    + beanType + "' and named '" + beanName + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        LOGGER.warn("ServiceLoader cannot found bean '{}' of type {}", beanName, beanType);
        return null;
    }

    @Override
    public <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType) {
        Set<T> results = new HashSet<T>();
        for (T instance : ServiceLoader.load(beanType)) {
            Annotation beanAnnotation = instance.getClass().getAnnotation(annotationType);
            if (beanAnnotation != null && beanAnnotation.annotationType().equals(annotationType)) {
                results.add(instance);
            }
        }
        if (results.size() > 1) {
            throw new IocInstanceNotUniqueException("There're more than one bean of type '"
                    + beanType + "' and annotated with '" + annotationType + "'");
        }
        if (results.size() == 1) {
            return results.iterator().next();
        }
        LOGGER.warn("ServiceLoader cannot found bean of type {} with annotation {}", beanType, annotationType);
        return null;
    }
}
