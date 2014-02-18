package org.dayatang.domain.internal;

import org.dayatang.domain.QueryCriterion;


/**
 * 查询条件的抽象基类，实现了AND、OR、NOT操作。
 * @author yyang (<a href="mailto:gdyangyu@gmail.com">gdyangyu@gmail.com</a>)
 */
public abstract class AbstractCriterion implements QueryCriterion {

	/**
	 * 执行AND操作，返回代表两个QueryCriterion的“与”操作结果的一个新的QueryCriterion
	 * @param criterion 另一个QueryCriterion
	 * @return 当前对象与criterion的“与”操作的结果
	 */
	public QueryCriterion and(QueryCriterion criterion) {
		return new AndCriterion(this, criterion);
	}
	
	/**
	 * 执行OR操作，返回代表两个QueryCriterion的“或”操作结果的一个新的QueryCriterion
	 * @param criterion 另一个QueryCriterion
	 * @return 当前对象与criterion的“或”操作的结果
	 */
	public QueryCriterion or(QueryCriterion criterion) {
		return new OrCriterion(this, criterion);
	}

	/**
	 * 执行NOT操作，返回代表当前对象的“非”操作的一个新的QueryCriterion
	 * @return
	 */
	public QueryCriterion not() {
		return new NotCriterion(this);
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
}
