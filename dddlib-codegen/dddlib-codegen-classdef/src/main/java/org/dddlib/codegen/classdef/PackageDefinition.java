package org.dddlib.codegen.classdef;

import org.dddlib.codegen.api.ClassDefinition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class PackageDefinition implements ClassDefinition {
    private String name;
    private String description;
    private Set<BaseDefinition> classes = new HashSet<BaseDefinition>();

    public PackageDefinition(String name) {
        this.name = name;
    }

    public PackageDefinition(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Set<BaseDefinition> getClasses() {
        return Collections.unmodifiableSet(classes);
    }

    public void addClass(EntityDefinition definition) {
        classes.add(definition);
    }

    public void removeClass(EntityDefinition definition) {
        classes.remove(definition);
    }
}
