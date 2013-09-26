package org.openkoala.koala.auth.ss3adapter.ehcache;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.ss3adapter.CustomUserDetails;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import com.dayatang.domain.InstanceFactory;

public class CacheUtil {

	private static AuthDataService provider;

	private static com.dayatang.cache.Cache userCache = null;
	private static com.dayatang.cache.Cache resourceCache = null;

	public static AuthDataService getAuthDataService() {
		if (provider == null) {
			provider = InstanceFactory.getInstance(AuthDataService.class);
		}
		return provider;
	}

	public static com.dayatang.cache.Cache getResourceCache() {
		if (resourceCache == null) {
			resourceCache = InstanceFactory.getInstance(com.dayatang.cache.Cache.class, "resource_cache");
		}
		return resourceCache;
	}

	public static com.dayatang.cache.Cache getUserCache() {
		if (userCache == null) {
			userCache = InstanceFactory.getInstance(com.dayatang.cache.Cache.class, "user_cache");
		}
		return userCache;
	}

	public static void refreshUrlAttributes(String url) {
		getResourceCache().remove(url);
		getResourceCache().put(url, getAuthDataService().getAttributes(url));
	}

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

	public static void removeUserFromCache(String user) {
		getUserCache().remove(user);
	}

}
