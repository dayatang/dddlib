package org.openkoala.bpm.KoalaBPM.infra.common;

public class ConstantUtil {
	
	/** 动态显示列xml元素格式如：KJ_keyId@@@keyName##showOrder##innerVariable##keyValue###KJ_keyId@@@keyName##showOrder##innerVariable##keyValue###... **/
	public static final String XML_ELEMENT_SEPERATOR = "###";
	public static final String XML_ELEMENT_NAME_SEPERATOR = "KJ_";
	public static final String XML_ELEMENT_VALUE_SEPERATOR = "##";
	public static final String XML_ELEMENT_NAME_VALUE_SEPERATOR = "@@@";
	
	public static final String TRUE = "true";
	
	/** 表单内容可读/可写 **/
	public static final String SECURITY_READ = "R";
	public static final String SECURITY_WRITE = "W";
	
	/** 决策分隔符 **/
	public static final String TASK_CHOICE_SEPERATOR = "===";
}
