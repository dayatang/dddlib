package org.dayatang.domain.internal;

import org.dayatang.domain.QueryCriterion;
import org.dayatang.domain.QueryException;

public class NotCriterion implements QueryCriterion {
	private QueryCriterion criterion;

	public NotCriterion(QueryCriterion criterion) {
		if (criterion == null) {
			throw new QueryException("Query criterion is null!");
		}
		this.criterion = criterion;
	}

	public QueryCriterion getCriteron() {
		return criterion;
	}
}
