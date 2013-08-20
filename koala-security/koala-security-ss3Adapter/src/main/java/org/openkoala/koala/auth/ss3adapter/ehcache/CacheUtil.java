package org.openkoala.koala.auth.ss3adapter.ehcache;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import com.dayatang.domain.InstanceFactory;

/**
 * 缓存工具类
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 16, 2013 9:55:29 AM
 */
public class CacheUtil {

	private static AuthDataService provider;

	private static com.dayatang.cache.Cache userCache = null;
	private static com.dayatang.cache.Cache resourceCache = null;
	
	private CacheUtil() {
		
	}

	public static AuthDataService getAuthDataService() {
		if (provider == null) {
			provider = InstanceFactory.getInstance(AuthDataService.class);
		}
		return provider;
	}

	/**
	 * 获取资源缓存信息
	 * @return
	 */
	public static com.dayatang.cache.Cache getResourceCache() {
		if (resourceCache == null) {
			resourceCache = InstanceFactory.getInstance(com.dayatang.cache.Cache.class, "resource_cache");
		}
		return resourceCache;
	}

	/**
	 * 获取用户缓存信息
	 * @return
	 */
	public static com.dayatang.cache.Cache getUserCache() {
		if (userCache == null) {
			userCache = InstanceFactory.getInstance(com.dayatang.cache.Cache.class, "user_cache");
		}
		return userCache;
	}

	/**
	 * 刷新资源缓存
	 * @param url
	 */
	public static void refreshUrlAttributes(String url) {
		getResourceCache().remove(url);
		getResourceCache().put(url, getAuthDataService().getAttributes(url));
	}

	/**
	 * 刷新用户授权信息
	 * @param user
	 */
	public static void refreshUserAttributes(String user) {
		if (!getUserCache().isKeyInCache(user)) {
			return;
		}
		CustomUserDetails cd = (CustomUserDetails) getUserCache().get(user);
		cd.getAuthorities().clear();
		for (String role : getAuthDataService().loadUserByUseraccount(user).getAuthorities()) {
			GrantedAuthorityImpl gai = new GrantedAuthorityImpl(role);
			cd.getAuthorities().add(gai);
		}
	}

	/**
	 * 将用户信息从缓存中删除
	 * @param user
	 */
	public static void removeUserFromCache(String user) {
		getUserCache().remove(user);
	}

}
