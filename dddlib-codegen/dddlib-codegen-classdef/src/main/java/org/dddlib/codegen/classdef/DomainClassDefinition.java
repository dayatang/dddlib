package org.dddlib.codegen.classdef;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by yyang on 2016/12/23.
 */
public class DomainClassDefinition implements ClassDefinition {

    private String pkg;
    private String name;
    private String description;
    private boolean isAbstract = false;
    private Set<String> pkProps = new HashSet<String>();
    private Set<String> uniqueProps = new HashSet<String>();
    private List<String> annotations = new ArrayList<String>();
    private List<FieldDefinition> fields = new ArrayList<FieldDefinition>();

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

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

    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    public Set<String> getPkProps() {
        return pkProps;
    }

    public void setPkProps(Set<String> pkProps) {
        this.pkProps = pkProps;
    }

    public Set<String> getUniqueProps() {
        return uniqueProps;
    }

    public void setUniqueProps(Set<String> uniqueProps) {
        this.uniqueProps = uniqueProps;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    public List<FieldDefinition> getFields() {
        return fields;
    }

    public void setFields(List<FieldDefinition> fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DomainClassDefinition)) {
            return false;
        }

        DomainClassDefinition that = (DomainClassDefinition) o;

        if (!getPkg().equals(that.getPkg())) {
            return false;
        }
        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        int result = getPkg().hashCode();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
