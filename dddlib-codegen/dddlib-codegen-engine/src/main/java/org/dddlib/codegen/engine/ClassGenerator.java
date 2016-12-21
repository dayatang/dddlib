package org.dddlib.codegen.engine;

import org.dddlib.codegen.api.JavaSourceFile;
import org.dddlib.codegen.engine.definitions.ClassDefinition;

import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public interface ClassGenerator {
    JavaSourceFile generate(ClassDefinition definition);
}
