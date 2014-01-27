package org.openkoala.organisation.domain;

/**
 * 性别枚举
 * @author xmfang
 *
 */
public enum Gender {

	MALE{
		@Override
		public String getLabel() {
			return "男";
		}
	},
	FEMALE{
		@Override
		public String getLabel() {
			return "女";
		}
	};
	
	public abstract String getLabel();
	
	public static Gender getByLabel(String label) {
		if ("男".equals(label)) {
			return Gender.MALE;
		}
		if ("女".equals(label)) {
			return Gender.FEMALE;
		}
		return null;
	}
}
