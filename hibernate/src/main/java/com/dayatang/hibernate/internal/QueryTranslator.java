package com.dayatang.hibernate.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.domain.*;
import com.dayatang.domain.internal.*;

public class QueryTranslator {

	private QuerySettings<?> settings;

	private String queryString = "";

	private List<String> criterias = new ArrayList<String>();
	private List<Object> params = new ArrayList<Object>();

	public String getQueryString() {
		return queryString;
	}

	public List<Object> getParams() {
		return params;
	}

	public QueryTranslator(QuerySettings<?> settings) {
		this.settings = settings;
		prepare();
	}

	private void prepare() {
		queryString = "select distinct(o) from " + settings.getEntityClass().getName() + " as o ";
		queryString += getWhereClause(settings.getQueryCriterions());
		queryString += getOrderClause(settings.getOrderSettings());
	}

	private String getWhereClause(Set<QueryCriterion> criterions) {
		if (criterions.isEmpty()) {
			return "";
		}
		for (QueryCriterion criterion : settings.getQueryCriterions()) {
			String aCriteria = processCriterion(criterion);
			if (StringUtils.isNotBlank(aCriteria)) {
				criterias.add(aCriteria);
			}
		}
		
		if (criterias.isEmpty()) {
			return "";
		}
		return " where " +  StringUtils.join(criterias, " and ");
	}

	private String processCriterion(QueryCriterion criterion) {
		if (criterion instanceof EqCriterion) {
			params.add(((EqCriterion) criterion).getValue());
			return "o." + ((EqCriterion)criterion).getPropName() + " = ?";
		}
		if (criterion instanceof NotEqCriterion) {
			params.add(((NotEqCriterion) criterion).getValue());
			return "o." + ((NotEqCriterion) criterion).getPropName() + " != ?";
		}
		if (criterion instanceof GtCriterion) {
			params.add(((GtCriterion) criterion).getValue());
			return "o." + ((GtCriterion) criterion).getPropName() + " > ?";
		}
		if (criterion instanceof GeCriterion) {
			params.add(((GeCriterion) criterion).getValue());
			return "o." + ((GeCriterion) criterion).getPropName() + " >= ?";
		}
		if (criterion instanceof LtCriterion) {
			params.add(((LtCriterion) criterion).getValue());
			return "o." + ((LtCriterion) criterion).getPropName() + " < ?";
		}
		if (criterion instanceof LeCriterion) {
			params.add(((LeCriterion) criterion).getValue());
			return "o." + ((LeCriterion) criterion).getPropName() + " <= ?";
		}
		if (criterion instanceof EqPropCriterion) {
			return "o." + ((EqPropCriterion) criterion).getPropName1() + " = o." + ((EqPropCriterion) criterion).getPropName2();
		}
		if (criterion instanceof NotEqPropCriterion) {
			return "o." + ((NotEqPropCriterion) criterion).getPropName1() + " != o." + ((NotEqPropCriterion) criterion).getPropName2();
		}
		if (criterion instanceof GtPropCriterion) {
			return "o." + ((GtPropCriterion) criterion).getPropName1() + " > o." + ((GtPropCriterion) criterion).getPropName2();
		}
		if (criterion instanceof GePropCriterion) {
			return "o." + ((GePropCriterion) criterion).getPropName1() + " >= o." + ((GePropCriterion) criterion).getPropName2();
		}
		if (criterion instanceof LtPropCriterion) {
			return "o." + ((LtPropCriterion) criterion).getPropName1() + " < o." + ((LtPropCriterion) criterion).getPropName2();
		}
		if (criterion instanceof LePropCriterion) {
			return "o." + ((LePropCriterion) criterion).getPropName1() + " <= o." + ((LePropCriterion) criterion).getPropName2();
		}
		if (criterion instanceof SizeEqCriterion) {
			params.add(((SizeEqCriterion) criterion).getValue());
			return "size(o." + ((SizeEqCriterion) criterion).getPropName() + ") = ?";
		}
		if (criterion instanceof SizeNotEqCriterion) {
			params.add(((SizeNotEqCriterion) criterion).getValue());
			return "size(o." + ((SizeNotEqCriterion) criterion).getPropName() + ") != ?";
		}
		if (criterion instanceof SizeGtCriterion) {
			params.add(((SizeGtCriterion) criterion).getValue());
			return "size(o." + ((SizeGtCriterion) criterion).getPropName() + ") > ?";
		}
		if (criterion instanceof SizeGeCriterion) {
			params.add(((SizeGeCriterion) criterion).getValue());
			return "size(o." + ((SizeGeCriterion) criterion).getPropName() + ") >= ?";
		}
		if (criterion instanceof SizeLtCriterion) {
			params.add(((SizeLtCriterion) criterion).getValue());
			return "size(o." + ((SizeLtCriterion) criterion).getPropName() + ") < ?";
		}
		if (criterion instanceof SizeLeCriterion) {
			params.add(((SizeLeCriterion) criterion).getValue());
			return "size(o." + ((SizeLeCriterion) criterion).getPropName() + ") <= ?";
		}
		if (criterion instanceof StartsWithTextCriterion) {
			params.add(((StartsWithTextCriterion) criterion).getValue() + "%");
			return "o." + ((StartsWithTextCriterion) criterion).getPropName() + " like ?";
		}
		if (criterion instanceof ContainsTextCriterion) {
			params.add("%" + ((ContainsTextCriterion) criterion).getValue() + "%");
			return "o." + ((ContainsTextCriterion) criterion).getPropName() + " like ?";
		}
		if (criterion instanceof BetweenCriterion) {
			params.add(((BetweenCriterion) criterion).getFrom());
			params.add(((BetweenCriterion) criterion).getTo());
			return "o." + ((BetweenCriterion) criterion).getPropName() + " between ? and ?";
		}
		if (criterion instanceof InCriterion) {
			Collection<? extends Object> value = ((InCriterion) criterion).getValue();
			if (value == null || value.isEmpty()) {
				return "1 > 1";
			} else {
				return "o." + ((InCriterion) criterion).getPropName() + " in (" + createInString(value) + ")";
			}
		}
		if (criterion instanceof NotInCriterion) {
			Collection<? extends Object> value = ((NotInCriterion) criterion).getValue();
			if (value == null || value.isEmpty()) {
				return "";
			}
			return "o." + ((NotInCriterion) criterion).getPropName() + " not in (" + createInString(value) + ")";
		}
		if (criterion instanceof IsNullCriterion) {
			return "o." + ((IsNullCriterion) criterion).getPropName() + " is null";
		}
		if (criterion instanceof NotNullCriterion) {
			return "o." + ((NotNullCriterion) criterion).getPropName() + " is not null";
		}
		if (criterion instanceof IsEmptyCriterion) {
			return "o." + ((IsEmptyCriterion)criterion).getPropName() + " is empty";
		}
		if (criterion instanceof NotEmptyCriterion) {
			return "o." + ((NotEmptyCriterion)criterion).getPropName() + " is not empty";
		}
		if (criterion instanceof AndCriterion) {
			List<String> subCriterionsStr = new ArrayList<String>();
			for (QueryCriterion each : ((AndCriterion) criterion).getCriterons()) {
				subCriterionsStr.add("(" + processCriterion(each) + ")");
			}
			return "(" + StringUtils.join(subCriterionsStr, " and ") + ")";
		}
		if (criterion instanceof OrCriterion) {
			List<String> subCriterionsStr = new ArrayList<String>();
			for (QueryCriterion each : ((OrCriterion) criterion).getCriterons()) {
				subCriterionsStr.add("(" + processCriterion(each) + ")");
			}
			return "(" + StringUtils.join(subCriterionsStr, " or ") + ")";
		}
		if (criterion instanceof NotCriterion) {
			return "(not (" + processCriterion(((NotCriterion) criterion).getCriteron()) + "))";
		}
		throw new IllegalStateException("Not support criterion type: " + criterion.getClass());
	}

	private String createInString(Collection<? extends Object> value) {
		Set<Object> elements = new HashSet<Object>();
		for (Object item : value) {
			Object element;
			if (item instanceof Entity) {
				element = ((Entity)item).getId();
			} else {
				element = item;
			}
			if (element instanceof String || element instanceof Date) {
				element = "'" + element + "'";
			}
			elements.add(element);
		}
		return StringUtils.join(elements, ",");
	}

	private String getOrderClause(List<OrderSetting> OrderSetting) {
		if (OrderSetting == null || OrderSetting.isEmpty()) {
			return "";
		}
		List<String> elements = new ArrayList<String>();
		for (OrderSetting orderSetting : OrderSetting) {
			elements.add(orderSetting.getPropName() + (orderSetting.isAscending() ? " asc" : " desc"));
		}
		return " order by " + StringUtils.join(elements, ",");
	}
}
