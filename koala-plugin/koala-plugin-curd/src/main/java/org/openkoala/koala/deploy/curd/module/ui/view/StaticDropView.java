package org.openkoala.koala.deploy.curd.module.ui.view;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：StaitcDropView.java
 *   
 * 功能描述：下拉框，固定定义下拉中的属性的下拉框
 *  
 * 创建日期：2013-1-18下午4:38:25     
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
public class StaticDropView extends UIWidget{
    
    /**
     * 以MAP的方式存储键-值对
     */
    private Map<String,String> valueMap = new HashMap<String, String>();
   
    public Map<String, String> getValueMap() {
        return valueMap;
    }

    public void setValueMap(Map<String, String> valueMap) {
        this.valueMap = valueMap;
    }
    
    public String getValuesAsJson(){
        StringBuffer sb = new StringBuffer("[");
        Set<String> keys = valueMap.keySet();
        for (String key : keys) {
            sb.append("{id: '").append(key).append("',text: '").append(valueMap.get(key)).append("'},");
        }
        if(sb.length()>1)sb.deleteCharAt(sb.length() - 1);
        return sb.append("]").toString();
    }

    public String toString(){
        return "下拉控件";
    }
    
    @Override
    public String getWidgetType() {
        return "staticDropdown";
    }
}
