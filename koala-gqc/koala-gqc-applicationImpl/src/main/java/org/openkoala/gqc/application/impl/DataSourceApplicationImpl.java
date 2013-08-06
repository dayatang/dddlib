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

@Named
@Transactional(value="transactionManager_gqc")
@Interceptors(value = org.openkoala.koala.util.SpringEJBIntercepter.class)
@Stateless(name = "DataSourceApplication")
@Remote
public class DataSourceApplicationImpl implements DataSourceApplication {

	private static QueryChannelService queryChannel;

	private static QueryChannelService getQueryChannelService() {
		if (queryChannel == null) {
			queryChannel = InstanceFactory.getInstance(QueryChannelService.class, "queryChannel_gqc");
		}
		return queryChannel;
	}

	public DataSourceVO getDataSource(Long id) {

		String jpql = "select _dataSource from DataSource _dataSource  where _dataSource.id=?";
		DataSource dataSource = (DataSource) getQueryChannelService().querySingleResult(jpql,
				new Object[] { id });
		DataSourceVO dataSourceVO = new DataSourceVO();
		// 将domain转成VO
		try {
			BeanUtils.copyProperties(dataSourceVO, dataSource);
			dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType().getDescription());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return dataSourceVO;
	}

	public DataSourceVO getDataSourceVoByDataSourceId(String dataSourceId) {
		String jpql = "select _dataSource from DataSource _dataSource  where _dataSource.dataSourceId=?";
		try {
			DataSource dataSource = (DataSource) getQueryChannelService().querySingleResult(jpql,
					new Object[] { dataSourceId });
			if (dataSource != null) {
				DataSourceVO dataSourceVO = new DataSourceVO();
				BeanUtils.copyProperties(dataSourceVO, dataSource);
				dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType()
						.getDescription());
				return dataSourceVO;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
					return "该系统数据源ID不存在";
				}
				
				try {
                    dataSource.save();
                    return null;
                } catch (Exception e) {
                    return "保存失败";
                }
			}

			BeanUtils.copyProperties(dataSource, dataSourceVO);
			dataSource.save();
			return null;

		} catch (Exception e) {
			e.printStackTrace();
			return "保存失败";
		}
	}

	public void updateDataSource(DataSourceVO dataSourceVO) {
		DataSource dataSource = DataSource.get(DataSource.class, dataSourceVO.getId());
		// 设置要更新的值
		try {
			BeanUtils.copyProperties(dataSource, dataSourceVO);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeDataSource(Long id) {
		this.removeDataSources(new Long[] { id });
	}

	public void removeDataSources(Long[] ids) {
		for (int i = 0; i < ids.length; i++) {
			DataSource dataSource = DataSource.load(DataSource.class, ids[i]);
			dataSource.remove();
		}
	}

	public List<DataSourceVO> findAllDataSource() {
		List<DataSourceVO> list = new ArrayList<DataSourceVO>();
		List<DataSource> all = DataSource.findAll(DataSource.class);
		for (DataSource dataSource : all) {
			DataSourceVO dataSourceVO = new DataSourceVO();
			// 将domain转成VO
			try {
				BeanUtils.copyProperties(dataSourceVO, dataSource);
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add(dataSourceVO);
		}
		return list;
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
			throw new RuntimeException("查询所有列失败",e);
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
				e.printStackTrace();
			}

			dataSourceVO.setDataSourceTypeDesc(dataSourceVO.getDataSourceType().getDescription());

			result.add(dataSourceVO);
		}
		return new Page<DataSourceVO>(pages.getCurrentPageNo(), pages.getTotalCount(),
				pages.getPageSize(), result);
	}

	/**
	 * 测试数据源连接
	 * 
	 * @return
	 */
	public boolean testConnection(Long id) {
		DataSourceVO dataSourceVO = this.getDataSource(id);
		// 将domain转成VO
		try {
			DataSource dataSource = new DataSource();
			BeanUtils.copyProperties(dataSource, dataSourceVO);
			return dataSource.testConnection();
		} catch (Exception e) {
//			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 非系统数据源的连接才需要close
	 * @param dataSource
	 * @param conn
	 * @throws SQLException
	 */
	private void closeConnection(DataSource dataSource, Connection conn) throws SQLException{
		if(!DataSourceType.SYSTEM_DATA_SOURCE.equals(dataSource.getDataSourceType())){
			conn.close();
		}
	}

}
