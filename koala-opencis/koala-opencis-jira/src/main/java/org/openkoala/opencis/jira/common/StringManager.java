package org.openkoala.opencis.jira.common;

import org.apache.commons.lang.StringUtils;

public class StringManager {
	
	public static boolean isEmpty(String str){
		if(StringUtils.isEmpty(str) || str.trim().length()==0){
			return true;
		}else{
			return false;
		}
	}
}
