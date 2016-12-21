package org.dddlib.codegen.engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *领域类定义
 * Created by yyang on 2016/12/21.
 */
public abstract class ClassDefinition {
    private DomainType domainType;
    private String packageName;
    private String className;
    private String description;
    private boolean isAbstract = false;
    private Set<String> pkProps = new HashSet<String>();
    private Set<String> uniqueProps = new HashSet<String>();
    private List<FieldDefinition> props = new ArrayList<FieldDefinition>();

    public DomainType getDomainType() {
        return domainType;
    }

    public void setDomainType(DomainType domainType) {
        this.domainType = domainType;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
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

    public List<FieldDefinition> getProps() {
        return props;
    }

    public void setProps(List<FieldDefinition> props) {
        this.props = props;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassDefinition)) {
            return false;
        }

        ClassDefinition that = (ClassDefinition) o;

        if (!getPackageName().equals(that.getPackageName())) {
            return false;
        }
        return getClassName().equals(that.getClassName());
    }

    @Override
    public int hashCode() {
        int result = getPackageName().hashCode();
        result = 31 * result + getClassName().hashCode();
        return result;
    }
}
