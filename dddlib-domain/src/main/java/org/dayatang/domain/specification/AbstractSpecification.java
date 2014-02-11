package org.dayatang.domain.specification;

/**
 * 抽象规范实现，实现了规范的“与”、“或”、“非”操作。
 *
 * @param <T> 泛型参数
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<T> and(final Specification<T> specification) {
        return new AndSpecification<T>(this, specification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<T> or(final Specification<T> specification) {
        return new OrSpecification<T>(this, specification);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Specification<T> not() {
        return new NotSpecification<T>(this);
    }
}
