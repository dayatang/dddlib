package org.openkoala.koala.util; 

import java.lang.reflect.Method;
import java.net.URL;

/**
 * Eclipse中URL解析工具类
 * @author zyb
 * @since 2012-11-15
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class EclipseUrlParseUtils {

	/**
	 * 解析URL
	 * @param url
	 * @return
	 */
	public static URL parseUrl(URL url){
		URL returnURL = null;
		try {
			Class fileLocatorClass = Class.forName("org.eclipse.core.runtime.FileLocator");
			Method resolve = fileLocatorClass.getMethod("resolve",URL.class);
			returnURL = (URL)resolve.invoke(fileLocatorClass, url);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		return returnURL;
	}
}
