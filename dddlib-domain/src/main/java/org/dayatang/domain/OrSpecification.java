package org.dayatang.domain;

/**
 * OR specification, used to create a new specifcation that is the OR of two other specifications.
 * @param <T>
 */
public class OrSpecification<T> extends AbstractSpecification<T> {

  private final Specification<T> spec1;
  private final Specification<T> spec2;

  /**
   * Create a new OR specification based on two other spec.
   *
   * @param spec1 Specification one.
   * @param spec2 Specification two.
   */
  public OrSpecification(final Specification<T> spec1, final Specification<T> spec2) {
    this.spec1 = spec1;
    this.spec2 = spec2;
  }

  /**
   * {@inheritDoc}
     * @param t
     * @return 
   */
  @Override
  public boolean isSatisfiedBy(final T t) {
    return spec1.isSatisfiedBy(t) || spec2.isSatisfiedBy(t);
  }
}
