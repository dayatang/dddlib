package org.openkoala.koala.deploy.curd.module.ui.view;

import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：TextAreaView.java
 *   
 * 功能描述：文本域
 *  
 * 创建日期：2013-1-18下午4:58:17     
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
public class TextAreaView extends UIWidget {
    
    public String toString(){
        return "文本域控件";
    }
    
    @Override
    public String getWidgetType() {
        return "textArea";
    }
}
