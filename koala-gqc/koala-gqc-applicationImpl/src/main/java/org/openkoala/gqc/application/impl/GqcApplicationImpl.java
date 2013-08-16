package org.openkoala.gqc.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.GeneralQueryEntity;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

@Named
@Transactional(value="transactionManager_gqc")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "GqcApplication")
@Remote
public class GqcApplicationImpl implements GqcApplication {

	/**
	 * 查询通道
	 */
	private static QueryChannelService queryChannel;

	/**
	 * 获取查询通道实例
	 * @return
	 */
	private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
		}
		return queryChannel;
	}
	
	public <T extends GeneralQueryEntity> T getEntity(Class<T> clazz, Long id) {
		return AbstractEntity.get(clazz, id);
	}
	
	public void saveEntity(GeneralQueryEntity entity) {
//	    GeneralQuery generalQuery = this.turnToGeneralQuery(generalQueryVO);
	    entity.save();
	}

	public void updateEntity(GeneralQueryEntity entity) {
	    try{
	    	entity.save();}
	    catch(Exception exception) {
	        exception.printStackTrace();
	    }
	}

	public void removeEntity(GeneralQueryEntity entity) {
		entity.remove();
	}

	public Page<GeneralQuery> pagingQueryGeneralQueries(int currentPage, int pagesize) {
	   	StringBuilder jpql = null;
		List<Object> conditionVals = null;
		Page<GeneralQuery> result = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			result = getQueryChannelService().queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public GeneralQuery getById(Long id){
	    return GeneralQuery.get(GeneralQuery.class, id);
	}
	
	public <T extends GeneralQueryEntity> void removeEntities(Set<T> entities) {
		for (GeneralQueryEntity entity : entities) {
			removeEntity(entity);
		}
	}

	public Page<GeneralQuery> pagingQueryGeneralQueriesByQueryName(String queryName, int currentPage, int pagesize) {
	   	StringBuilder jpql = null;
		List<Object> conditionVals = null;
		Page<GeneralQuery> result = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			
			if (queryName != null && !queryName.isEmpty()) {
				jpql = jpql.append(" where _generalQuery.queryName like ?");
				conditionVals.add("%" + queryName + "%");
			}
			
			result = getQueryChannelService().queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pagesize);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
}
