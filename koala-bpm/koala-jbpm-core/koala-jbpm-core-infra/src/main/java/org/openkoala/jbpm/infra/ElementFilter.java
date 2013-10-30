package org.openkoala.jbpm.infra;

/**
 * XML元素过滤器
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Oct 17, 2013 3:11:01 PM
 */
public interface ElementFilter {

	/**
	 * 获取符合条件的元素名，返回正则表达式
	 * @return
	 */
	String getElementName();
	
}
