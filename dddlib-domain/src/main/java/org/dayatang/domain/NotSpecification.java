package org.dayatang.domain;

/**
 * NOT decorator, used to create a new specifcation that is the inverse (NOT) of the given spec.
 * @param <T>
 */
public class NotSpecification<T> extends AbstractSpecification<T> {

  private final Specification<T> spec1;

  /**
   * Create a new NOT specification based on another spec.
   *
   * @param spec1 Specification instance to not.
   */
  public NotSpecification(final Specification<T> spec1) {
    this.spec1 = spec1;
  }

  /**
   * {@inheritDoc}
     * @param t
     * @return 
   */
  @Override
  public boolean isSatisfiedBy(final T t) {
    return !spec1.isSatisfiedBy(t);
  }
}
