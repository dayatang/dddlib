package org.openkoala.gqc.application;

import java.util.Set;

import org.openkoala.gqc.core.domain.GeneralQuery;
import org.openkoala.gqc.core.domain.GeneralQueryEntity;

import com.dayatang.querychannel.support.Page;

public interface GqcApplication {

	public void saveEntity(GeneralQueryEntity entity);
	
	public void updateEntity(GeneralQueryEntity entity);
	
	public void removeEntity(GeneralQueryEntity entity);
	
	public <T extends GeneralQueryEntity> void removeEntities(Set<T> entities);
	
	public Page<GeneralQuery> pagingQueryGeneralQueries(int currentPage, int pagesize);
	
	public GeneralQuery getById(Long id);
	
	public Page<GeneralQuery> pagingQueryGeneralQueriesByQueryName(String queryName, int currentPage, int pagesize);
	
}
