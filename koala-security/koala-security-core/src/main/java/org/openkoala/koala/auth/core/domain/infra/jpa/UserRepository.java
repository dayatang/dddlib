package org.openkoala.koala.auth.core.domain.infra.jpa;

import java.util.List;

import org.openkoala.koala.auth.core.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface UserRepository extends PagingAndSortingRepository<User, Long>{
	public List<User> findByName(String name);
	public List<User> findByUserAccount(String userAccount);
}
