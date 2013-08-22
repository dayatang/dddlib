package org.openkoala.gqc.application.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.inject.Named;
import javax.interceptor.Interceptors;

import org.apache.commons.beanutils.BeanUtils;
import org.openkoala.gqc.application.DataSourceApplication;
import org.openkoala.gqc.core.domain.DataSource;
import org.openkoala.gqc.core.domain.DataSourceType;
import org.openkoala.gqc.infra.util.DatabaseUtils;
import org.openkoala.gqc.vo.DataSourceVO;
import org.springframework.transaction.annotation.Transactional;

import com.dayatang.domain.InstanceFactory;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

/**
 * 数据源应用层实现，处理数据源的增删改查
 *
 */
@Named
@Transactional(value="transactionManager_gqc")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "DataSourceApplication")
@Remote
public class DataSourceApplicationImpl implements DataSourceApplication {

	/**
	 * 查询通道
	 */
	private static QueryChannelService queryChannel;
	
	/**
	 * 锁对象
	 */
	private static byte[] lock = new byte[0];

	/**
	 * 获取查询通道实例
	 * @return
	 */
	private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			synchronized (lock) {
				if (queryChannel == null) {
					queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
				}
			}
		}
		return queryChannel;
	}
	
	void setQueryChannelService(QueryChannelService queryChannel){
		DataSourceApplicationImpl.queryChannel = queryChannel;
	}

	public DataSourceVO getDataSourceVoById(Long id) {
		try {
//			String jpql = " select _dataSource from DataSource _dataSource  where _dataSource.id = ? ";
//			DataSource dataSource = (DataSource) getQueryChannelService().querySingleResult(jpql,
//					new Object[] { id });
			DataSource dataSource = DataSource.get(DataSource.class, id);
			DataSourceVO dataSourceVO = new DataSourceVO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(dataSourceVO, dataSource);
				dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType().getDescription());
			} catch (Exception e) {
			}
			return dataSourceVO;
		} catch (Exception e) {
			throw new RuntimeException("查询数据源失败！", e);
		}
	}

	public DataSourceVO getDataSourceVoByDataSourceId(String dataSourceId) {
		String jpql = " select _dataSource from DataSource _dataSource  where _dataSource.dataSourceId = ? ";
		try {
			DataSource dataSource = (DataSource) getQueryChannelService().querySingleResult(jpql,
					new Object[] { dataSourceId });
			if (dataSource != null) {
				DataSourceVO dataSourceVO = new DataSourceVO();
				BeanUtils.copyProperties(dataSourceVO, dataSource);
				dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType()
						.getDescription());
				return dataSourceVO;
			}else{
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException("查询指定数据源失败！", e);
		}
	}

	public String saveDataSource(DataSourceVO dataSourceVO) {
		try {
			DataSourceVO checkExist = this.getDataSourceVoByDataSourceId(dataSourceVO
					.getDataSourceId());
			if (checkExist != null) {
				return "该数据源ID已存在";
			}

			DataSource dataSource = new DataSource();
			// 系统数据源
			if (DataSourceType.SYSTEM_DATA_SOURCE.equals(dataSourceVO.getDataSourceType())) {
				try {
					// 若未抛异常，表明该系统数据源存在
					dataSource = DataSource.getSystemDataSource(dataSourceVO.getDataSourceId());
					dataSource.setConnectUrl(dataSource.getConnectUrl().split(";")[0]);
				} catch (Exception e) {
					return e.getMessage();
				}
				
				try {
                    dataSource.save();
                    return null;
                } catch (Exception e) {
                    return "新增失败";
                }
			}

			BeanUtils.copyProperties(dataSource, dataSourceVO);
			dataSource.save();
			
			return null;
		} catch (Exception e) {
			return "保存失败";
		}
	}

	public void updateDataSource(DataSourceVO dataSourceVO) {
		DataSource dataSource = DataSource.get(DataSource.class, dataSourceVO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(dataSource, dataSourceVO);
		} catch (Exception e) {
			throw new RuntimeException("更新数据源失败！", e);
		}
	}

	public void removeDataSource(Long id) {
		try {
			this.removeDataSources(new Long[] { id });
		} catch (Exception e) {
			throw new RuntimeException("删除数据源失败！", e);
		}
	}

	public void removeDataSources(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			DataSource dataSource = DataSource.load(DataSource.class, ids[i]);
			dataSource.remove();
		}
	}

	public List<DataSourceVO> findAllDataSource() {
		try {
			List<DataSourceVO> list = new ArrayList<DataSourceVO>();
			List<DataSource> all = DataSource.findAll(DataSource.class);
			for (DataSource dataSource : all) {
				DataSourceVO dataSourceVO = new DataSourceVO();
				// 将domain转成VO
				try {
					BeanUtils.copyProperties(dataSourceVO, dataSource);
				} catch (Exception e) {
				}
				list.add(dataSourceVO);
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException("查询数据源列表失败！", e);
		}
	}

	public List<String> findAllTable(Long id) {
		DataSource dataSource = null;
		Connection conn = null;
		try {
			dataSource = DataSource.get(DataSource.class, id);
			conn = dataSource.generateConnection();
			List<String> tableList = DatabaseUtils.getTables(conn);

			return tableList;
		} catch (Exception e) {
			throw new RuntimeException("查询所有表失败",e);
		}finally{
			if(conn != null){
				try {
					this.closeConnection(dataSource, conn);
				} catch (SQLException e) {
					throw new RuntimeException("关闭自定义数据源连接失败",e);
				}
			}
		}
	}

	public Map<String, Integer> findAllColumn(Long id, String tableName) {
		DataSource dataSource = null;
		Connection conn = null;
		try {
			dataSource = DataSource.get(DataSource.class, id);
			conn = dataSource.generateConnection();
			Map<String, Integer> tableMap = DatabaseUtils.getColumns(conn, tableName);
			
			return tableMap;
		} catch (Exception e) {
			throw new RuntimeException("查询所有列失败", e);
		}finally{
			if(conn != null){
				try {
					this.closeConnection(dataSource, conn);
				} catch (SQLException e) {
					throw new RuntimeException("关闭自定义数据源连接失败",e);
				}
			}
		}
	}
	
	public Page<DataSourceVO> pageQueryDataSource(DataSourceVO queryVo, int currentPage,
			int pageSize) {
		try {
			List<DataSourceVO> result = new ArrayList<DataSourceVO>();
			List<Object> conditionVals = new ArrayList<Object>();

			StringBuilder jpql = new StringBuilder(
					"select _dataSource from DataSource _dataSource   where 1=1 ");

			if (queryVo.getDataSourceId() != null && !"".equals(queryVo.getDataSourceId())) {
				jpql.append(" and _dataSource.dataSourceId like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDataSourceId()));
			}

			if (queryVo.getDataSourceDescription() != null
					&& !"".equals(queryVo.getDataSourceDescription())) {
				jpql.append(" and _dataSource.dataSourceDescription like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDataSourceDescription()));
			}

			if (queryVo.getConnectUrl() != null && !"".equals(queryVo.getConnectUrl())) {
				jpql.append(" and _dataSource.connectUrl like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getConnectUrl()));
			}

			if (queryVo.getJdbcDriver() != null && !"".equals(queryVo.getJdbcDriver())) {
				jpql.append(" and _dataSource.jdbcDriver like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getJdbcDriver()));
			}

			if (queryVo.getDriverUri() != null && !"".equals(queryVo.getDriverUri())) {
				jpql.append(" and _dataSource.driverUri like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getDriverUri()));
			}

			if (queryVo.getUsername() != null && !"".equals(queryVo.getUsername())) {
				jpql.append(" and _dataSource.username like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getUsername()));
			}

			if (queryVo.getPassword() != null && !"".equals(queryVo.getPassword())) {
				jpql.append(" and _dataSource.password like ?");
				conditionVals.add(MessageFormat.format("%{0}%", queryVo.getPassword()));
			}

			Page<DataSource> pages = getQueryChannelService().queryPagedResultByPageNo(jpql.toString(),
					conditionVals.toArray(), currentPage, pageSize);
			for (DataSource dataSource : pages.getResult()) {
				DataSourceVO dataSourceVO = new DataSourceVO();
				// 将domain转成VO
				try {
					BeanUtils.copyProperties(dataSourceVO, dataSource);
				} catch (Exception e) {
				}

				dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType().getDescription());

				result.add(dataSourceVO);
			}
			
			return new Page<DataSourceVO>(pages.getCurrentPageNo(), pages.getTotalCount(),
					pages.getPageSize(), result);
		} catch (Exception e) {
			throw new RuntimeException("查询数据源列表失败！", e);
		}
	}
	
	public boolean chechDataSourceCanConnect(DataSource dataSource) {
		try {
			//页面传递的是系统数据源的话，实际上只传递了dataSourceId，需要从数据库把详细的dataSource查出来
			if (dataSource.getDataSourceType().equals(DataSourceType.SYSTEM_DATA_SOURCE)) {
				dataSource = DataSource.getSystemDataSource(dataSource.getDataSourceId());
  	        }
			
			return dataSource.testConnection();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 测试数据源连接
	 * 
	 * @return
	 */
	public boolean testConnection(Long id) {
		DataSourceVO dataSourceVO = this.getDataSourceVoById(id);
		// 将domain转成VO
		try {
			DataSource dataSource = new DataSource();
			BeanUtils.copyProperties(dataSource, dataSourceVO);
			return dataSource.testConnection();
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 非系统数据源的连接才需要close
	 * @param dataSource
	 * @param conn
	 * @throws SQLException
	 */
	private void closeConnection(DataSource dataSource, Connection conn) throws SQLException {
		if(!DataSourceType.SYSTEM_DATA_SOURCE.equals(dataSource.getDataSourceType())){
			conn.close();
		}
	}

}
