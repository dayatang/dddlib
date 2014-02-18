package org.dayatang.persistence.jpa.internal;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.dayatang.domain.Entity;
import org.dayatang.domain.KeyValue;
import org.dayatang.domain.OrderSettings;
import org.dayatang.domain.QueryCriterion;

public class JpaCriteriaQueryBuilder {
	private static JpaCriteriaQueryBuilder instance;
	
	public static synchronized JpaCriteriaQueryBuilder getInstance() {
		if (instance == null) {
			instance = new JpaCriteriaQueryBuilder();
		}
		return instance;
	}

    @SuppressWarnings("unchecked")
	public final <T extends Entity> CriteriaQuery<T> createCriteriaQuery(
            org.dayatang.domain.CriteriaQuery dddQuery, EntityManager entityManager) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(dddQuery.getEntityClass());
        Root<T> root = query.from(dddQuery.getEntityClass());
        query.select(root);

        JpaCriterionConverter converter = new JpaCriterionConverter(builder, root);
        QueryCriterion criterion = dddQuery.getQueryCriterion();
        if (!criterion.isEmpty()) {
            Predicate predicate = converter.convert(criterion);
            query.where(predicate);
        }
        query.orderBy(toOrder(builder, root, dddQuery.getOrderSettings()));
        return query;
    }

	@SuppressWarnings("rawtypes")
	private Order[] toOrder(CriteriaBuilder builder, Root root, OrderSettings orderSettings) {
		List<Order> results = new ArrayList<Order>();
		for (KeyValue<String, Boolean> orderSetting : orderSettings.getOrderBy()) {
			Path<?> path = getPropPath(root, orderSetting.getKey());
			if (orderSetting.getValue().booleanValue()) {
				results.add(builder.asc(path));
			} else {
				results.add(builder.desc(path));
			}
		}
		return (Order[]) results.toArray(new Order[] {});
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
