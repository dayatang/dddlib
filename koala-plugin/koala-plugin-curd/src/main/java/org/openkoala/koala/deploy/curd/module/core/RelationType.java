package org.openkoala.koala.deploy.curd.module.core;

/**
 * 
 * 类    名：RelationType.java
 *   
 * 功能描述：实体与实体的有关系名
 *  
 * 创建日期：2013-1-18下午3:19:44     
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
public enum RelationType {
    /**
     * 一对一关联
     */
    OneToOne,
    /**
     * 一对多关联
     */
    OneToMany,
    /**
     * 多对一关联 
     */
    ManyToOne,
    
    /**
     * 多对多关联
     */
    ManyToMany
}
