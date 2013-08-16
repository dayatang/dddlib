package org.openkoala.exception.surpport.struts2.tags;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.Head;
import org.apache.struts2.views.velocity.components.AbstractDirective;

import com.opensymphony.xwork2.util.ValueStack;

/**
 * @see Head
 */
public class HeadDirective extends AbstractDirective {
	protected Component getBean(ValueStack stack, HttpServletRequest req,
			HttpServletResponse res) {
		return new Head(stack, req, res);
	}

	public String getBeanName() {
		return "head";
	}

	public String getName() {
		return "sj" + getBeanName();
	}
}
