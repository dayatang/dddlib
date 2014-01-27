package org.openkoala.koala.mybatis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import com.dayatang.domain.Entity;
import com.dayatang.domain.QuerySettings;
import com.dayatang.querychannel.service.QueryChannelService;
import com.dayatang.querychannel.support.Page;

public class QueryChannelServiceMybatis implements QueryChannelService {

	private static final long serialVersionUID = -7717195132438268067L;
	
	private SqlSessionFactory sqlSessionFactory;

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public long queryResultSize(String queryStr, Object[] params) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		long size = 0;
		try {
			size = session.selectList(className+"."+queryStr, params).size();
		} finally {
		    session.close();
		}
		return size;
	}

	public <T> List<T> queryResult(String queryStr, Object[] params,
			long firstRow, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			List<T> result = session.selectList(className+"."+queryStr, paramsMap);
			return result;
		} finally {
		  session.close();
		}
	}

	public <T> T querySingleResult(String queryStr, Object[] params) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		try {
			T t = (T)session.selectOne(className+"."+queryStr, params);
			return t;
		} finally {
		  session.close();
		}
	}

	public <T> List<T> queryResult(String queryStr, Object[] params) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		try { 
			List<T> result = session.selectList(className+"."+queryStr, paramsMap);
			return result;
		} finally {
		  session.close();
		}
	}

	public List<Map<String, Object>> queryMapResult(String queryStr,
			Object[] params) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		try { 
			List<Map<String, Object>> result = session.selectList(className+"."+queryStr, paramsMap);
			return result;
		} finally {
		  session.close();
		}
	}

	public <T> Page<T> queryPagedResult(String queryStr, Object[] params,
			long firstRow, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			List<T> result = (List<T>)session.selectList(className+"."+queryStr, paramsMap);
			int count = (Integer)session.selectOne(className+"."+queryStr+"Count",paramsMap);
			Page<T> page = new Page<T>(firstRow, count,pageSize,result);
			return page;
		} finally {
		  session.close();
		}
	}

	public <T> Page<T> queryPagedResultByPageNo(String queryStr,
			Object[] params, int currentPage, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		int firstRow = (currentPage-1)*pageSize;
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			List<T> result = (List<T>)session.selectList(className+"."+queryStr, paramsMap);
			int count = (Integer)session.selectOne(className+"."+queryStr+"Count",paramsMap);
			Page<T> page = new Page<T>(firstRow, count,pageSize,result);
			return page;
		} finally {
		  session.close();
		}
	}

	public <T> Page<T> queryPagedResultByNamedQuery(String queryName,
			Object[] params, long firstRow, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			List<T> result = (List<T>)session.selectList(className+"."+queryName, paramsMap);
			int count = (Integer)session.selectOne(className+"."+queryName+"Count",paramsMap);
			Page<T> page = new Page<T>(firstRow, count,pageSize,result);
			return page;
		} finally {
		  session.close();
		}
	}

	public <T> Page<T> queryPagedResultByPageNoAndNamedQuery(String queryName,
			Object[] params, int currentPage, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		int firstRow = (currentPage-1)*pageSize;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			List<T> result = (List<T>)session.selectList(className+"."+queryName, paramsMap);
			int count = (Integer)session.selectOne(className+"."+queryName+"Count",paramsMap);
			Page<T> page = new Page<T>(firstRow, count,pageSize,result);
			return page;
		} finally {
		  session.close();
		}
	}

	public Page<Map<String, Object>> queryPagedMapResult(String queryStr,
			Object[] params, int currentPage, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		int firstRow = (currentPage-1)*pageSize;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			
			Object o= (session.selectList(className+"."+queryStr, paramsMap));
			List<Map<String, Object>> result = (List<Map<String, Object>>)o;
			int count = (Integer)session.selectOne(className+"."+queryStr+"Count",paramsMap);
			Page<Map<String, Object>> page = new Page<Map<String, Object>>(firstRow, count,pageSize,result);
			return page;
		} finally {
		  session.close();
		}
		
		
	}

	public Page<Map<String, Object>> queryPagedMapResultByNamedQuery(
			String queryName, Object[] params, int currentPage, int pageSize) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		int firstRow = (currentPage-1)*pageSize;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		paramsMap.put("firstRow", firstRow);
		paramsMap.put("pageSize", pageSize);
		try { 
			
			Object o= (session.selectList(className+"."+queryName, paramsMap));
			List<Map<String, Object>> result = (List<Map<String, Object>>)o;
			int count = (Integer)session.selectOne(className+"."+queryName+"Count",paramsMap);
			Page<Map<String, Object>> page = new Page<Map<String, Object>>(firstRow, count,pageSize,result);
			return page;
		} finally {
		  session.close();
		}
	}

	public <T extends Entity> Page<T> queryPagedByQuerySettings(
			QuerySettings<T> settings, int currentPage, int pageSize) {
		 throw new UnsupportedOperationException();
	}

}
