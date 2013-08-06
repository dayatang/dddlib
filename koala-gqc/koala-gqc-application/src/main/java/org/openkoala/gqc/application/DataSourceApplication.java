
package org.openkoala.gqc.application;

import java.util.List;
import java.util.Map;

import org.openkoala.gqc.vo.DataSourceVO;

import com.dayatang.querychannel.support.Page;

public interface DataSourceApplication {

	public DataSourceVO getDataSource(Long id);
	
	public DataSourceVO getDataSourceVoByDataSourceId(String dataSourceId);
	
	public String saveDataSource(DataSourceVO dataSource);
	
	public void updateDataSource(DataSourceVO dataSource);
	
	public void removeDataSource(Long id);
	
	public void removeDataSources(Long[] ids);
	
	public List<DataSourceVO> findAllDataSource();
	
	public List<String> findAllTable(Long id);
	
	public Map<String, Integer> findAllColumn(Long id, String tableName);
	
	public Page<DataSourceVO> pageQueryDataSource(DataSourceVO dataSource, int currentPage, int pageSize);
	
	public boolean testConnection(Long id);
	

}

