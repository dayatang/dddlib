package org.openkoala.koala.deploy.curd.generator; 

/**
 * 基本数字包装类型（short、int、long等）
 * @author Ken
 * @since 2013-1-31 下午3:13:10
 *
 */
public enum WrapperType {
	SHORT {
		@Override
		public String toString() {
			return "java.lang.Short";
		}
	},
	INT {
		@Override
		public String toString() {
			return "java.lang.Integer";
		}
	},
	LONG {
		@Override
		public String toString() {
			return "java.lang.Long";
		}
	},
	FLOAT {
		@Override
		public String toString() {
			return "java.lang.Float";
		}
	},
	DOUBLE {
		@Override
		public String toString() {
			return "java.lang.Double";
		}
	}
}
