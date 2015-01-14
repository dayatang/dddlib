package org.dayatang.domain;

import java.util.Map;

/**
 * Created by yyang on 15/1/14.
 */
public interface InstanceLocatorFactory {
    InstanceLocator create(InstanceProvider instanceProvider);
    InstanceLocator createByServiceLoader();
    InstanceLocator create(Map<Object, Object> instances);
}
