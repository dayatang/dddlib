package com.dayatang.domain.internal;

import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QueryException;

public class AndCriterion implements QueryCriterion {
	private QueryCriterion[] criterions;

	public AndCriterion(QueryCriterion... criterions) {
		if (criterions == null || criterions.length < 2) {
			throw new QueryException("At least two query criterions required!");
		}
		this.criterions = criterions;
	}

	public QueryCriterion[] getCriterons() {
		return criterions;
	}
	
	
}
