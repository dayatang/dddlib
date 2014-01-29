package org.openkoala.koala.auth.ss3adapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.vo.DefaultUserDetailsImpl;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

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

	private Cache resourceCache;

	private Cache userCache;

	private AuthDataService provider;
	
	private static final Logger LOGGER = Logger.getLogger("SecurityMetadataSource");

	/**
	 * 获取资源缓存
	 * @return
	 */
	private Cache getResourceCache() {
		if (resourceCache == null) {
			resourceCache = InstanceFactory.getInstance(Cache.class, "resource_cache");
		}
		return resourceCache;
	}

	/**
	 * 获取用户缓存
	 * @return
	 */
	private Cache getUserCache() {
		if (userCache == null) {
			userCache = InstanceFactory.getInstance(Cache.class, "user_cache");
		}
		return userCache;
	}
	
	

	/**
	 * 根据用户账号获取资源授权信息
	 * @param userAccount
	 * @param res
	 * @return
	 */
	public boolean getResAuthByUseraccount(String userAccount, String res) {
		List<String> grantRoles = getGrantRoles(res);
		CustomUserDetails user =  getUserInfo(userAccount);
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
	
	@SuppressWarnings("unchecked")
	private List<String> getGrantRoles(String res) {
		List<String> roles = new ArrayList<String>();
		roles = (List<String>) getResourceCache().get(res);
		if(roles == null || roles.isEmpty()) {
			roles = provider.getAttributes(res);
		}
		return roles;
	}
	
	
	/**
	 * 获取用户信息
	 * @param userAccount
	 * @return
	 */
	private CustomUserDetails getUserInfo(String userAccount){
		CustomUserDetails userInfo = (CustomUserDetails)getUserCache().get(userAccount);
		if(userInfo==null){
			DefaultUserDetailsImpl user = (DefaultUserDetailsImpl) provider.loadUserByUseraccount(userAccount);
			List<GrantedAuthority> gAuthoritys = new ArrayList<GrantedAuthority>();
			for (String role : user.getAuthorities()) {
				GrantedAuthorityImpl gai = new GrantedAuthorityImpl(role);
				gAuthoritys.add(gai);
			}
			userInfo = new CustomUserDetails(user.getPassword(), user.getUseraccount(), user.isAccountNonExpired(),
					user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled(), gAuthoritys, user.getRealName());
			userInfo.setSuper(user.isSuper());
			getUserCache().put(userAccount, userInfo);
		}
		
		return userInfo;
	}

	/**
	 * 加载所有资源
	 * 
	 * @throws Exception
	 */
	private void loadResource() {
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
	public void afterPropertiesSet() {

	}

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;

	}

	/**
	 * 根据请求的url从集合中查询出所需权限
	 */
	@SuppressWarnings("unchecked")
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		try {
			loadResource();
		} catch (Exception e) {
			LOGGER.info(e.getMessage());
		}

		String url = ((FilterInvocation) object).getRequestUrl();
		int position = url.indexOf('?');
		if (-1 != position) {
			url = url.substring(0, position);
		}

		if (getResourceCache().isKeyInCache(url.substring(url.indexOf('/') + 1))) {
			List<String> roles = (List<String>)getResourceCache().get(url.substring(url.indexOf('/') + 1));
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

	public SecurityMetadataSource() {

	}

	public AuthDataService getProvider() {
		return provider;
	}

	public void setProvider(AuthDataService provider) {
		this.provider = provider;
	}

}
