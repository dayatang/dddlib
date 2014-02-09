package org.openkoala.framework.i18n.velocity; 

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.openkoala.framework.i18n.I18NManager;

/**
 * I18N Velocity指令
 * @author Ken
 * @since 2013-01-14
 *
 */
public class I18NDirective extends Directive {
	
	private static VelocityEngine engine;
	
	static {
		engine = new VelocityEngine();
	}
	
	/**
	 * 指定指令的名称
	 */
	@Override
	public String getName() {
		return "I18N";
	}

	@Override
	public int getType() {
		return LINE;
	}

	/**
	 * 指令渲染
	 */
	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException,
			ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		// 获取指令的第一个参数的值 
		SimpleNode keyNode = (SimpleNode) node.jjtGetChild(0);
		if (keyNode == null) {
			throw new RuntimeException("First argument must be not null");
		}
		String key = (String) keyNode.value(context);
		// 获取指令的第二个参数
		int size = node.jjtGetNumChildren();
		SimpleNode localeNode = null;
		if (size == 2) {
			localeNode = (SimpleNode) node.jjtGetChild(1);
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		String message = null;
		if (localeNode == null) {
			message = I18NManager.getMessage(key);
		} else {
			String locale = (String) localeNode.value(context);
			message = I18NManager.getMessage(key, locale);
		}
		map.put("data", message);
		writer.write(renderTemplate(map, message));
		return true;
	}
	
	/**
	 * 渲染模板
	 * @param params
	 * @return
	 */
	private String renderTemplate(Map<String, Object> params, String val) {
		VelocityContext context = new VelocityContext(params);
		StringWriter writer = new StringWriter();
		engine.evaluate(context, writer, "", val);
		return writer.toString();
	}

}
