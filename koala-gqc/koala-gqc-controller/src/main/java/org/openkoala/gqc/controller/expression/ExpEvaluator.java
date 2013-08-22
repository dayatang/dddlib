package org.openkoala.gqc.controller.expression;

/**
 * 表达式求值
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 22, 2013 10:19:04 AM
 */
public interface ExpEvaluator {

	/**
	 * 计算表达式的值
	 * @param exp		表达式
	 * @return
	 */
	Object eval(String exp);
}
