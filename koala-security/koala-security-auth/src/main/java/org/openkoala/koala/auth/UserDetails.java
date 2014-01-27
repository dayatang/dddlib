package org.openkoala.koala.auth;

import java.io.Serializable;
import java.util.List;

/**
 * 用户信息接口
 * @author zyb <a href="mailto:zhuyuanbiao2013@gmail.com">zhuyuanbiao2013@gmail.com</a>
 * @since Aug 16, 2013 10:05:30 AM
 */
public interface UserDetails extends Serializable {
	
	/**
	 * 获取授权信息
	 * @return
	 */
    public List<String> getAuthorities();
    
    /**
     * 获取密码
     * @return
     */
    public String getPassword();
    
    /**
     * 获取用户账号
     * @return
     */
    public String getUseraccount();
    
    /**
     * 账号是否没有过期
     * @return
     */
    public boolean isAccountNonExpired();
    
    /**
     * 账号是否没有被锁
     * @return
     */
    public boolean isAccountNonLocked();
    
    /**
     * 用户凭证是否没有过期
     * @return
     */
    public boolean isCredentialsNonExpired();
    
    /**
     * 是否禁用
     * @return
     */
    public boolean isEnabled();
    
    /**
     * 是否超级用户
     * @return
     */
    public boolean isSuper();
    
    /**
     * 获取用户邮箱
     * @return
     */
    public String getEmail();
    
    /**
     * 获取真实名字
     * @return
     */
    public String getRealName();
    
}