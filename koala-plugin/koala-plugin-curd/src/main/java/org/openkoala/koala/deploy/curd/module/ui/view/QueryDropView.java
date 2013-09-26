package org.openkoala.koala.deploy.curd.module.ui.view;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.deploy.curd.module.ui.UIWidget;

/**
 * 
 * 类    名：QueryDropView.java
 *   
 * 功能描述：动态查询下拉框	
 *  
 * 创建日期：2013-1-18下午4:39:48     
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
public class QueryDropView extends UIWidget {
    
    private String dropdownStyle = "list";//下拉框样式：list,tree
    private String className;//对应实体类 如：org.openkoala.domain.City
    private String optKeyField;//下拉框值对应实体的字段名
    private String optTextField;//下拉框显示值对应实体的字段名
    private String parentIdField;//父级对应实体的字段名（主要用户层级结构）
    private String parentId;//父ID值
    private String filterParams;//过滤条件
    private List<TextView> cascadeViewList;//级联下拉框对应域
    
    
    
    public String getDropdownStyle() {
        return dropdownStyle;
    }


    public void setDropdownStyle(String dropdownStyle) {
        this.dropdownStyle = dropdownStyle;
    }


    public String getClassName() {
        return className;
    }


    public void setClassName(String className) {
        this.className = className;
    }


    public String getOptKeyField() {
        return optKeyField;
    }


    public void setOptKeyField(String optKeyField) {
        this.optKeyField = optKeyField;
    }


    public String getOptTextField() {
        return optTextField;
    }


    public void setOptTextField(String optTextField) {
        this.optTextField = optTextField;
    }


    public String getParentIdField() {
        return parentIdField;
    }


    public void setParentIdField(String parentIdField) {
        this.parentIdField = parentIdField;
    }


    public String getParentId() {
        return parentId;
    }


    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
    
    public String getFilterParams() {
        return filterParams;
    }

    public void setFilterParams(String filterParams) {
        this.filterParams = filterParams;
    }


    public List<TextView> getCascadeViewList() {
        if(cascadeViewList == null)cascadeViewList = new ArrayList<TextView>();
        return cascadeViewList;
    }

    public void setCascadeViewList(List<TextView> cascadeViewList) {
        this.cascadeViewList = cascadeViewList;
    }
    
    public String getCascadeNames(){
        StringBuffer sb = new StringBuffer();
        if(cascadeViewList.size()>0){
            for (TextView view : cascadeViewList) {
                sb.append(view.getExpress()).append("ID,");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 需要获取值的字段
     * @return
     */
    public String getBeanFields() {
        StringBuffer sb = new StringBuffer();
        sb.append(optKeyField).append(",").append(optTextField);
        if(parentIdField != null)sb.append(",").append(parentIdField);
        return sb.toString();
    }
    

    public String toString(){
        return "下拉查询控件";
    }


    /*
     *@see org.openkoala.koala.deploy.curd.module.ui.UIWidget#getWidgetType()
     */
    @Override
    public String getWidgetType() {
        return "queryDropdown";
    }
}
