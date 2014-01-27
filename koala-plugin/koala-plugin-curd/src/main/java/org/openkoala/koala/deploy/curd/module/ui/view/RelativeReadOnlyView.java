package org.openkoala.koala.deploy.curd.module.ui.view;

import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：RelativeReadOnlyView.java
 *   
 * 功能描述：关联对象显示的view
 *  
 * 创建日期：2013-1-18下午4:50:24     
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
public class RelativeReadOnlyView extends UIWidget {
    
    private String relative;
    
    private String relativeType;
    public String toString(){
        return "只读关联控件";
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }
    
    @Override
    public String getWidgetType() {
        return "relativeReadOnly";
    }
    
    public String getViewType(){
        return this.getClass().getSimpleName();
    } 

    public String getRelativeType() {
        return relativeType;
    }

    public void setRelativeType(String relativeType) {
        this.relativeType = relativeType;
    }

    /**
     * @param persistenceFieldModel
     * @param name
     * @return
     */
    public static RelativeReadOnlyView createDateViewFromFieldModel(FieldModel fieldModel, String name,String relativeType) {
        RelativeReadOnlyView readOnlyView = new RelativeReadOnlyView();
        readOnlyView.setName(fieldModel.getName());
        readOnlyView.setExpress(fieldModel.getName());
        readOnlyView.setType(fieldModel.getType());
        readOnlyView.setRelative(name);
        readOnlyView.setRelativeType(relativeType);
        return readOnlyView;
    }
    
}
