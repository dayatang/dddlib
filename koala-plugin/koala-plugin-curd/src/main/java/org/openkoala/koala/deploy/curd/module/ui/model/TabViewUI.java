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
package org.openkoala.koala.deploy.curd.module.ui.model;

import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;

/**
 * 类    名：TabViewUI.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-24上午9:42:55     
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
public class TabViewUI {
    
    /**
     * 当前TAB页实体的关联实体
     */
    private EntityModel parent;
    /**
     * 关联对象模型
     */
    private RelationFieldModel relationFieldModel;
    
    
    private TabModel listModel;


    /**
     * @param parent
     * @param relationFieldModel
     * @param listModel
     */
    public TabViewUI(EntityModel parent, RelationFieldModel relationFieldModel, TabModel listModel) {
        super();
        this.parent = parent;
        this.relationFieldModel = relationFieldModel;
        this.listModel = listModel;
    }


    public EntityModel getParent() {
        return parent;
    }


    public void setParent(EntityModel parent) {
        this.parent = parent;
    }


    public RelationFieldModel getRelationFieldModel() {
        return relationFieldModel;
    }


    public void setRelationFieldModel(RelationFieldModel relationFieldModel) {
        this.relationFieldModel = relationFieldModel;
    }


    public TabModel getListModel() {
        return listModel;
    }


    public void setListModel(TabModel listModel) {
        this.listModel = listModel;
    }
    
}
