package org.openkoala.organisation.application;

import java.util.List;
import java.util.Set;

import org.openkoala.organisation.domain.Party;

import com.dayatang.domain.AbstractEntity;

/**
 * 组织系统基础应用接口，提供了一些通用的应用服务.
 * @author xmfang
 *
 */
public interface BaseApplication {

	/**
	 * 持久化一个参与者
	 * @param party
	 * @return
	 */
	void saveParty(Party party);
	
	/**
	 * 修改一个参与者
	 * @param entity
	 * @return
	 */
	void updateParty(Party party);
	
	/**
	 * 撤销一个参与者
	 * @param party
	 */
	void terminateParty(Party party);
	
	/**
	 * 同时撤销多个参与者
	 * @param parties
	 */
	<T extends Party> void terminateParties(Set<T> parties);
	
	/**
	 * 根据实体类型及其id获得实体
	 * @param clazz
	 * @param id
	 * @return
	 */
	<T extends AbstractEntity> T getEntity(Class<T> clazz, Long id);
	
	/**
	 * 根据参与者类型获得其所有实例
	 * @param clazz
	 * @return
	 */
	<T extends Party> List<T> findAll(Class<T> clazz);

}
