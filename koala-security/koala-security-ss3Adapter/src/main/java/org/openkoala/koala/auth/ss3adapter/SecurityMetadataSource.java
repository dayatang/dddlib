package org.openkoala.koala.auth.ss3adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.LogFactory;
import org.openkoala.koala.auth.AuthDataService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntUrlPathMatcher;
import org.springframework.security.web.util.UrlMatcher;

import com.dayatang.cache.Cache;
import com.dayatang.domain.InstanceFactory;

/**
 * ClassName:SecurityMetadataSource Function: 资源与权限建立管理
 * 在服务器启动时就加载所有访问URL所需的权限，存入resourceMap集合中。
 * Spring在设置完一个bean所有的合作者后，会检查bean是否实现了InitializingBean接口，
 * 如果实现就调用bean的afterPropertiesSet方法。 但这样便造成bean和spring的耦合， 最好用init-method
 * 
 * @author caoyong
 * @version 1.0, 2012-12-04
 * @since ss31.0
 */
// @Component("securityMetadataSource")
public class SecurityMetadataSource implements FilterInvocationSecurityMetadataSource, InitializingBean {
	org.apache.commons.logging.Log log = LogFactory.getLog(SecurityMetadataSource.class);

	private UrlMatcher urlMatcher = new AntUrlPathMatcher();

	private Cache resourceCache;

	private Cache userCache;

	private AuthDataService provider;

	private Cache getResourceCache() {
		if (resourceCache == null) {
			resourceCache = InstanceFactory.getInstance(Cache.class, "resource_cache");
		}
		return resourceCache;
	}

	private Cache getUserCache() {
		if (userCache == null) {
			userCache = InstanceFactory.getInstance(Cache.class, "user_cache");
		}
		return userCache;
	}

	@SuppressWarnings("unchecked")
	public boolean getResAuthByUseraccount(String userAccount, String res) {
		List<String> grantRoles = null;
		if (getResourceCache().isKeyInCache(res)) {
			grantRoles = (List<String>) getResourceCache().get(res);
		}
		CustomUserDetails user = (CustomUserDetails) this.getUserCache().get(userAccount);
		if (user != null && grantRoles != null) {
			Collection<GrantedAuthority> authorities = user.getAuthorities();
			for (GrantedAuthority grant : authorities) {
				String role = grant.getAuthority();
				if (grantRoles.contains(role)) {
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * 加载所有资源
	 * 
	 * @throws Exception
	 */
	private void loadResource() throws Exception {
		// 查询出所有资源
		if (resourceCache == null) {
			Map<String, List<String>> allRes = provider.getAllReourceAndRoles();
			Set<String> urls = allRes.keySet();
			for (String url : urls) {
				getResourceCache().put(url, allRes.get(url));
			}
		}
	}

	/**
	 * 构造方法中建立请求url(key)与权限(value)的关系集合
	 */
	public void afterPropertiesSet() throws Exception {

	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;

	}

	/**
	 * 根据请求的url从集合中查询出所需权限
	 */
	@SuppressWarnings("unchecked")
	public Collection<ConfigAttribute> getAttributes(Object arg0) throws IllegalArgumentException {
		try {
			loadResource();
		} catch (Exception e) {
			e.printStackTrace();
		}

		String url = ((FilterInvocation) arg0).getRequestUrl();
		int position = url.indexOf("?");
		if (-1 != position) {
			url = url.substring(0, position);
		}

		if (getResourceCache().isKeyInCache(url.substring(url.indexOf("/") + 1))) {
			List<String> roles = (List<String>)getResourceCache().get(url.substring(url.indexOf("/") + 1));
			Collection<ConfigAttribute> attris = new ArrayList<ConfigAttribute>();
			for (final String role : roles){
				attris.add(new ConfigAttribute(){
					private static final long serialVersionUID = -2841059463182100139L;

					public String getAttribute() {
						return role;
					}
				});
			}
			return attris;
		}
		
		return null;
	}

	/**
	 * 返回false则报错 SecurityMetadataSource does not support secure object class:
	 * class org.springframework.security.web.FilterInvocation
	 */
	public boolean supports(Class<?> arg0) {
		return true;
	}

	public UrlMatcher getUrlMatcher() {
		return urlMatcher;
	}

	public void setUrlMatcher(UrlMatcher urlMatcher) {
		this.urlMatcher = urlMatcher;
	}

	public SecurityMetadataSource() {

	}

	public AuthDataService getProvider() {
		return provider;
	}

	public void setProvider(AuthDataService provider) {
		this.provider = provider;
	}

}
