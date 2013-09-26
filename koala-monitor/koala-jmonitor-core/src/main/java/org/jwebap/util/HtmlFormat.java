package org.jwebap.util;


/**
 * 字符串格式化处理
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  2008-2-4
 */
public class HtmlFormat {
	

	/**
	 * 把文本转义成正确格式的Html
	 * 比如:"<"转换成"&lt"等等
	 * @param html
	 * @return format后的文本
	 */
	public static String formatHtml(String html){
		String result=html;
		
		result=result.replaceAll("\"","&quot;");
		result=result.replaceAll("<","&lt;");
		result=result.replaceAll(">","&gt;");
		result=result.replaceAll(" ","&nbsp;");
		result=result.replaceAll("&","&amp;");
		result=result.replaceAll("\n","<br/>");
		return result;
	}
	
	
}
