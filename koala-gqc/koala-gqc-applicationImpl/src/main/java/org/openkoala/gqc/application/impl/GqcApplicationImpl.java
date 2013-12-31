package org.openkoala.gqc.application.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.openkoala.gqc.application.GqcApplication;
import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.GeneralQueryEntity;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.AbstractEntity;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 通用查询器应用层实现，提供增删改查功能
 *
 */
@Named
@Transactional(value="transactionManager_gqc")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "GqcApplication")
@Remote
public class GqcApplicationImpl implements GqcApplication {

	/**
	 * 查询通道
	 */
	@Inject
	private QueryChannelService queryChannel;
	
	/**
	 * 锁对象
	 */
//	private static byte[] lock = new byte[0];

	/**
	 * 获取查询通道实例
	 * @return
	 */
	/*private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			synchronized (lock) {
				if (queryChannel == null) {
					queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
				}
			}
		}
		return queryChannel;
	}*/
	
	public <T extends GeneralQueryEntity> T getEntity(Class<T> clazz, Long id) {
		try {
			return AbstractEntity.get(clazz, id);
		} catch (Exception e) {
			throw new RuntimeException("查询指定查询器失败！", e);
		}
	}
	
	public void saveEntity(GeneralQueryEntity entity) {
	    try {
			entity.save();
		} catch (Exception e) {
			throw new RuntimeException("保存查询器失败！", e);
		}
	}

	public void updateEntity(GeneralQueryEntity entity) {
	    try{
	    	entity.save();}
	    catch(Exception e) {
	    	throw new RuntimeException("修改查询器失败！", e);
	    }
	}

	public void removeEntity(GeneralQueryEntity entity) {
		try {
			entity.remove();
		} catch (Exception e) {
			throw new RuntimeException("删除指定查询器失败！", e);
		}
	}

	public Page<GeneralQuery> pagingQueryGeneralQueries(int currentPage, int pagesize) {
	   	StringBuilder jpql = null;
		List<Object> conditionVals = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			return queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pagesize);
		} catch (Exception e) {
			throw new RuntimeException("查询失败！", e);
		}
	}
	
	public GeneralQuery getById(Long id){
	    try {
			return GeneralQuery.get(GeneralQuery.class, id);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("查询指定的查询器失败！", e);
		}
	}
	
	public <T extends GeneralQueryEntity> void removeEntities(Set<T> entities) {
		try {
			for (GeneralQueryEntity entity : entities) {
				removeEntity(entity);
			}
		} catch (Exception e) {
			throw new RuntimeException("批量删除失败！", e);
		}
	}

	public Page<GeneralQuery> pagingQueryGeneralQueriesByQueryName(String queryName, int currentPage, int pagesize) {
	   	StringBuilder jpql = null;
		List<Object> conditionVals = null;
		try {
			jpql = new StringBuilder("select _generalQuery from GeneralQuery _generalQuery");
			conditionVals = new ArrayList<Object>();
			
			if (queryName != null && !queryName.isEmpty()) {
				jpql = jpql.append(" where _generalQuery.queryName like ?");
				conditionVals.add("%" + queryName + "%");
			}
			
			return queryChannel.queryPagedResultByPageNo(jpql.toString(), conditionVals.toArray(), currentPage, pagesize);
		} catch (Exception e) {
			throw new RuntimeException("查询失败！", e);
		}
	}
	
}
