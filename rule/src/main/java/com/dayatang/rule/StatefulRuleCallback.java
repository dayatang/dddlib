package com.dayatang.rule;

import javax.rules.StatefulRuleSession;

/**
 * 有状态规则服务回调接口。客户代码负责创建它的实现，交给StatefulRuleTemplate来执行。在doInRuleSession()方法中使用StatefulRuleSession执行业务特定的规则运算。
 * @author yyang
 *
 */
public interface StatefulRuleCallback {
	void doInRuleSession(StatefulRuleSession session) throws Exception;
}
