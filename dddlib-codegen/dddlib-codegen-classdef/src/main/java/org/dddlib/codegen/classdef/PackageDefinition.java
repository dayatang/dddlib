package org.dddlib.codegen.classdef;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by yyang on 2016/12/26.
 */
public class PackageDefinition implements ClassDefinition {
    private String name;
    private String description;
    private Set<MspDefinition> mappedSuperClasses = new HashSet<MspDefinition>();
    private Set<EntityDefinition> entities = new HashSet<EntityDefinition>();
    private Set<ValueObjectDefinition> valueObjects = new HashSet<ValueObjectDefinition>();

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

    public Set<MspDefinition> getMappedSuperClasses() {
        return mappedSuperClasses;
    }

    public void setMappedSuperClasses(Set<MspDefinition> mappedSuperClasses) {
        this.mappedSuperClasses = mappedSuperClasses;
    }

    public Set<EntityDefinition> getEntities() {
        return entities;
    }

    public void setEntities(Set<EntityDefinition> entities) {
        this.entities = entities;
    }

    public Set<ValueObjectDefinition> getValueObjects() {
        return valueObjects;
    }

    public void setValueObjects(Set<ValueObjectDefinition> valueObjects) {
        this.valueObjects = valueObjects;
    }

    public Set<ClassDefinition> toClassDefinitions() {
        Set<ClassDefinition> results = new HashSet<ClassDefinition>();
        results.add(this);
        for (MspDefinition each: getMappedSuperClasses()) {
            each.setPkg(getName());
            results.add(each);
        }
        for (EntityDefinition each: getEntities()) {
            each.setPkg(getName());
            results.add(each);
        }
        for (ValueObjectDefinition each: getValueObjects()) {
            each.setPkg(getName());
            results.add(each);
        }
        return results;
    }

}
