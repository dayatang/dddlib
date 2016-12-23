package org.dddlib.codegen.api;

import org.dddlib.codegen.api.ClassDefinition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/21.
 */
public class PackageDefinition<T extends ClassDefinition> implements ClassDefinition {
    private String name;
    private String description;
    private Set<T> mappedSuperClasses = new HashSet<DomainClassDefinition>();
    private Set<T> entities = new HashSet<DomainClassDefinition>();
    private Set<T> valueObjects = new HashSet<DomainClassDefinition>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<DomainClassDefinition> getMappedSuperClasses() {
        return mappedSuperClasses;
    }

    public void setMappedSuperClasses(Set<DomainClassDefinition> mappedSuperClasses) {
        this.mappedSuperClasses = mappedSuperClasses;
    }

    public Set<DomainClassDefinition> getEntities() {
        return entities;
    }

    public void setEntities(Set<DomainClassDefinition> entities) {
        this.entities = entities;
    }

    public Set<DomainClassDefinition> getValueObjects() {
        return valueObjects;
    }

    public void setValueObjects(Set<DomainClassDefinition> valueObjects) {
        this.valueObjects = valueObjects;
    }
}
