package org.openkoala.gqc.controller.expression;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

/**
 * 用于计算当前时间
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 22, 2013 12:24:42 PM
 */
public class CurrentTimeEvaluator implements ExpEvaluator {

	@Override
	public Object eval(String exp) {
		Map<String, Object> vars = new HashMap<String, Object>();
		vars.put("now", new Date());
		return MVEL.eval(exp, vars);
	}

}
