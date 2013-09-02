package org.openkoala.koala.monitor.component.jdbc;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openkoala.koala.monitor.analyser.CommonAnalyser;
import org.openkoala.koala.monitor.analyser.SessionFilterAnalyser;
import org.openkoala.koala.monitor.component.AbstractComponent;
import org.openkoala.koala.monitor.core.ComponentContext;
import org.openkoala.koala.monitor.core.TraceLiftcycleManager;
import org.openkoala.koala.monitor.toolkit.asm.ClassEnhancer;

/**
 * Jdbc监控组件
 * 
 * 对于本地连接池，可以采取配置驱动的方式，动态监控，比如应用采用oracle,mysql
 * 两种数据库，驱动分别是orcale.jdbc.driver.OracleDriver和
 * com.mysql.jdbc.Driver，那么在DBComponent配置项中配置
 * driver-clazzs=orcale.jdbc.driver.OracleDriver;com.mysql.jdbc.Driver
 * 
 * 对于jndi远程数据源，可以配置应用中用于获取连接的类作为驱动，比如
 * com.ConnectionManager。DBComponent会在启动时对这些驱动类进行注入，使得所有
 * 该类方法返回的Connection具有监控的功能。
 * 
 * @see JdbcDriverInjectHandle
 * @author leadyu
 * @since Jwebap 0.5
 * @date 2007-8-14
 */
public class JdbcComponent extends AbstractComponent {
	private static final Log log = LogFactory.getLog(JdbcComponent.class);

	private ComponentContext componentContext = null;

	
    public static String TRACE_TYPE = "JDBC";
	
	public void startup(ComponentContext context) {
		componentContext = context;
		
		TraceLiftcycleManager container = componentContext.getContainer();
		container.registerAnalyser(TRACE_TYPE, new CommonAnalyser(TRACE_TYPE));
		container.registerAnalyser(TRACE_TYPE, new SessionFilterAnalyser(TRACE_TYPE));

		String clazzs = context.getProperty("driver-clazzs");
		String[] drivers = getDriverClassNames(clazzs);
		// inject trace
		injectJdbcDriver(drivers);
		
		log.info("jdbccomponent startup.");
	}

	private String[] getDriverClassNames(String clazzs) {
		if (clazzs == null || "".equals(clazzs)) {
			return new String[0];
		}
		clazzs = clazzs.replaceAll("\n\r\t\\s+", "");
		try {
			return clazzs.split(";");
		} catch (Exception e) {
			return new String[0];
		}
	}

	private void injectJdbcDriver(String[] drivers) {
		ClassEnhancer enhancer = new ClassEnhancer();
		for (int i = 0; i < drivers.length; i++) {

			String className = drivers[i];
			try {
				TraceLiftcycleManager container = componentContext.getContainer();

				// inject jdbcdriver to delegate connection from local pool or
				// remote pool.
				enhancer.createClass(className, new JdbcDriverInjectHandle(container), true);

			} catch (Exception e) {
				log.warn("对数据库驱动：" + className + "的监听部署失败，不过这并不影响系统的运行。错误原因：\n["
						+ e.getClass().getName() + "-->"+ e.getMessage() + "]");

			}

		}
	}
	

	public void destory() {
	}

	public void clear() {
	}


	public ComponentContext getComponentContext() {
		return componentContext;
	}
}
