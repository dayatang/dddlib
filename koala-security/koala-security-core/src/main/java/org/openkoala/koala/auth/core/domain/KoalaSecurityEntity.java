/*
 * Copyright (c) openkoala 2011 All Rights Reserved
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.openkoala.koala.auth.core.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.dayatang.domain.Entity;
import com.dayatang.domain.EntityRepository;
import com.dayatang.domain.InstanceFactory;
import com.dayatang.domain.QuerySettings;

/**
 * 类    名：KoalaEntity.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-3-22上午11:51:40     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@MappedSuperclass
public abstract class KoalaSecurityEntity implements Entity {
    
    private static final long serialVersionUID = 1342711951865077906L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    private int version;


    /**
     * 获得实体的标识
     * 
     * @return 实体的标识
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置实体的标识
     * 
     * @param id
     *            要设置的实体标识
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获得实体的版本号。持久化框架以此实现乐观锁。
     * 
     * @return 实体的版本号
     */
    public int getVersion() {
        return version;
    }

    /**
     * 设置实体的版本号。持久化框架以此实现乐观锁。
     * 
     * @param version
     *            要设置的版本号
     */
    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isNew() {
        return id == null || id.intValue() == 0;
    }

    private static EntityRepository repository;

    /**
     * 获取仓储
     * @return
     */
    public static EntityRepository getRepository() {
        if (repository == null) {
            repository = InstanceFactory.getInstance(EntityRepository.class,"repository_ss");
        }
        return repository;
    }

    /**
     * 设置仓储
     * @param repository
     */
    public static void setRepository(EntityRepository repository) {
        KoalaSecurityEntity.repository = repository;
    }

    /**
     * 保存实体
     */
    public void save() {
        getRepository().save(this);
    }

    /**
     * 删除实体
     */
    public void remove() {
        getRepository().remove(this);
    }

    /**
     * 判断一个实体是否已经存在
     * @param clazz
     * @param id
     * @return
     */
    public static <T extends Entity> boolean exists(Class<T> clazz, Serializable id) {
        return getRepository().exists(clazz, id);
    }

    /**
     * 获取一个实体
     * @param clazz
     * @param id
     * @return
     */
    public static <T extends Entity> T get(Class<T> clazz, Serializable id) {
        return getRepository().get(clazz, id);
    }

    /**
     * 获取未修改的实体
     * @param clazz
     * @param entity
     * @return
     */
    public static <T extends Entity> T getUnmodified(Class<T> clazz, T entity) {
        return getRepository().getUnmodified(clazz, entity);
    }

    /**
     * 加载一个实体
     * @param clazz
     * @param id
     * @return
     */
    public static <T extends Entity> T load(Class<T> clazz, Serializable id) {
        return getRepository().load(clazz, id);
    }

    /**
     * 获取所有的实体
     * @param clazz
     * @return
     */
    public static <T extends Entity> List<T> findAll(Class<T> clazz) {
        return getRepository().find(QuerySettings.create(clazz));
    }


    @Override
	public boolean existed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean notExisted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean existed(String propertyName, Object propertyValue) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object other);

    @Override
    public abstract String toString();
}
