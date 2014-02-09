package org.openkoala.koala.auth.ss3adapter;

import java.util.ArrayList;
import java.util.List;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.vo.DefaultUserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dayatang.cache.Cache;
import com.dayatang.domain.InstanceFactory;

/**
 * ClassName:UserDetailManager Function: 查询出用户所具有的权限等信息并进行封装得到UserDetails
 * 
 * @author caoyong
 * @version 1.0, 2012-12-04
 * @since ss31.0
 */
// @Component("userDetailManager")
public class UserDetailManager implements UserDetailsService {
	
	private static Logger logger = LoggerFactory.getLogger(UserDetailManager.class);

	private AuthDataService provider;

	private Cache cache;
	

	private Cache getCache() {
		if (cache == null) {
			cache = InstanceFactory.getInstance(Cache.class, "user_cache");
		}
		return cache;
	}

	/**
	 * 根据用户名取得及权限等信息
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		DefaultUserDetailsImpl user = null;
		try {
			user = (DefaultUserDetailsImpl) provider.loadUserByUseraccount(username);
		} catch (Exception e) {
			logger.error("加载用户失败：{}", e);
		}
		if (user == null) {
			return null;
		}
		List<GrantedAuthority> gAuthoritys = new ArrayList<GrantedAuthority>();
		for (String role : user.getAuthorities()) {
			GrantedAuthorityImpl gai = new GrantedAuthorityImpl(role);
			gAuthoritys.add(gai);
		}

		CustomUserDetails result = new CustomUserDetails(user.getPassword(), user.getUseraccount(), user.isAccountNonExpired(),
				user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled(), gAuthoritys, user.getRealName());
		result.setSuper(user.isSuper());
		getCache().put(username, result);
		return result;
	}

	public AuthDataService getProvider() {
		return provider;
	}

	public void setProvider(AuthDataService provider) {
		this.provider = provider;
	}

}
