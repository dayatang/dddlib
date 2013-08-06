package org.openkoala.koala.deploy.webservice.pojo;

/**
 * WebService的类型，包括put,get,post,delete四种
 * 
 * @author lingen
 * 
 */
public enum WebServiceType {
	PUT {
		public String getAnnotationString() {
			return "PUT";
		}
		public String toString() {
			return "[PUT]";
		}
	},
	GET {
		public String getAnnotationString() {
			return "GET";
		}
		public String toString() {
			return "[GET]";
		}
	},
	DELETE {
		public String getAnnotationString() {
			return "DELETE";
		}
		public String toString() {
			return "[DELETE]";
		}
	},
	POST {
		public String getAnnotationString() {
			return "POST";
		}
		public String toString() {
			return "[POST]";
		}
	};
	
	public abstract String getAnnotationString();
	
	public static WebServiceType getValueByString(String httpMethodString) {
		for (WebServiceType httpMethod : values()) {
			if (httpMethod.name().equals(httpMethodString)) {
				return httpMethod;
			}
		}
		return null;
	}

}
