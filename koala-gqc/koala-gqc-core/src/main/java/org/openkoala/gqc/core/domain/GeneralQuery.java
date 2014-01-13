package org.openkoala.gqc.core.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.openkoala.gqc.core.domain.utils.PagingQuerier;
import org.openkoala.gqc.core.domain.utils.QueryAllQuerier;
import org.openkoala.gqc.core.domain.utils.SqlStatmentMode;

import com.dayatang.domain.QuerySettings;
import com.dayatang.querychannel.support.Page;

/**
 * 通用查询
 * 
 * 作    者：xmfang(xinmin.fang@gmail.com)
 */
@Entity
@Table(name = "GENERAL_QUERYS")
public class GeneralQuery extends GeneralQueryEntity {
	
	private static final long serialVersionUID = -3088017969475345884L;

	/**
	 * 数据源
	 */
	@ManyToOne
	@JoinColumn(name = "DATA_SOURCE_ID", nullable = false)
	private DataSource dataSource;
	
	/**
	 * 查询名称
	 */
	@Column(name = "QUERY_NAME", unique = true)
	private String queryName;
	
	/**
	 * 表名
	 */
	@Column(name = "TABLE_NAME", nullable = false)
	private String tableName;
	
	/**
	 * 描述
	 */
	@Column(name = "DESCRIPTION")
	private String description;
	
	/**
	 * 创建日期
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "CREATE_DATE")
	private Date createDate;
	
	/**
	 * 静态查询条件
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GQ_PRE_QUERY_CONDITIONS", joinColumns = @JoinColumn(name = "GQ_ID"))
	@OrderColumn(name = "ORDER_COLUMN")
	private List<PreQueryCondition> preQueryConditions = new ArrayList<PreQueryCondition>();
	
	/**
	 * 动态查询条件
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GQ_DYNAMIC_QUERIES", joinColumns = @JoinColumn(name = "GQ_ID"))
	@OrderColumn(name = "ORDER_COLUMN")
	private List<DynamicQueryCondition> dynamicQueryConditions = new ArrayList<DynamicQueryCondition>();
	
	/**
	 * 查询结果要显示的字段
	 */
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "GQ_FIELD_DETAILS", joinColumns = @JoinColumn(name = "GQ_ID"))
	@OrderColumn(name = "ORDER_COLUMN")
	private List<FieldDetail> fieldDetails = new ArrayList<FieldDetail>();
	
	/**
	 * 查询所有查询器
	 */
	private transient QueryAllQuerier queryAllQuerier;
	
	/**
	 * 分页查询查询器
	 */
	private transient PagingQuerier pagingQuerier;
	
	public GeneralQuery() {
		
	}
	
	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public String getQueryName() {
		return queryName;
	}

	public void setQueryName(String queryName) {
		this.queryName = queryName;
	}

	public GeneralQuery(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
 
	public Date getCreateDate() {
		return new Date(createDate.getTime());
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<PreQueryCondition> getPreQueryConditions() {
		return preQueryConditions;
	}

	public void setPreQueryConditions(List<PreQueryCondition> preQueryConditions) {
		this.preQueryConditions = preQueryConditions;
	}

	public List<DynamicQueryCondition> getDynamicQueryConditions() {
		return dynamicQueryConditions;
	}

	public void setDynamicQueryConditions(
			List<DynamicQueryCondition> dynamicQueryConditions) {
		this.dynamicQueryConditions = dynamicQueryConditions;
	}

	public List<FieldDetail> getFieldDetails() {
		return fieldDetails;
	}

	public void setFieldDetails(List<FieldDetail> fieldDetails) {
		this.fieldDetails = fieldDetails;
	}

	public QueryAllQuerier getQueryAllQuerier() {
		if (queryAllQuerier == null) {
			queryAllQuerier = new QueryAllQuerier(getQuerySql(), dataSource);
		} else {
			queryAllQuerier.setQuerySql(getQuerySql());
		}
		return queryAllQuerier;
	}

	public PagingQuerier getPagingQuerier() {
		if (pagingQuerier == null) {
			pagingQuerier = new PagingQuerier(getQuerySql(), dataSource);
		} else {
			pagingQuerier.setQuerySql(getQuerySql());
		}
		return pagingQuerier;
	}

	/**
	 * 执行查询，返回查询结果，不分页。
	 * @return
	 */
	public List<Map<String, Object>> query() {
		return getQueryAllQuerier().query();
	}

	/**
	 * 执行分页查询，返回查询结果。
	 * @param startNumber
	 * @param pagesize
	 * @return 
	 */
	public List<Map<String, Object>> pagingQuery(int currentPage, int pagesize) {
		getPagingQuerier().changePagingParameter(Page.getStartOfPage(currentPage, pagesize), pagesize);
		return getPagingQuerier().query();
	}
	
	/**
	 * 执行分页查询，返回page对象。
	 * @param currentPage
	 * @param pagesize
	 * @return
	 */
	public Page<Map<String, Object>> pagingQueryPage(int currentPage, int pagesize) {
		long totalCount = getPagingQuerier().caculateTotalCount();
		return new Page<Map<String, Object>>(Page.getStartOfPage(currentPage, pagesize), totalCount, pagesize, pagingQuery(currentPage, pagesize));
	}
	
	/**
	 * 根据通用查询名称查询通用查询实例
	 * @param queryName
	 * @return
	 */
	public static GeneralQuery findByQueryName(String queryName) {
		return getRepository().getSingleResult(QuerySettings.create(GeneralQuery.class).containsText("queryName", queryName));
	}
	
	
	/**
	 * 生成查询SQL语句
	 * @return
	 */
	public SqlStatmentMode getQuerySql() {
		return generateCommonQuerySql();
	}
	
	/**
	 * 获得可现实的静态条件
	 * @return
	 */
	public List<PreQueryCondition> getVisiblePreQueryConditions() {
		List<PreQueryCondition> results = new ArrayList<PreQueryCondition>();
		for (PreQueryCondition preQueryCondition : preQueryConditions) {
			if (preQueryCondition.getVisible()) {
				results.add(preQueryCondition);
			}
		}
		return results;
	}
	
	private SqlStatmentMode generateCommonQuerySql() {
		SqlStatmentMode result = new SqlStatmentMode();
		result.setStatment(generateSelectPrefixStatement());
		
		addPreQueryConditionStatement(result);
		addDynamicQueryConditionStatement(result);
		
		return result;
	}
	
	private String generateSelectPrefixStatement() {
		StringBuilder result = new StringBuilder();
		result.append("select ");
		
		for (FieldDetail fieldDetail : fieldDetails) {
			result.append(fieldDetail.getFieldName() + ",");
		}
		result.deleteCharAt(result.length() - 1);
		result.append(" from " + tableName);
		
		if (!preQueryConditions.isEmpty() || !dynamicQueryConditions.isEmpty()) {
			result.append(" where 1=1");
		}

		return result.toString();
	}
	
	private void addPreQueryConditionStatement(SqlStatmentMode sqlStatmentMode) {
		generateConditionStatement(preQueryConditions, sqlStatmentMode);
	}
	
	private void addDynamicQueryConditionStatement(SqlStatmentMode sqlStatmentMode) {
		generateConditionStatement(dynamicQueryConditions, sqlStatmentMode);
	}
	
	private <T extends QueryCondition> void generateConditionStatement(List<T> queryConditions, SqlStatmentMode sqlStatmentMode) {
		if (queryConditions.isEmpty()) {
			return;
		}
		
		for (T queryCondition : queryConditions) {
			SqlStatmentMode conditionSqlStatment = queryCondition.generateConditionStatment();
			sqlStatmentMode.setStatment(sqlStatmentMode.getStatment() + conditionSqlStatment.getStatment());
			sqlStatmentMode.addValues(conditionSqlStatment.getValues());
		}
	}
	
	/**
	 * 通过字段名称查询动态查询条件
	 * @param fieldName
	 * @return
	 */
	public DynamicQueryCondition getDynamicQueryConditionByFieldName(String fieldName) {
		for (DynamicQueryCondition dynamicQueryCondition : dynamicQueryConditions) {
			if (dynamicQueryCondition.getFieldName().equals(fieldName)) {
				return dynamicQueryCondition;
			}
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((dynamicQueryConditions == null) ? 0 : dynamicQueryConditions
						.hashCode());
		result = prime * result
				+ ((fieldDetails == null) ? 0 : fieldDetails.hashCode());
		result = prime
				* result
				+ ((preQueryConditions == null) ? 0 : preQueryConditions
						.hashCode());
		result = prime * result
				+ ((tableName == null) ? 0 : tableName.hashCode());
		result = prime * result
				+ ((queryName == null) ? 0 : queryName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;
		}
		if (obj == null){
			return false;
		}
		if (getClass() != obj.getClass()){
			return false;
		}
		GeneralQuery other = (GeneralQuery) obj;
		if (dynamicQueryConditions == null) {
			if (other.dynamicQueryConditions != null){
				return false;
			}
		} else if (!dynamicQueryConditions.equals(other.dynamicQueryConditions)){
			return false;
		}
		if (fieldDetails == null) {
			if (other.fieldDetails != null){
				return false;
			}
		} else if (!fieldDetails.equals(other.fieldDetails)){
			return false;
		}
		if (preQueryConditions == null) {
			if (other.preQueryConditions != null){
				return false;
			}
		} else if (!preQueryConditions.equals(other.preQueryConditions)){
			return false;
		}
		if (tableName == null) {
			if (other.tableName != null){
				return false;
			}
		} else if (!tableName.equals(other.tableName)){
			return false;
		}
		if (queryName == null) {
			if (other.queryName != null){
				return false;
			}
		} else if (!queryName.equals(other.queryName)){
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "general query : " + tableName;
	}

}
