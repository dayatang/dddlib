package org.openkoala.opencis.support;

import java.io.File;

/**
 * 一些公共方法
 * @author zjh
 *
 */
public class CommonUtil {

	/**
	 * 判断路径最后是否包含有斜杠或反斜杠
	 * @param path
	 * @return
	 */
	public static String validatePath(String path){
		if(path.matches(".*[\\\\\\\\|/]$")){
			return path;
		}
		return path + File.separator;
	}
}
