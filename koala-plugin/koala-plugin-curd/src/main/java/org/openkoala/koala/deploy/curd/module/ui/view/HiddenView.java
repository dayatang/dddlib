package org.openkoala.koala.deploy.curd.module.ui.view;

import org.openkoala.koala.deploy.curd.module.core.FieldModel;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：HiddenView.java
 *   
 * 功能描述：隐藏
 *  
 * 创建日期：2013-1-18下午4:45:28     
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
public class HiddenView extends UIWidget {
    
    public String toString(){
        return "隐藏控件";
    }
    
    public static HiddenView createDateViewFromFieldModel(FieldModel fieldModel){
        HiddenView hiddenView = new HiddenView();
        hiddenView.setName(fieldModel.getName());
        hiddenView.setExpress(fieldModel.getName());
        hiddenView.setType(fieldModel.getType());
        return hiddenView;
    }
    
    @Override
    public String getWidgetType() {
        return "hidden";
    }
}
