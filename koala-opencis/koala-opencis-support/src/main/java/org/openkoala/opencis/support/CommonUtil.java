package org.openkoala.opencis.support;

import java.io.File;
import java.util.List;

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
		if(path.matches(".*[/]$")){
			return path;
		}
		return path + "/";
	}
	
	/**
	 * 检查文件是否存在
	 * @param 文件全路径，包括文件名
	 * @return
	 */
	public static boolean fileExists(String filePath,String fileName){
		return new File(filePath,fileName).exists();
	}
	
	/**
	 * 转换用户列表为一个字符串
	 * @param userList
	 * @return
	 */
	public static String ConvertGroupUserListToString(List<String> userList){
		StringBuffer groupUsersStr = new StringBuffer();
        for (String groupUser : userList) {
            if (groupUsersStr.length() > 0) {
                groupUsersStr.append("," + groupUser);
            } else {
                groupUsersStr.append(groupUser);
            }
        }
        return groupUsersStr.toString();
	}
}
