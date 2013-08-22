package org.openkoala.gqc.controller.expression;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

/**
 * 根据表达式【#{username}】获取当前登录用户账号
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 22, 2013 12:07:40 PM
 */
public class UserNameEvaluator implements ExpEvaluator {
	
	@Override
	public Object eval(String exp) {
		Map<String, Object> vars = new HashMap<String, Object>();
		try {
			Class<?> clazz = Class.forName("org.openkoala.koala.auth.ss3adapter.AuthUserUtil");
			Method method = clazz.getMethod("getLoginUserName");
			vars.put("username", method.invoke(null));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return MVEL.eval(exp, vars);
	}

}
