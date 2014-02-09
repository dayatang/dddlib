package org.openkoala.koala.annotation.parse;

import java.util.List;

/**
 * 
 * 
 * @description 对象解析的接口
 *  
 * @date：      2013-8-27   
 * 
 * @version    Copyright (c) 2013 Koala All Rights Reserved
 * 
 * @author     lingen.liu  <a href=mailto:lingen.liu@gmail.com">lingen.liu@gmail.com</a>
 */
public interface Parse {

	void initParms(List params,String name,Object fieldVal);
	
	void process() throws Exception;
}
