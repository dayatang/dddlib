/*
 * Copyright (c) OpenKoala 2011 All Rights Reserved
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
package org.openkoala.exception.surpport.springaop;

import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.openkoala.exception.base.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类    名：AppExceptionInterceptor.java<br />
 *   
 * 功能描述：应用层异常拦截器	<br />
 *  
 * 创建日期：2012-11-20下午05:38:54  <br />   
 * 
 * 版本信息：v 1.0<br />
 * 
 * 版权信息：Copyright (c) 2011 Csair All Rights Reserved<br />
 * 
 * 作    者：<a href="mailto:jiangwei@openkoala.com">vakin jiang</a><br />
 * 
 * 修改记录： <br />
 * 修 改 者    修改日期     文件版本   修改说明	
 */
public class AppExceptionInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(AppExceptionInterceptor.class);
    

    public Object intercept(ProceedingJoinPoint point) throws Throwable {
        Method method = null;
        Class<? extends Object> target = point.getTarget().getClass();
        Method[] methods = target.getDeclaredMethods();
        for (Method tmp : methods) {
            if (point.getSignature().getName().equals(tmp.getName())) {
                method = tmp;
                break;
            }
        }

        Object o = null;
        try {
            o = point.proceed();
        } catch (Exception e) {
            logger.error("Method["+target.getName() + "." + method.getName()+"]",e);
            throw new BaseException(e);
        }finally{
        	
        }
        return o;
    }
}
