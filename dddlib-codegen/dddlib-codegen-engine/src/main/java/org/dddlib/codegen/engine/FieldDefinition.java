package org.dddlib.codegen.engine;

/**
 * 字段定义
 * Created by yyang on 2016/12/21.
 */
public class FieldDefinition {

    //字段类别
    private FieldCategory type;

    //多重性（to_one还是to_many）
    private Cardinality cardinality;

    //字段名
    private String name;

    //数据类型
    private String dataType;

    //缺省值
    private String defaultValue;

    //字段说明
    private String description;

    public FieldCategory getType() {
        return type;
    }

    public void setType(FieldCategory type) {
        this.type = type;
    }

    public Cardinality getCardinality() {
        return cardinality;
    }

    public void setCardinality(Cardinality cardinality) {
        this.cardinality = cardinality;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
