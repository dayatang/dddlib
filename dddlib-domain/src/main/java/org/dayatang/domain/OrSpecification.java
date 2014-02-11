package org.dayatang.domain;

import org.dayatang.utils.Assert;

/**
 * OR规范，用于创建一个新规范，作为其他两个规范的"或（OR）"操作的结果。
 *
 * @param <T> 类型参数，表示规范所应用到的目标对象类型。
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

    private final Specification<T> spec1;
    private final Specification<T> spec2;

    /**
     * 基于其他两个规范创建它们的OR规范。
     *
     * @param spec1 第一个规范.
     * @param spec2 第二个规范.
     */
    public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
        Assert.notNull(spec1, "Specification " + spec1 + " is null!");
        Assert.notNull(spec2, "Specification " + spec2 + " is null!");
        this.spec1 = spec1;
        this.spec2 = spec2;
    }

    /**
     * {@inheritDoc}
     *
     * @param t 要用来判断是否满足本OR规范的对象。
     * @return 如果对象至少满足规范1和规范2两个规范之一则返回true,否则返回false。
     */
    @Override
    public boolean isSatisfiedBy(final T t) {
        return spec1.isSatisfiedBy(t) || spec2.isSatisfiedBy(t);
    }
}
