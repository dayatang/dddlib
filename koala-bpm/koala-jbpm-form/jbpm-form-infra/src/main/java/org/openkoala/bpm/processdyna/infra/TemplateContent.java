package org.openkoala.bpm.processdyna.infra;

/**
 * 模板渲染接口
 * @author lingen
 *
 */
public interface TemplateContent {
	
	/**
	 * 传入参数以及模板，将模板渲染出其代表的 String内容
	 * @param params
	 * @param template
	 * @return
	 */
	public String process(Object params,String template);

}
