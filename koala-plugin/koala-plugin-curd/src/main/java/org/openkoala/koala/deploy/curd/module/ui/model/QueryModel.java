package org.openkoala.koala.deploy.curd.module.ui.model;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.deploy.curd.module.ui.Model;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：QueryModel.java
 *   
 * 功能描述：查询视图定义
 *  
 * 创建日期：2013-1-18下午4:57:17     
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
public class QueryModel implements Model{
    
    
    private List<UIWidget> views;

    /**
     * 
     */
    public QueryModel() {
        super();
    }

    /**
     * @param views
     */
    public QueryModel(List<UIWidget> views) {
        super();
        this.views = views;
    }

    public List<UIWidget> getViews() {
        if (views != null && views.size() > 2) {
            List<UIWidget> dateWidgets = new ArrayList<UIWidget>();
            // 把日期空间放到第三列（即最后一列）显示
            for (int i = 0; i < views.size(); i++) {
                if ("id".equals(views.get(i).getName())) {
                    views.remove(i);
                    i--;
                    continue;
                }
                if ("date".equals(views.get(i).getWidgetType())) {
                    dateWidgets.add(views.remove(i));
                    i--;
                    continue;
                }
            }
            
            UIWidget temp;
            for (int i = 2; i < views.size(); i+=3) {
                if(dateWidgets.isEmpty())continue;
                temp = views.get(i);
                views.set(i, dateWidgets.remove(0));
                views.add(temp);
            }
            if(!dateWidgets.isEmpty())views.addAll(dateWidgets);
        }
        return views;
    }

    public void setViews(List<UIWidget> views) {
        this.views = views;
    } 
    
}
