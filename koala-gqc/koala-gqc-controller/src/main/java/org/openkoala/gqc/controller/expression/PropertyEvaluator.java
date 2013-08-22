package org.openkoala.gqc.controller.expression;

import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

/**
 * 属性表达式求值
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 22, 2013 12:20:33 PM
 */
public class PropertyEvaluator implements ExpEvaluator {
	
	private Map<String, Object> vars = new HashMap<String, Object>();

	public PropertyEvaluator(Map<String, Object> vars) {
		this.vars = vars;
	}

	@Override
	public Object eval(String exp) {
		return MVEL.eval(exp, vars);
	}

}
