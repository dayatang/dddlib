package org.dayatang.domain;

import java.lang.annotation.Annotation;

/**
 * Bean实例定位
 * Created by yyang on 15/1/14.
 */
public interface InstanceLocator {

    public abstract  <T> T getInstance(Class<T> beanType);

    public abstract <T> T getInstance(Class<T> beanType, String beanName);

    public abstract <T> T getInstance(Class<T> beanType, Class<? extends Annotation> annotationType);
}
