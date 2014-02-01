/**
 * 
 */
package com.dayatang.rule;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * 无状态规则服务接口，
 * 
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>) <a href="mailto:gdyangyu@gmail.com">杨宇</a>
 * 
 * @version $LastChangedRevision$ $LastChangedBy$ $LastChangedDate$
 * 
 */
@SuppressWarnings("rawtypes")
public interface StatelessRuleService extends Serializable {

	/**
	 * 执行规则并返回结果
     * @param facts 用于执行规则的对象列表。
	 * @return 返回规则执行的结果，可能包括全部或部分传入的对象（这些对象的某些属性值可能已发生变化），以及规则执行过程中创建的某些对象。
	 * @throws RuleRuntimeException
	 */
	List executeRules(List facts);
}
