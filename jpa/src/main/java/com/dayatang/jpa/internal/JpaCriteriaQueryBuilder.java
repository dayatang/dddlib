package com.dayatang.jpa.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;

import com.dayatang.domain.Entity;
import com.dayatang.domain.OrderSetting;
import com.dayatang.domain.QueryCriterion;
import com.dayatang.domain.QuerySettings;

public class JpaCriteriaQueryBuilder {
	private static JpaCriteriaQueryBuilder instance;
	
	public static synchronized JpaCriteriaQueryBuilder getInstance() {
		if (instance == null) {
			instance = new JpaCriteriaQueryBuilder();
		}
		return instance;
	}

	public final <T extends Entity> CriteriaQuery<T> createCriteriaQuery(QuerySettings<T> settings, EntityManager entityManager) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(settings.getEntityClass());
		Root<T> root = query.from(settings.getEntityClass());
		query.select(root);
		if (!StringUtils.isEmpty(settings.getRootAlias())) {
			root.alias(settings.getRootAlias());
		}
		Map<String, String> aliases = settings.getAliases();
		for (Map.Entry<String, String> each : aliases.entrySet()) {
			root.get(each.getKey()).alias(each.getValue());
		}
		
		JpaCriterionConverter converter = new JpaCriterionConverter(builder, root);
		List<Predicate> criterions = new ArrayList<Predicate>();
		for (QueryCriterion criterion : settings.getQueryCriterions()) {
			Predicate predicate = converter.convert(criterion);
			if (predicate != null) {
				criterions.add(predicate);
			}
		}
		if (criterions.size() == 1) {
			query.where(criterions.get(0));
		}
		if (criterions.size() > 1) {
			query.where(criterions.toArray(new Predicate[0]));
		}
		query.orderBy(toOrder(builder, root, settings.getOrderSettings()));
		return query;
	}

	@SuppressWarnings("rawtypes")
	private Order[] toOrder(CriteriaBuilder builder, Root root, List<OrderSetting> orderSettings) {
		Order[] results = new Order[orderSettings.size()];
		int i = 0;
		for (OrderSetting orderSetting : orderSettings) {
			Path<?> path = getPropPath(root, orderSetting.getPropName());
			if (orderSetting.isAscending()) {
				results[i] = builder.asc(path);
			} else {
				results[i] = builder.desc(path);
			}
			i++;
		}
		return results;
	}

	private Path<?> getPropPath(Root<?> root, String propName) {
		String[] props = propName.split("\\.");
		Path<?> path = root;
		for (int j = 0; j < props.length; j++) {
			path = path.get(props[j]);
		}
		return path;
	}
}
