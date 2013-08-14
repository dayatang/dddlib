package com.dayatang.rule;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 有状态规则服务模板类。负责创建StatefulRuleSession,执行规则和关闭StatefulRuleSession。
 * 建立这个类的目的，一是消除了客户代码自行创建StatefulRuleSession的必要性，二是为了防止客户代码执行规则之后忘记释放StatefulRuleSession。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>) <a href="mailto:gdyangyu@gmail.com">杨宇</a>
 *
 */
@SuppressWarnings("rawtypes")
public class StatefulRuleTemplate {
	
	private static Logger LOGGER = LoggerFactory.getLogger(StatefulRuleTemplate.class);

	private Map sessionProperties = new HashMap();
	
	private RuleAdministrator ruleAdministrator;
	private LocalRuleExecutionSetProvider ruleExecutionSetProvider;
	private RuleRuntime ruleRuntime;

	private RuleExecutionSet ruleExecutionSet;

	public final StatefulRuleTemplate sessionProperties(Map sessionProperties) {
		this.sessionProperties = sessionProperties;
		return this;
	}
	
	/**
	 * 从指定的来源读取规则集
	 * @param ruleSource 规则定义源
	 * @param executionSetProperties 规则的属性Map(如：source=drl/xml dsl=java.io.Reader)
	 * @return
	 */
	public final StatefulRuleTemplate ruleSource(Object ruleSource, Map executionSetProperties) {
		try {
			if (ruleSource instanceof String) {
				this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(new StringReader((String) ruleSource), executionSetProperties);
			} else if (ruleSource instanceof Reader) {
				this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet((Reader) ruleSource, executionSetProperties);
			} else if (ruleSource instanceof InputStream) {
				this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet((InputStream) ruleSource, executionSetProperties);
			} else {
				this.ruleExecutionSet = ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);
			}
		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new UnSupportedRuleFormatException(e);
		}
		return this;
	}

	/**
	 * 从指定的来源读取规则集
	 * @param ruleSource 规则定义源
	 * @return
	 */
	public final StatefulRuleTemplate ruleSource(Object ruleSource) {
		return ruleSource(ruleSource, null);
	}
	
	
	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 */
	public StatefulRuleTemplate(RuleServiceProvider ruleServiceProvider) {
		this(ruleServiceProvider, null);
	}
	
	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 * @param serviceProviderProperties 具体规则服务提供者所需要的额外属性
	 */
	public StatefulRuleTemplate(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(serviceProviderProperties);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
	}
	
	/**
	 * 构造函数
	 * @param ruleServiceProvider 规则服务提供者实现类，如Drools等。
	 * @param serviceProviderProperties 具体规则服务提供者所需要的额外属性
	 * @param ruleSource 规则源，包含规则定义的内容。可能是字符串，Reader, InputStream或其他服务提供者支持的类型。
	 * @param executionSetProperties 规则的属性Map(如：source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 */
	@Deprecated
	public StatefulRuleTemplate(RuleServiceProvider ruleServiceProvider, Map serviceProviderProperties, 
			Object ruleSource, Map executionSetProperties, Map sessionProperties) {
		try {
			ruleAdministrator = ruleServiceProvider.getRuleAdministrator();
			ruleExecutionSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(serviceProviderProperties);
			ruleRuntime = ruleServiceProvider.getRuleRuntime();
			this.ruleExecutionSet = createRuleExecutionSet(ruleSource, executionSetProperties);
			LOGGER.info("The rule service provider of JSR94 is " + ruleServiceProvider.getClass());
		} catch (Exception e) {
			throw new RuleRuntimeException(e);
		}
		this.sessionProperties = sessionProperties;
	}

	private RuleExecutionSet createRuleExecutionSet(Object ruleSource, Map executionSetProperties) {
		try {
			if (ruleSource instanceof String) {
				Reader reader = new StringReader((String) ruleSource);
				return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof Reader) {
				Reader reader = (Reader) ruleSource;
				return ruleExecutionSetProvider.createRuleExecutionSet(reader, executionSetProperties);
			}
			if (ruleSource instanceof InputStream) {
				InputStream in = (InputStream) ruleSource;
				return ruleExecutionSetProvider.createRuleExecutionSet(in, executionSetProperties);
			}
			return ruleExecutionSetProvider.createRuleExecutionSet(ruleSource, executionSetProperties);

		} catch (RuleExecutionSetCreateException e) {
			throw new UnSupportedRuleFormatException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 提供StatefulRuleSession给StatefulRuleCallback执行
	 * @param callback
	 * @throws Exception
	 */
	public void execute(StatefulRuleCallback callback) throws Exception {
		StatefulRuleSession session = getStatefulRuleSession();
		try {
			callback.doInRuleSession(session);
		} finally {
			releaseStatefulRuleSession(session);
		}
	}

	private void releaseStatefulRuleSession(StatefulRuleSession session) {
		try {
			session.release();
		} catch (Exception e) {
			throw new RuleRuntimeException("Cannot release rule session!!", e);
		}
	}
	
	/**
	 * 获取StatefulRuleSession。客户代码要记得在使用StatefulRuleSession之后通过调用其release()方法释放资源。
	 * @return
	 */
	public StatefulRuleSession getStatefulRuleSession() {
		try{
			String ruleExecutionSetUri = ruleExecutionSet.getName();
			ruleAdministrator.registerRuleExecutionSet(ruleExecutionSetUri, ruleExecutionSet, null);
			return (StatefulRuleSession) ruleRuntime.createRuleSession(ruleExecutionSetUri, sessionProperties, RuleRuntime.STATEFUL_SESSION_TYPE);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuleRuntimeException("Cannot create Rule Session!!!", e);
		}
	}
}
