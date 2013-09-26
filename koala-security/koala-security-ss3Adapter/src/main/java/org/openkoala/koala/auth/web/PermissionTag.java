package org.openkoala.koala.auth.web; 

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * 权限标签
 * @author Ken
 * @since 2013-02-25 14:41
 *
 */
@Deprecated
public class PermissionTag extends TagSupport {
	
	private static final long serialVersionUID = -4667702276984898787L;
	
	// 资源标识
	private String identify;

	private PermissionController permissionController;

	@Override
	public int doStartTag() throws JspException {
		if (permissionController == null) {
			permissionController = new PermissionController();
		}
		// 如果有权限则执行标签体内的内容
		if (permissionController.hasPrivilege(identify)) {
			return EVAL_BODY_INCLUDE;
		}
		return SKIP_BODY;
	}

	public String getIdentify() {
		return identify;
	}

	public void setIdentify(String identify) {
		this.identify = identify;
	}

}
