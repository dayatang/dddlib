package org.dddlib.codegen.engine.definitions;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class PackageDefinition {
    private String name;
    private String description;
    private Set<ClassDefinition> classes = new HashSet<ClassDefinition>();

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

    public Set<ClassDefinition> getClasses() {
        return Collections.unmodifiableSet(classes);
    }

    public void addClass(ClassDefinition definition) {
        classes.add(definition);
    }

    public void removeClass(ClassDefinition definition) {
        classes.remove(definition);
    }
}
