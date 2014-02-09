package org.openkoala.koala.deploy.curd.generator; 

import org.apache.velocity.VelocityContext;

/**
 * 
 * 类    名：VelocityContextUtils.java
 *   
 * 功能描述：VelocityContext工具类，保证每次拿到的都是同一个VelocityContext对象
 *  
 * 创建日期：2013-1-25上午9:55:00     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：Ken (rejoy2008@126.com)
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public class VelocityContextUtils {
    
    private static VelocityContext context;
    
    private VelocityContextUtils() {
    	
    }
    
    public synchronized static VelocityContext getVelocityContext() {
        if (context == null) {
            context = new VelocityContext();
        }
        return context;
    }
}
