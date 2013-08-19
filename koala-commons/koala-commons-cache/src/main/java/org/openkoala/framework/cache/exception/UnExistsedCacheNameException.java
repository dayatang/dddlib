package org.openkoala.framework.cache.exception;

/**
 * 、
* @Description: 表明指定name在缓存配置文件中不存在
* @author lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a> 
* @date 2013-8-16 下午10:54:28
 */
public class UnExistsedCacheNameException extends RuntimeException {
	
	public UnExistsedCacheNameException(){
		super("指定的缓存不存在");
	}

}
