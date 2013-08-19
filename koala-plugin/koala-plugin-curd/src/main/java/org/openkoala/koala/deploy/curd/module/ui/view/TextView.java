package org.openkoala.koala.deploy.curd.module.ui.view;

import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：TextView.java
 *   
 * 功能描述：文本框	
 *  
 * 创建日期：2013-1-18下午4:36:46     
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
public class TextView extends UIWidget {

    public String toString(){
        return "文本控件";
    }
    
    public static TextView createDateViewFromFieldModel(FieldModel fieldModel){
        TextView view = new TextView();
        view.setName(fieldModel.getName());
        view.setExpress(fieldModel.getName());
        view.setType(fieldModel.getType());
        return view;
    }
    
    @Override
    public String getWidgetType() {
        return "text";
    }
}
