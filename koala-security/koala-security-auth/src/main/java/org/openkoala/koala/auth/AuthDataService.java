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
package org.openkoala.koala.auth;

import java.util.List;
import java.util.Map;

/**
 * 类    名：AuthDataService.java
 *   
 * 功能描述：鉴权的核心接口	
 *  
 * 创建日期：2013-1-25下午3:10:58     
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
public interface AuthDataService {
    
    
    public List<String> getAttributes(String res);
    
    /**
     * 传入用户名，获取当前用户
     * @param useraccount
     * @return
     */
    public UserDetails loadUserByUseraccount(String useraccount);
    
    
    /**
     * 查询指定用户的角色
     * @param accountName
     * @return
     */
    public List<String> getUserRoles(String accountName);
    
    
    /**
     * 查询资源-角色对应列表
     * @return
     */
    public Map<String, List<String>> getAllReourceAndRoles();
    
    
}
