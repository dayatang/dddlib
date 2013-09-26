package org.openkoala.koala.deploy.curd.module.ui.view;

import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：DateView.java
 *   
 * 功能描述：日期类型
 *  
 * 创建日期：2013-1-18下午4:37:27     
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
public class DateView extends UIWidget {
    
    
    public String toString(){
        return "日期控件";
    }
    
    public static DateView createDateViewFromFieldModel(FieldModel fieldModel){
        DateView dateView = new DateView();
        dateView.setName(fieldModel.getName());
        dateView.setExpress(fieldModel.getName());
        dateView.setType(fieldModel.getType());
        return dateView;
    }

    /*
     *@see org.openkoala.koala.deploy.curd.module.ui.UIWidget#getWidgetType()
     */
    @Override
    public String getWidgetType() {
        return "date";
    }
}
