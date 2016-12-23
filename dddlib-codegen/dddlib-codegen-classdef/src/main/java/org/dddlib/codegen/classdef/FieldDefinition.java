package org.dddlib.codegen.classdef;

import java.util.ArrayList;
import java.util.List;

/**
 * 字段定义
 * Created by yyang on 2016/12/21.
 */
public class FieldDefinition {

    //字段名
    private String name;

    //字段说明
    private String description;

    //字段类型
    private String type;

    //关系类型
    private RelationType relation = RelationType.VALUE;

    //集合类型
    private CollectionType collectionType = CollectionType.NONE;

    //缺省值
    private String defaultValue;

    //注解
    private List<String> annotations = new ArrayList<String>();

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RelationType getRelation() {
        return relation;
    }

    public void setRelation(RelationType relation) {
        this.relation = relation;
    }

    public CollectionType getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(CollectionType collectionType) {
        this.collectionType = collectionType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public List<String> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(List<String> annotations) {
        this.annotations = annotations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FieldDefinition)) {
            return false;
        }

        FieldDefinition that = (FieldDefinition) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
