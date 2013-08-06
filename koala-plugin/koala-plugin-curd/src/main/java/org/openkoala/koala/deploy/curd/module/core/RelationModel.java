package org.openkoala.koala.deploy.curd.module.core;

/**
 * 
 * 类    名：RelationModel.java
 *   
 * 功能描述：实体关联的抽象父类
 *  
 * 创建日期：2013-1-18下午3:22:43     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public abstract class RelationModel {
    
    /**
     * 关联关系对应对方对象
     */
    protected FieldModel targetField;
    
    /**
     * 关联对象的EntityModel
     */
    protected EntityModel entityModel;
    /**
     * 返回关联关系
     * @return
     */
    public abstract RelationType getType();
    
    
    public FieldModel getTargetField(){
        return this.targetField;
    }


    public void setTargetField(FieldModel targetField) {
        this.targetField = targetField;
    }


    public EntityModel getEntityModel() {
        return entityModel;
    }


    public void setEntityModel(EntityModel entityModel) {
        this.entityModel = entityModel;
    }

    public String getTypeAsString(){
        return getType().name();
    }
    
}
