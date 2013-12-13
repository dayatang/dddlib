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
package org.openkoala.koala.auth.impl.ejb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.Remote;
import javax.ejb.Stateless;

import org.openkoala.koala.auth.AuthDataService;
import org.openkoala.koala.auth.UserDetails;
import org.openkoala.koala.auth.vo.JdbcCustomUserDetails;

/**
 * 类    名：JDBCAuthDataService.java
 *   
 * 功能描述：具体功能做描述。	
 *  
 * 创建日期：2013-1-25下午4:04:04     
 * 
 * 版本信息：
 * ˙
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：lingen(lingen.liu@gmail.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明	
 */
@Remote()
@Stateless
public class EJBAuthDataService implements AuthDataService {
   
    private JdbcSecurityConfig config = new JdbcSecurityConfig();
    
    private static final Logger LOGGER = Logger.getLogger("EJBAuthDataService");

    /**
     * 根据用户名，查询出用户
     */
    public UserDetails loadUserByUseraccount(String useraccount) {
        JdbcManager dbmanager = new JdbcManager(config);
        JdbcCustomUserDetails sd = null;
        if (config.getUseAdmain().equals("true") && config.getAdminAccount().equals(useraccount)) {
        	PasswordEncoder encoder = new PasswordEncoder("", "MD5");
            sd = new JdbcCustomUserDetails();
            sd.setUseraccount(config.getAdminAccount());
            sd.setPassword(encoder.encode(config.getAdminPass()));
            sd.setDescription(config.getAdminRealName());
            sd.setEmail("administrator@openkoala.com");
            sd.setRealName(config.getAdminRealName());
            sd.setAuthorities(getUserRoles(config.getAdminAccount()));
            sd.setSuper(true);
            return sd;
        } else {
            ResultSet rs = dbmanager.getUser(useraccount);
            try {
                while (rs.next()) {
                    sd = new JdbcCustomUserDetails();
                    sd.setUseraccount(rs.getString("USERACCOUNT"));
                    sd.setPassword(rs.getString("PASSWORD"));
                    sd.setDescription(rs.getString("DESCRIPTION"));
                    sd.setEmail(rs.getString("EMAIL"));
                    sd.setRealName(rs.getString("REAL_NAME"));
                    sd.setAuthorities(getUserRoles(rs.getString("USERACCOUNT")));
                }
                return sd;
            } catch (SQLException e) {
                LOGGER.info(e.getMessage());
                return null;
            } finally {
                try {
                    rs.close();
                    dbmanager.destroy();
                } catch (SQLException e) {
                    LOGGER.info(e.getMessage());
                }
            }
        }
    }

    public JdbcSecurityConfig getConfig() {
        return config;
    }

    public void setConfig(JdbcSecurityConfig config) {
        this.config = config;
    }

    /*
     *@see org.openkoala.koala.auth.AuthDataService#getUserRoles(java.lang.String)
     */
    public List<String> getUserRoles(String accountName) {
        JdbcManager dbmanager = new JdbcManager(config);
        List<String> ls = new ArrayList<String>();
        if (accountName.equals(config.getAdminAccount())) {
            ResultSet rs = dbmanager.getAllAuth();
            try {
                while (rs.next()) {
                    String roleName = rs.getString("ROLE_NAME");
                    ls.add(roleName);
                }
            } catch (SQLException e) {
                LOGGER.info(e.getMessage());
                return null;
            } finally {
                try {
                    rs.close();
                    dbmanager.destroy();
                } catch (SQLException e) {
                    LOGGER.info(e.getMessage());
                }
            }
            return ls;
        } else {
            ResultSet rs = dbmanager.getUserAuth(accountName);
            try {
                while (rs.next()) {
                    String roleName = rs.getString("ROLE_NAME");
                    ls.add(roleName);
                }
            } catch (SQLException e) {
                LOGGER.info(e.getMessage());
                return null;
            } finally {
                try {
                    rs.close();
                    dbmanager.destroy();
                } catch (SQLException e) {
                    LOGGER.info(e.getMessage());
                }
            }
            return ls;
        }
    }

    
    /*
     *@see org.openkoala.koala.auth.AuthDataService#getAllReourceAndRoles()
     */
    public Map<String, List<String>> getAllReourceAndRoles() {
    	Map<String, List<String>> result = new HashMap<String, List<String>>();
        JdbcManager resManager = new JdbcManager(this.config);
        ResultSet rs = resManager.getAllRes();
        try {
			while (rs.next()) {
				Set<String> roles = new HashSet<String>();
				roles.add(rs.getString("role_name"));
				if (result.containsKey(rs.getString("identifier"))) {
					result.get(rs.getString("identifier")).addAll(roles);
				} else {
					result.put(rs.getString("identifier"), new ArrayList<String>(roles));
				}
			}
        } catch (SQLException e) {
            LOGGER.info(e.getMessage());
            return null;
        }finally
        {
            try {
                rs.close();
                resManager.destroy();
            } catch (SQLException e) {
                LOGGER.info(e.getMessage());
            }
        }
        return result;
    }
    
    /**
     * 传入一个资源，返回这个资源的所有角色
     * @param res
     * @return
     */
    public List<String> getAttributes(String res) {
    	return getAllReourceAndRoles().get(res);
    }
}