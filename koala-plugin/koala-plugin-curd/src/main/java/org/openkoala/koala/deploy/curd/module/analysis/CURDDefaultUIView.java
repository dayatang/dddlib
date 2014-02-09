package org.openkoala.koala.deploy.curd.module.analysis;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openkoala.koala.deploy.curd.module.core.EntityModel;
import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.PKFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.PersistenceFieldModel;
import org.openkoala.koala.deploy.curd.module.core.model.RelationFieldModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;
import org.openkoala.koala.deploy.curd.module.ui.model.AddModel;
import org.openkoala.koala.deploy.curd.module.ui.model.EntityViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.ListModel;
import org.openkoala.koala.deploy.curd.module.ui.model.QueryModel;
import org.openkoala.koala.deploy.curd.module.ui.model.TabModel;
import org.openkoala.koala.deploy.curd.module.ui.model.TabViewUI;
import org.openkoala.koala.deploy.curd.module.ui.model.UpdateModel;
import org.openkoala.koala.deploy.curd.module.ui.model.ViewModel;
import org.openkoala.koala.deploy.curd.module.ui.view.DateView;
import org.openkoala.koala.deploy.curd.module.ui.view.ReadOnlyView;
import org.openkoala.koala.deploy.curd.module.ui.view.TextView;

/**
 * 
 * 类    名：CURDDefaultUIView.java
 *   
 * 功能描述：根据分析出来的领域模型建模，生成默认的VIEW UI	
 *  
 * 创建日期：2013-1-23上午10:43:26     
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
public class CURDDefaultUIView {
    
    /**
     * 传入一个领域模型建模对象，生成这个领域模型建模对象的默认UI
     * @return
     */
    public static EntityViewUI getDefaultEntityViewUI(EntityModel entity){
        EntityViewUI ui = new EntityViewUI(entity);
        
        //List<PKFieldModel> idFieldModels = entity.getIDFieldModel();
        List<PersistenceFieldModel> persistenceFieldModels=entity.getPersistenceFieldModel();
        
        //生成查询UI，查询UI仅针对本身实体，不针对级联实体进行查询
          //默认的查询显示所有字段的查询
        List<UIWidget> queryViews = new ArrayList<UIWidget>();
        
//        for(FieldModel idFieldModel:idFieldModels){
//            UIWidget view = getDefaultActionView(idFieldModel);
//            queryViews.add(view);
//        }
        
        for(FieldModel persistenceFieldModel:persistenceFieldModels){
            UIWidget view = getDefaultActionView(persistenceFieldModel);
            queryViews.add(view);
        }
        
        QueryModel queryModel = new QueryModel(queryViews);
        ui.setQueryModel(queryModel);
        
        //生成表格的分页UI，默认显示当前实体的所有字段
        List<UIWidget> displayViews = new ArrayList<UIWidget>();
//        for(FieldModel idFieldModel:idFieldModels){
//            UIWidget view = getDefaultDisplayView(idFieldModel);
//            displayViews.add(view);
//        }
        
        for(FieldModel persistenceFieldModel:persistenceFieldModels){
            UIWidget view = getDefaultDisplayView(persistenceFieldModel);
            displayViews.add(view);
        }
        
        ListModel listModel = new ListModel(displayViews);
        ui.setListModel(listModel);
        
        //生成新增的UI，默认显示除主键以外的所有字段
        List<UIWidget> addViews = new ArrayList<UIWidget>();
        for(FieldModel persistenceFieldModel:persistenceFieldModels){
            UIWidget view = getDefaultActionView(persistenceFieldModel);
            addViews.add(view);
        }
        AddModel addModel = new AddModel(addViews);
        ui.setAddModel(addModel);
        
        //生成修改的UI，默认显示所有字段，其中ID为隐藏 
        List<UIWidget> updatesViews = new ArrayList<UIWidget>();
        for(FieldModel persistenceFieldModel:persistenceFieldModels){
            UIWidget view = getDefaultActionView(persistenceFieldModel);
            updatesViews.add(view);
        }
        UpdateModel updateModel = new UpdateModel(updatesViews);
        ui.setUpdateModel(updateModel);
        
        //生成查看的UI，默认显示所有字段
        List<UIWidget> detailViews = new ArrayList<UIWidget>();
//        for(FieldModel idFieldModel:idFieldModels){
//            UIWidget view = getDefaultDisplayView(idFieldModel);
//            detailViews.add(view);
//        }
        
        for(FieldModel persistenceFieldModel:persistenceFieldModels){
            UIWidget view = getDefaultDisplayView(persistenceFieldModel);
            detailViews.add(view);
        }
        
        List<RelationFieldModel> relationFieldModels = entity.getRelationFieldModel();
        List<TabViewUI> tabs = new ArrayList<TabViewUI>();
        for(RelationFieldModel relation:relationFieldModels){
            TabViewUI tabViewUi = new TabViewUI(entity,relation, getDefaultView(relation.getRelationModel().getEntityModel()));
            tabs.add(tabViewUi);
          //relations.put(relation.getRelationModel().getEntityModel(), getDefaultView(relation.getRelationModel().getEntityModel()));
        }
        ViewModel viewModel = new ViewModel(detailViews);
        viewModel.setTabs(tabs);
        ui.setViewModel(viewModel);
        
        return ui;
    }
    
    
  
    private static TabModel getDefaultView(EntityModel entity){
//        List<PKFieldModel> idFieldModels = entity.getIDFieldModel();
        List<PersistenceFieldModel> persistenceFieldModels=entity.getPersistenceFieldModel();
        
        List<UIWidget> displayViews = new ArrayList<UIWidget>();
//        for(FieldModel idFieldModel:idFieldModels){
//            UIWidget view = getDefaultDisplayView(idFieldModel);
//            displayViews.add(view);
//        }
        
        for(FieldModel persistenceFieldModel:persistenceFieldModels){
            UIWidget view = getDefaultDisplayView(persistenceFieldModel);
            displayViews.add(view);
        }
        
        return new TabModel(displayViews);
    }
    
    /**
     * 获取一个属性的默认field
     * @param fieldModel
     * @return
     */
    private static UIWidget getDefaultDisplayView(FieldModel fieldModel){
        ReadOnlyView readOnlyView = ReadOnlyView.createDateViewFromFieldModel(fieldModel);
        return readOnlyView;
    }
    
    /**
     * 获取一个FieldModel的默认编辑形式
     * 日期类型是 DateTime
     * 其它类型是text
     * @param fieldModel
     * @return
     */
    private static UIWidget getDefaultActionView(FieldModel fieldModel){
        String type = fieldModel.getType();
        //日期类型默认使用日期控件
        if(Date.class.getName().equals(type) || Timestamp.class.getName().equals(type)){
            DateView dateView = DateView.createDateViewFromFieldModel(fieldModel);
            return dateView;
        }
        //其它类型，默认使用textView
        else{
            TextView textView = TextView.createDateViewFromFieldModel(fieldModel);
            return textView;
        }
    }
}
