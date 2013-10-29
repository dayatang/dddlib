package org.openkoala.bpm.processdyna.core;

/**
 * 动态表单类型
 * @author lingen
 *
 */
public enum DynaType {

	/**
	 * 文本框
	 */
	Text("文本框"),
	
	/**
	 * 下拉框
	 */
	DropDown("下拉框"),
	
	/**
	 * 多选框
	 */
	Checkbox("复选框"),
	
	/**
	 * 单选框
	 */
	Radio("单选框"),
	
	/**
	 * 文本域
	 */
	TextArea("文本域"),
	
	/**
	 * 日期
	 */
	Date("日期"),
	
	/**
	 * 日期+时间
	 */
	DateTime("日期+时间"),
	
	/**
	 * 时间
	 */
	Time("时间"),
	
	/**
	 * 密码框
	 */
	Password("密码框"),
	/**
	 * 文件上传
	 */
	File("文件上传");
	
	private final String text;

	public String getText() {
		return text;
	}

	private DynaType(String text) {
		this.text = text;
	}
   
	public static DynaType name2Enum(String name){
		for (DynaType type : DynaType.values()) {
			if(type.name().equalsIgnoreCase(name)) {
                return type;
            }
		}
		return null;
	}
	
}
