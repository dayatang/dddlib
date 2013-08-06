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

/**
 * 类    名：EntityViewUI.java
 *   
 * 功能描述：一个ENTITY对应的
 *  
 * 创建日期：2013-1-23上午10:57:52     
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
public class EntityViewUI {
    
    
  
    public EntityViewUI() {
        super();
    }


    public EntityViewUI(EntityModel entityModel) {
        super();
        this.entityModel = entityModel;
    }

    /**
     * 关联的领域实体模型
     */
    private EntityModel entityModel;
    
    /**
     * 当前实体的查询模型
     */
    private QueryModel queryModel;
    
    /**
     * List模型
     */
    private ListModel listModel;
    
    /**
     * VIEW模型
     */
    private ViewModel viewModel;
    
    /**
     * 新增模型
     */
    private AddModel addModel;
    
    /**
     * 更新模型
     */
    private UpdateModel updateModel;

    public QueryModel getQueryModel() {
        return queryModel;
    }

    public void setQueryModel(QueryModel queryModel) {
        this.queryModel = queryModel;
    }

    public ListModel getListModel() {
        return listModel;
    }

    public void setListModel(ListModel listModel) {
        this.listModel = listModel;
    }

    public ViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(ViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public AddModel getAddModel() {
        return addModel;
    }

    public void setAddModel(AddModel addModel) {
        this.addModel = addModel;
    }

    public UpdateModel getUpdateModel() {
        return updateModel;
    }

    public void setUpdateModel(UpdateModel updateModel) {
        this.updateModel = updateModel;
    }


    public EntityModel getEntityModel() {
        return entityModel;
    }


    public void setEntityModel(EntityModel entityModel) {
        this.entityModel = entityModel;
    }
    
}
