package org.openkoala.koala.deploy.webservice.pojo;

/**
 * 
 * 类    名：ParamType.java
 *   
 * 功能描述：RESTful WebService 参数传输方式枚举	
 *  
 * 创建日期：2013-5-30下午2:22:30     
 * 
 * 作    者：xmfang(xinmin.fang@gmail.com)
 */
public enum ParamType {

	PATH_PARAM {
		public String getParamTransferType() {
			return "PathParam";
		}
	},
	QUERY_PARAM {
		public String getParamTransferType() {
			return "QueryParam";
		}
	},
	FORM_PARAM {
		public String getParamTransferType() {
			return "FormParam";
		}
	},
	HEADER_PARAM {
		public String getParamTransferType() {
			return "HeaderParam";
		}
	},
	MATRIX_PARAM {
		public String getParamTransferType() {
			return "MatrixParam";
		}
	},
	COOKIE_PARAM {
		public String getParamTransferType() {
			return "CookieParam";
		}
	};
	
	public abstract String getParamTransferType();
	
	public static ParamType getValueByString(String paramTypeString) {
		for (ParamType paramType : values()) {
			if (paramType.name().equals(paramTypeString)) {
				return paramType;
			}
		}
		return null;
	}
	
}
