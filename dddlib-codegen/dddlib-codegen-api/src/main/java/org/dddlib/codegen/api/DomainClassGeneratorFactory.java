package org.dddlib.codegen.api;

import java.util.ServiceLoader;

/**
 * Created by yyang on 2016/12/21.
 */
public class DomainClassGeneratorFactory {
    public static DomainClassGenerator getInstance() {
        ServiceLoader<DomainClassGenerator> loader = ServiceLoader.load(DomainClassGenerator.class);
        if (loader.iterator().hasNext()) {
            return loader.iterator().next();
        }
        throw new RuntimeException("Not found implementation class of org.dddlib.codegen.api.DomainClassGenerator");
    }
}
