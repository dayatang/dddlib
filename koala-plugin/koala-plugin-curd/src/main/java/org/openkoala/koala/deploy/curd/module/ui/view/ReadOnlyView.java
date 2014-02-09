package org.openkoala.koala.deploy.curd.module.ui.view;

import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：ReadOnlyView.java
 *   
 * 功能描述：显示为只读	
 *  
 * 创建日期：2013-1-18下午4:48:16     
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
public class ReadOnlyView extends UIWidget{
    
    
    public String getViewType(){
        return "ReadOnlyView";
    }
    
    public String toString(){
        return "只读控件";
    }
    
    public static ReadOnlyView createDateViewFromFieldModel(FieldModel fieldModel){
        ReadOnlyView readOnlyView = new ReadOnlyView();
        readOnlyView.setName(fieldModel.getName());
        readOnlyView.setExpress(fieldModel.getName());
        readOnlyView.setType(fieldModel.getType());
        return readOnlyView;
    }
    
    @Override
    public String getWidgetType() {
        return "readOnly";
    }
}

