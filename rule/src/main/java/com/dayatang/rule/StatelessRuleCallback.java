package com.dayatang.rule;

import java.util.List;

import javax.rules.StatelessRuleSession;

/**
 * 无状态规则服务回调接口。客户代码负责创建它的实现，交给StatelessRuleTemplate来执行。在doInRuleSession()方法中使用StatelessRuleSession执行业务特定的规则运算。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 *
 */
public interface StatelessRuleCallback {
	
	@SuppressWarnings("rawtypes")
	List doInRuleSession(StatelessRuleSession session) throws Exception;
}
