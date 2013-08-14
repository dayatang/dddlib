/**
 * 
 */
package com.dayatang.rule;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * 无状态规则服务接口，
 * 
 * @author cchen <a href="mailto:chencao0524@gmail.com">陈操</a>
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>) <a href="mailto:gdyangyu@gmail.com">杨宇</a>
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
@SuppressWarnings("rawtypes")
public interface StatelessRuleService extends Serializable {

	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
     * @param facts 用于执行规则的对象列表。
	 * @return 返回规则执行的结果，可能包括全部或部分传入的对象（这些对象的某些属性值可能已发生变化），以及规则执行过程中创建的某些对象。
	 * @throws RuleRuntimeException
	 */
	List executeRules(Object ruleSource, List facts);


	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param facts 用于执行规则的对象列表。
	 * @return 返回规则执行的结果，可能包括全部或部分传入的对象（这些对象的某些属性值可能已发生变化），以及规则执行过程中创建的某些对象。
	 * @throws RuleRuntimeException
	 */
	List executeRules(Object ruleSource, Map sessionProperties, List facts);


	/**
	 * 执行规则并返回结果
	 * @param ruleSource 规则源
	 * @param executionSetProperties 规则的属性Map(如：source=drl/xml dsl=java.io.Reader)
	 * @param sessionProperties 规则中的上下文（如全局变量等）
	 * @param facts 用于执行规则的对象列表。
	 * @return 返回规则执行的结果，可能包括全部或部分传入的对象（这些对象的某些属性值可能已发生变化），以及规则执行过程中创建的某些对象。
	 * @throws RuleRuntimeException
	 */
	List executeRules(Object ruleSource, Map executionSetProperties, Map sessionProperties, List facts);

}
