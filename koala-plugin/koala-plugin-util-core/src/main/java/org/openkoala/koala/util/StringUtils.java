package org.openkoala.koala.util;

public class StringUtils {

	public static String handleSpecialCharactersInName(String name) {
		String[] specialCharacters = new String[]{"-", "_"};
		String result = name;
		
		for (String specialCharacter : specialCharacters) {
			if (result.contains(specialCharacter)) {
				result = result.replaceAll(specialCharacter, "");
			}
		}
		
		return result;
	}
	
	/**
	 * 生成一个符合规范的JAVA类名
	 * @param name
	 * @return
	 */
	public static String generateJavaClass(String name){
		String result = name.toLowerCase();
		String[] specialCharacters = new String[]{"-", "_"};
		for (String specialCharacter : specialCharacters) {
				int index = result.indexOf(specialCharacter);
				while(index!=-1){
					result  = result.substring(0,index)+result.substring(index+1,index+2).toUpperCase()+result.substring(index+2);
					index = result.indexOf(specialCharacter);
				}
		}
		result = result.replaceAll("[^a-zA-Z]", "");
		result = result.substring(0,1).toUpperCase()+result.substring(1);
		return result;
	}
	
	public static String generateJavaVari(String name){
		String result = name.toLowerCase();
		String[] specialCharacters = new String[]{"-", "_"};
		for (String specialCharacter : specialCharacters) {
				int index = result.indexOf(specialCharacter);
				while(index!=-1){
					result  = result.substring(0,index)+result.substring(index+1,index+2).toUpperCase()+result.substring(index+2);
					index = result.indexOf(specialCharacter);
				}
		}
		result = result.replaceAll("[^a-zA-Z]", "");
		result = result.substring(0,1).toLowerCase()+result.substring(1);
		return result;
	}
	
	/**
	 * 生成一个符合规范的JAVA变量名
	 * @param args
	 */
	public static void main(String args[]){
		String result = "abc-ad-ef";
		int index = result.indexOf("-");
		while(index!=-1){
			result  = result.substring(0,index)+result.substring(index+1,index+2).toUpperCase()+result.substring(index+2);
			index = result.indexOf("-");
		}
		System.out.println(result);
	}
	
}
