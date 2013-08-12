package org.openkoala.koala.mybatis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import com.dayatang.domain.DataPage;
import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.ExampleSettings;
import com.dayatang.domain.QuerySettings;
/**
 * EntityRepository的Mybatis实现版本
 * @author lingen
 *
 */
public class EntityRepositoryMybatis implements EntityRepository{
	
	
	private SqlSessionFactory sqlSessionFactory;

	public <T extends Entity> T save(T entity) {
		SqlSession session = sqlSessionFactory.openSession();  
		try {
			int i = 0;
			if(entity.getId()!=null){
				i = session.update(entity.getClass().getName()+".update",entity);
			}
			if(i==0){
				session.insert(entity.getClass().getName()+".insert",entity);
			}
		} finally {
		  session.close();
		}
		return entity;
	}

	public void remove(Entity entity) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			session.delete(entity.getClass().getName()+".remove",entity);
		} finally {
		  session.close();
		}
	}

	public <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
		T t = get(clazz,id);
		if(t!=null)return true;
		return false;
	}

	public <T extends Entity> T get(Class<T> clazz, Serializable id) {
		SqlSession session = sqlSessionFactory.openSession();
		try {
			T t = (T)session.selectOne(clazz.getName()+".get",id);
			return t;
		} finally {
		  session.close();
		}
	}

	public <T extends Entity> T load(Class<T> clazz, Serializable id) {
		return get(clazz,id);
	}

	public <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
		 throw new UnsupportedOperationException();
	}

	public <T extends Entity> List<T> findAll(Class<T> clazz) {
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		try {
			lists = session.selectList(clazz.getName()+".getAll", null);
		} finally {
		  session.close();
		}
		return lists;
	}

	public <T extends Entity> List<T> find(QuerySettings<T> settings) {
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		try {
			lists = session.selectList(settings.getEntityClass().getName()+".getAll", null);
		} finally {
		  session.close();
		}
		return lists;
	}

	public <T> List<T> find(String queryString, Object[] params,
			Class<T> resultClass) {
		
        String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		try {
			lists = session.selectList(className+"."+queryString, paramsMap);
		} finally {
		  session.close();
		}
		return lists;
		
	}

	public <T> List<T> find(String queryString, Map<String, Object> params,
			Class<T> resultClass) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		try {
			lists = session.selectList(className+"."+queryString, params);
		} finally {
		  session.close();
		}
		return lists;
	}

	public <T> List<T> findByNamedQuery(String queryName, Object[] params,
			Class<T> resultClass) {

		String className = new Exception().getStackTrace()[1].getClassName();
		
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		try {
			lists = session.selectList(className+"."+queryName, paramsMap);
		} finally {
		  session.close();
		}
		return lists;
	}

	public <T> List<T> findByNamedQuery(String queryName,
			Map<String, Object> params, Class<T> resultClass) {
		
		String className = new Exception().getStackTrace()[1].getClassName();
		
		SqlSession session = sqlSessionFactory.openSession();
		List<T> lists = null;
		try {
			lists = session.selectList(className+"."+queryName, params);
		} finally {
		  session.close();
		}
		return lists;
	}

	public <T extends Entity, E extends T> List<T> findByExample(E example,
			ExampleSettings<T> settings) {
		throw new UnsupportedOperationException();
	}

	public <T extends Entity> T getSingleResult(QuerySettings<T> settings) {
		throw new UnsupportedOperationException();
	}

	public <T> T getSingleResult(String queryString, Object[] params,
			Class<T> resultClass) {
		 String className = new Exception().getStackTrace()[1].getClassName();
			
			SqlSession session = sqlSessionFactory.openSession();
			T t = null;
			Map<String,Object> paramsMap = new HashMap<String,Object>();
			paramsMap.put("params", params);
			try {
				t = (T)session.selectOne(className+"."+queryString, paramsMap);
			} finally {
			  session.close();
			}
			return t;
	}

	public <T> T getSingleResult(String queryString,
			Map<String, Object> params, Class<T> resultClass) {
        String className = new Exception().getStackTrace()[1].getClassName();
		
		SqlSession session = sqlSessionFactory.openSession();
		T t = null;
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		try {
			t = (T)session.selectOne(className+"."+queryString, paramsMap);
		} finally {
		  session.close();
		}
		return t;
		
	}

	public void executeUpdate(String queryString, Object[] params) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		session.update(className+"."+queryString, paramsMap);
	}

	public void executeUpdate(String queryString, Map<String, Object> params) {
		String className = new Exception().getStackTrace()[1].getClassName();
		SqlSession session = sqlSessionFactory.openSession();
		Map<String,Object> paramsMap = new HashMap<String,Object>();
		paramsMap.put("params", params);
		session.update(className+"."+queryString, paramsMap);
	}

	public void flush() {
		throw new UnsupportedOperationException();
	}

	public void refresh(Entity entity) {
		throw new UnsupportedOperationException();
	}

	public <T extends Entity> DataPage<T> findAll(Class<T> clazz,
			int pageIndex, int pageSize) {
		throw new UnsupportedOperationException();
	}

	public <T extends Entity> DataPage<T> find(QuerySettings<T> settings,
			int pageIndex, int pageSize) {
		throw new UnsupportedOperationException();
	}

	public <T> DataPage<T> find(String queryString, Object[] params,
			int pageIndex, int pageSize, Class<T> resultClass) {
		throw new UnsupportedOperationException();
	}

	public <T> DataPage<T> find(String queryString, Map<String, Object> params,
			int pageIndex, int pageSize, Class<T> resultClass) {
		throw new UnsupportedOperationException();
	}

	public <T> DataPage<T> findByNamedQuery(String queryName, Object[] params,
			int pageIndex, int pageSize, Class<T> resultClass) {
		throw new UnsupportedOperationException();
	}

	public <T> DataPage<T> findByNamedQuery(String queryName,
			Map<String, Object> params, int pageIndex, int pageSize,
			Class<T> resultClass) {
		throw new UnsupportedOperationException();
	}

	public void clear() {
		
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}
	
}
