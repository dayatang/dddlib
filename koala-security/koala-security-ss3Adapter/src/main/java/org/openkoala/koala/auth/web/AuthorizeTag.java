package org.openkoala.koala.auth.web;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 权限标签
 * 
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Sep 11, 2013 2:59:57 PM
 */
public class AuthorizeTag extends TagSupport {

	private static final long serialVersionUID = 1031592996312070012L;

	/**
	 * 如果所有资源都授权，则显示标签体的内容。资源标识用逗号分隔
	 */
	private String ifAllGranted;

	/**
	 * 如果所有资源中有一个资源被授权，则显示标签体的内容。资源标识用逗号分隔
	 */
	private String ifAnyGranted;

	/**
	 * 如果所有资源中都没有被授权，则显示标签体的内容。资源标识用逗号分隔
	 */
	private String ifNotGranted;
	
	private PermissionController permissionController;
	
	@Override
	public int doStartTag() throws JspException {
        if (ifAllGranted == null && ifAnyGranted == null && ifNotGranted == null) {
        	return Tag.SKIP_BODY;
        }
        
        if (permissionController == null) {
        	permissionController = new PermissionController();
        }
        
        if (ifAllGranted != null) {
        	return ifAllGranted();
        }
        
        if (ifAnyGranted != null) {
	        return ifAnyGranted();
        }
        
        if (ifNotGranted != null) {
	        return ifNotGranted();
        }
        
		return Tag.EVAL_BODY_INCLUDE;
	}

	/**
	 * 如果资源没有授权，则显示标签体的内容
	 * @return
	 */
	private int ifNotGranted() {
		for (String identify : ifNotGranted.split(",")) {
			if (permissionController.hasPrivilege(identify)) {
				return Tag.SKIP_BODY;
			}
		}
		return Tag.EVAL_BODY_INCLUDE;
	}

	/**
	 * 如果有一个资源被授权，则显示标签体的内容
	 * @return
	 */
	private int ifAnyGranted() {
		for (String identify : ifAnyGranted.split(",")) {
			if (permissionController.hasPrivilege(identify)) {
				return Tag.EVAL_BODY_INCLUDE;
			}
		}
		return Tag.SKIP_BODY;
	}

	/**
	 * 如果全部资源都被授权，则显示标签体的内容
	 * @return
	 */
	private int ifAllGranted() {
        for (String identify : ifAllGranted.split(",")) {
        	if (!permissionController.hasPrivilege(identify)) {
        		return Tag.SKIP_BODY;
        	}
        }
        return Tag.EVAL_BODY_INCLUDE;
	}

	public void setIfAllGranted(String ifAllGranted) {
		this.ifAllGranted = ifAllGranted;
	}

	public String getIfAllGranted() {
		return ifAllGranted;
	}

	public String getIfAnyGranted() {
		return ifAnyGranted;
	}

	public void setIfAnyGranted(String ifAnyGranted) {
		this.ifAnyGranted = ifAnyGranted;
	}

	public String getIfNotGranted() {
		return ifNotGranted;
	}

	public void setIfNotGranted(String ifNotGranted) {
		this.ifNotGranted = ifNotGranted;
	}

}
