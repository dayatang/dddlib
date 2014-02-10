package org.dayatang.domain;

/**
 * 规范接口，对应于DDD一书中的“规范”概念
 * @param <T> 类型参数，表明规范所针对的类型
 */
public interface Specification<T> {

  /**
   * 检查 {@code t} 是否由本规范满足.
   *
   * @param t 要测试的对象.
   * @return 如果{@code t}满足此规范则返回{@code true}。
   */
  boolean isSatisfiedBy(T t);

  /**
   * 创建一个新规范，作为本规范{@code this}和另一个规范{@code specification}的“与（AND）”操作的结果
   * @param specification 另一个规范。
   * @return 一个新规范。
   */
  Specification<T> and(Specification<T> specification);

  /**
   * 创建一个新规范，作为本规范{@code this}和另一个规范{@code specification}的“或（OR）”操作的结果
   * @param specification 另一个规范。
   * @return 一个新规范。
   */
  Specification<T> or(Specification<T> specification);

  /**
   * 创建一个新规范，作为另一个规范{@code specification}的“非（NOT）”操作的结果
   * @param specification 另一个规范。
   * @return 一个新规范。
   */
  Specification<T> not(Specification<T> specification);
}
