package org.openkoala.koala.deploy.curd.module.core;
/**
 * 
 * 类    名：FieldModel.java
 *   
 * 功能描述：领域对象中的属性	
 *  
 * 创建日期：2013-1-18下午3:11:15     
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
public abstract class FieldModel {
    
    /**
     * 属性的名称
     */
    private String name;
    
    /**
     * 属性的类型
     */
    private String type;
    

    public FieldModel(String name, String type) {
        super();
        this.name = name;
        this.type = type;
    }

    public FieldModel() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    
}
