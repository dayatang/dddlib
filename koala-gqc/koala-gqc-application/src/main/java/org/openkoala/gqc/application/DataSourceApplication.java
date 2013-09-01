
package org.openkoala.gqc.application;

import java.util.List;
import java.util.Map;

import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.vo.DataSourceVO;

import com.dayatang.querychannel.support.Page;

/**
 * 数据源应用层接口，处理数据源的增删改查
 *
 */
public interface DataSourceApplication {

	/**
	 * 查询数据源
	 * @param id 主键id
	 * @return
	 */
	public DataSourceVO getDataSourceVoById(Long id);
	
	/**
	 * 查询数据源
	 * @param dataSourceId 数据源id
	 * @return
	 */
	public DataSourceVO getDataSourceVoByDataSourceId(String dataSourceId);
	
	/**
	 * 保存数据源
	 * @param dataSource 待保存的数据源
	 * @return
	 */
	public String saveDataSource(DataSourceVO dataSource);
	
	/**
	 * 更新数据源
	 * @param dataSource 待更新的数据源
	 */
	public void updateDataSource(DataSourceVO dataSource);
	
	/**
	 * 删除数据源
	 * @param id 主键id
	 */
	public void removeDataSource(Long id);
	
	/**
	 * 批量删除数据源
	 * @param ids 数据源主键数组
	 */
	public void removeDataSources(Long[] ids);
	
	/**
	 * 查询所有数据源
	 * @return
	 */
	public List<DataSourceVO> findAllDataSource();
	
	/**
	 * 查询所有表
	 * @param id 数据源主键
	 * @return
	 */
	public List<String> findAllTable(Long id);
	
	/**
	 * 查询所有列
	 * @param id 数据源主键
	 * @param tableName 表名
	 * @return
	 */
	public Map<String, Integer> findAllColumn(Long id, String tableName);
	
	/**
	 * 分页查询数据源
	 * @param dataSource 查询条件
	 * @param currentPage 当前页
	 * @param pageSize 页面大小
	 * @return
	 */
	public Page<DataSourceVO> pageQueryDataSource(DataSourceVO dataSource, int currentPage, int pageSize);
	
	/**
	 * 检测数据源是否可用
	 * @param dataSource
	 * @return
	 */
	public boolean checkDataSourceCanConnect(DataSource dataSource);
	
	/**
	 * 测试数据源连接
	 * @param id 数据源主键
	 * @return
	 */
	public boolean testConnection(Long id);
	

}

