package org.dddlib.codegen.api;


import org.dddlib.codegen.classdef.ClassDefinition;

/**
 * Created by yyang on 2016/12/21.
 */
public interface ClassGenerator {
    JavaSourceFile generate(ClassDefinition definition);
}
