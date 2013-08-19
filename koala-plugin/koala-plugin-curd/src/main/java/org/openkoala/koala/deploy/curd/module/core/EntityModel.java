package org.openkoala.koala.deploy.curd.module.core;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.deploy.curd.module.core.model.ObjectValueFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.PKFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.PersistenceFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;

/**
 * 
 * 类    名：EntityModel.java
 *   
 * 功能描述：领域对象建模实体
 *  
 * 创建日期：2013-1-18下午3:10:27
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2012 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class EntityModel {
    
    /**
     * Entity的名称
     */
    private String name;
    
    /**
     * entity的java全路径名
     */
    private String className;
    
    /**
     * 领域对象中的所有属性
     */
    private List<FieldModel> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<FieldModel> getFields() {
        if(fields==null)fields = new ArrayList<FieldModel>();
        return fields;
    }

    public void setFields(List<FieldModel> fields) {
        this.fields = fields;
    }
    
    
    /**
     * 获取主键
     * @return
     */
    public List<PKFieldModel> getIDFieldModel(){
        List<PKFieldModel> models = new ArrayList<PKFieldModel>();
        for(FieldModel fieldModel:this.getFields()){
            if(fieldModel instanceof PKFieldModel){
                models.add((PKFieldModel)fieldModel);
            }
        }
        return models;
    }
    
    /**
     * 获取数据库字段Field
     * @return
     */
    public List<PersistenceFieldModel> getPersistenceFieldModel(){
        List<PersistenceFieldModel> models = new ArrayList<PersistenceFieldModel>();
        for(FieldModel fieldModel:this.getFields()){
            if(fieldModel instanceof PersistenceFieldModel){
                models.add((PersistenceFieldModel)fieldModel);
            }
        }
        return models;
    }
    
    /**
     * 获取值对象Field
     * @return
     */
    public List<ObjectValueFieldModel> getObjectValueFieldModel(){
        List<ObjectValueFieldModel> models = new ArrayList<ObjectValueFieldModel>();
        for(FieldModel fieldModel:this.getFields()){
            if(fieldModel instanceof ObjectValueFieldModel){
                models.add((ObjectValueFieldModel)fieldModel);
            }
        }
        return models;
    }
    
    /**
     * 获取关联FieldModel
     * @return
     */
    public List<RelationFieldModel> getRelationFieldModel(){
        List<RelationFieldModel> models = new ArrayList<RelationFieldModel>();
        for(FieldModel fieldModel:this.getFields()){
            if(fieldModel instanceof RelationFieldModel){
                models.add((RelationFieldModel)fieldModel);
            }
        }
        return models;
    }
    
    /**
     * //获取最后一级包名
     * @return
     */
    public String getLastPackageName(){
        String[] temparr = getClassName().split("\\.");
        return temparr[temparr.length - 2];
    }
}
