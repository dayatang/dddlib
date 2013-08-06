package org.openkoala.ui.base.datamodel;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 类    名：TreeNodeVo.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-23下午7:46:53     
 * 
 * 版本信息：v1.0
 * 
 * 版权信息：Copyright (c) 2011 openkoala All Rights Reserved
 * 
 * 作    者：vakinge(chiang.www@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class TreeNodeVo extends BaseVo{
    private static final long serialVersionUID = 2392268664824179661L;
    private String topParentIDValue;
    private String parentID;
	private String url; // URL链接
	private String icon;//图标
	private boolean disabled = false; // 是否为不可选择
	private boolean ischecked = false;
	
	private String remark;//
	
	private List<TreeNodeVo> children; // 子节点
	
    public TreeNodeVo(String id, String text, String url) {
        super();
        this.id = id;
        this.text = text;
        this.url = url;
    }

    public TreeNodeVo(String id, String text, String url, String icon) {
        this(id, text, url);
        this.icon = icon;
    }

    /**
     * 
     */
    public TreeNodeVo() {}

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }

    public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}


    public List<TreeNodeVo> getChildren() {
        if(children == null)children = new ArrayList<TreeNodeVo>();
        return children;
    }

    public void setChildren(List<TreeNodeVo> children) {
        this.children = children;
    }


    public String getTopParentIDValue() {
        return topParentIDValue;
    }

    public void setTopParentIDValue(String topParentIDValue) {
        this.topParentIDValue = topParentIDValue;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getIschecked() {
        if(isParent()){
            if(children == null || children.isEmpty())return false;
            boolean ck = true;
            for (TreeNodeVo c : children) {
                ck = ck && c.ischecked;
            }
            return ck;
        }else{
            return ischecked;
        }
    }
    
    public void setIschecked(boolean ischecked) {
        this.ischecked = ischecked;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public boolean hasChild(){
        return getChildren().size()>0;
    }
    
    public boolean isParent(){
        return url == null || "".equals(url.trim());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((text == null) ? 0 : text.hashCode());
        result = prime * result + ((url == null) ? 0 : url.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TreeNodeVo other = (TreeNodeVo) obj;
        if (text == null) {
            if (other.text != null)
                return false;
        } else if (!text.equals(other.text))
            return false;
        if (url == null) {
            if (other.url != null)
                return false;
        } else if (!url.equals(other.url))
            return false;
        return true;
    }
    
    
    
}
