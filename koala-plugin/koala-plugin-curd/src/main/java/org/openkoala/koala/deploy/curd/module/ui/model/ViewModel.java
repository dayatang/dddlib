package org.openkoala.koala.deploy.curd.module.ui.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openkoala.koala.deploy.curd.module.ui.Model;
import org.openkoala.koala.deploy.curd.module.ui.UIWidget;
import org.openkoala.koala.deploy.curd.module.ui.view.RelativeReadOnlyView;

/**
 * 
 * 类    名：ViewModel.java
 *   
 * 功能描述：详细视图
 *  
 * 创建日期：2013-1-18下午5:13:27     
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
public class ViewModel implements Model{
    
    
    private List<UIWidget> views;


    private List<TabViewUI> tabs;
    
    /**
     * @param views
     * @param relations
     */
    public ViewModel(List<UIWidget> views) {
        super();
        this.views = views;
    }

    public List<TabViewUI> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabViewUI> tabs) {
        this.tabs = tabs;
    }
    
    
    public List<UIWidget> getViews() {
        return views;
    }

    public void setViews(List<UIWidget> views) {
        this.views = views;
    }

    public Map<String,String> getRelativeEntity(){
        Map<String,String> relativeMap = new HashMap<String,String>();
        for (UIWidget view : views){
            if (view instanceof RelativeReadOnlyView){
                RelativeReadOnlyView relative = (RelativeReadOnlyView) view;
                if (!relativeMap.containsKey(relative.getRelativeType())){
                    relativeMap.put(relative.getRelativeType(),relative.getRelative());
                }
            }
        }
        return relativeMap;
    }
    
}
