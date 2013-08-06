/**
 * @(#)KoalaUISurpportApplication.java
 * 
 * Copyright csair.All rights reserved.
 * This software is the XXX system. 
 *
 * @Version: 1.0
 * @JDK: jdk jdk1.6.0_10
 * @Module: examine-application
 */ 
 /*- 				History
 **********************************************
 *  ID      DATE           PERSON       REASON
 *  1     2013-1-23     Administrator    Created
 **********************************************
 */

/*
 * Copyright (c) OpenKoala 2011 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.ui.service;

import java.util.List;

import org.openkoala.ui.base.datamodel.BaseVo;
import org.openkoala.ui.base.datamodel.TreeNodeVo;

/**
 * 类    名：KoalaUISurpportApplication.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-23下午7:59:58     
 * 
 * 版本信息：v1.0
 * 
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved
 * 
 * 作    者：vakinge(chiang.www@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明	
 */

public interface KoalaUISurpportApplication {

    /**
     * 查询所有下拉框选择项
     * @param beanName
     * @param beanFields
     * @param parentId
     * @param filterParams 过滤条件
     * @return
     */
    List<BaseVo> queryAllOptions(String beanName,String beanFields,String parentId,String filterParams);
    
    /**
     * 查询指定父节点下面所有子节点
     * @param beanName
     * @param beanFields
     * @param parentId
     * @param filterParams 过滤条件
     * @param lazyLoadChildNode 
     * @return
     */
    TreeNodeVo queryTreeNodes(String beanName,String beanFields,String parentId,String filterParams,boolean lazyLoadChildNode);
}
