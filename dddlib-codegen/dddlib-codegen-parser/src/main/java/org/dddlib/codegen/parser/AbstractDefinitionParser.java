package org.dddlib.codegen.parser;

import org.dddlib.codegen.api.DefinitionParser;
import org.dddlib.codegen.classdef.ClassDefinition;
import org.dddlib.codegen.classdef.DomainClassDefinition;
import org.dddlib.codegen.classdef.PackageDefinition;

import java.io.File;
import java.io.Reader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/23.
 */
public abstract class AbstractDefinitionParser implements DefinitionParser {

    protected Set<ClassDefinition> toClassDefinitions(PackageDefinition packageDefinition) {
        Set<ClassDefinition> results = new HashSet<ClassDefinition>();
        results.add(packageDefinition);
        for (DomainClassDefinition each: packageDefinition.getMappedSuperClasses()) {
            each.setPkg(packageDefinition.getName());
            results.add(each);
        }
        results.addAll(packageDefinition.getMappedSuperClasses());
        results.addAll(packageDefinition.getEntities());
        results.addAll(packageDefinition.getValueObjects());
        return results;
    }
}
