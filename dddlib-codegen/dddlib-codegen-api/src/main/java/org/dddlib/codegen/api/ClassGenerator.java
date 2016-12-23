package org.dddlib.codegen.api;


/**
 * Created by yyang on 2016/12/21.
 */
public interface ClassGenerator {
    JavaSourceFile generate(ClassDefinition definition);
}
