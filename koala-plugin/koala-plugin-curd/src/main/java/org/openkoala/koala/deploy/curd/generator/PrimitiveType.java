package org.openkoala.koala.deploy.curd.generator;

/**
 * 
 * 类    名：PrimitiveType.java
 *   
 * 功能描述：数字原始类型枚举
 *  
 * 创建日期：2013-4-18上午10:31:47     
 * 
 * 版本信息：
 * 
 * 版权信息：Copyright (c) 2013 Koala All Rights Reserved
 * 
 * 作    者：zyb
 * 
 * 修改记录： 
 * 修 改 者    修改日期     文件版本   修改说明
 */
public enum PrimitiveType {
	
	SHORT {
		@Override
		public String toString() {
			return "short";
		}

		@Override
		public String convertToWrapper() {
			return "Short";
		}
	},
	INT {
		@Override
		public String toString() {
			return "int";
		}

		@Override
		public String convertToWrapper() {
			return "Integer";
		}
	},
	LONG {
		@Override
		public String toString() {
			return "long";
		}

		@Override
		public String convertToWrapper() {
			return "Long";
		}
	},
	FLOAT {
		@Override
		public String toString() {
			return "float";
		}

		@Override
		public String convertToWrapper() {
			return "Float";
		}
	},
	DOUBLE {
		@Override
		public String toString() {
			return "double";
		}

		@Override
		public String convertToWrapper() {
			return "Double";
		}
	};
	
	/**
	 * 转成包装类型
	 * @return
	 */
	public abstract String convertToWrapper();
}
