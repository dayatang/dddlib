package org.openkoala.koala.deploy.webservice.pojo;

/**
 * 
 * 类    名：MediaType.java
 *   
 * 功能描述：RESTful WebService 数据传输格式枚举
 *  
 * 创建日期：2013-5-30下午2:28:46     
 * 
 * 作    者：xmfang(xinmin.fang@gmail.com)
 * 
 */
public enum MediaType {

	APPLICATION_XML,
	APPLICATION_JSON,
	TEXT_XML,
	TEXT_HTML,
	APPLICATION_FORM_URLENCODED,
	MULTIPART_FORM_DATA;
	
	
	public static MediaType getValueByString(String mediaTypeString) {
		for (MediaType mediaType : values()) {
			if (mediaType.name().equals(mediaTypeString)) {
				return mediaType;
			}
		}
		return null;
	}

	
}
