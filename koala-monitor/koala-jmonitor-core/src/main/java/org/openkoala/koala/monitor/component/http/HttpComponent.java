package org.openkoala.koala.monitor.component.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openkoala.koala.monitor.analyser.CommonAnalyser;
import org.openkoala.koala.monitor.analyser.SessionFilterAnalyser;
import org.openkoala.koala.monitor.component.AbstractComponent;
import org.openkoala.koala.monitor.core.ComponentContext;
import org.openkoala.koala.monitor.core.TraceLiftcycleManager;

/**
 * http请求监控组件
 * 
 * @author leadyu
 * @since Jwebap 0.5
 * @date 2007-8-14
 */
public class HttpComponent extends AbstractComponent {
	private static final Log log = LogFactory.getLog(HttpComponent.class);

	private ComponentContext componentContext = null;
	public static String TRACE_TYPE = "HTTP";

	public void startup(ComponentContext context) {
		componentContext = context;
		TraceLiftcycleManager container = componentContext.getContainer();
		container.registerAnalyser(TRACE_TYPE, new CommonAnalyser(TRACE_TYPE));
		container.registerAnalyser(TRACE_TYPE, new SessionFilterAnalyser(TRACE_TYPE));
		log.info("httpcomponent startup.");
	}

	public void destory() {
	}

	public void clear() {}

	public ComponentContext getComponentContext() {
		return componentContext;
	}
}
