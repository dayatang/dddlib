package org.openkoala.jbpm.core;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;

import com.dayatang.domain.AbstractEntity;


@Entity
public class KoalaJbpmVariable extends AbstractEntity {

	
	private static final long serialVersionUID = -3979483109001226826L;
	
	public static final String KOALA_GLOBAL = "&KOALA_GLOBAL";
	
	public static final String KOALA_PACKAGE = "&KOALA_PACKAGE_";
	
	public static final String KOALA_PROCESS = "&KOALA_PROCESS_";

	/*
	 * 变量名 
	 */
	@Column(name = "var_key")
	private String key;
	
	/**
	 * 变量值 
	 */
	@Column(name = "var_value")
	private String value;
	
	/**
	 * 变量类型
	 * String 默认
	 * int 
	 * long
	 * double
	 * 
	 */
	@Column(name = "var_type")
	private String type;
	
	/**
	 * 全局及包名
	 * 全局 -- &KOALA_ALL
	 * 包名变量 -- &KOALA_PACKAGE
	 * 流程变量，在流程中定义
	 * 变量的范围
	 */
	@Column(name = "var_scope")
	private String scope;
	
	
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}
	
	public static void setProcessVariable(String processId,String key,String value,String type){
		String scopeValue = type.toUpperCase();
		if("STRING".equals(scopeValue) || "BOOLEAN".equals(scopeValue) || "INT".equals(scopeValue) || "LONG".equals(scopeValue) || "DOUBLE".equals(scopeValue)){
			List<KoalaJbpmVariable> variables = KoalaJbpmVariable.getRepository().find("from KoalaJbpmVariable k where k.scope = '"+KOALA_PROCESS+processId+"' and k.key = ? ",new Object[]{key}, KoalaJbpmVariable.class);
			
			if(variables!=null && variables.size()>0){
				for(KoalaJbpmVariable variable:variables){
					variable.setValue(value);
					variable.save();
				}
			}else{
				KoalaJbpmVariable variable = new KoalaJbpmVariable();
				variable.setKey(key);
				variable.setScope(KOALA_PROCESS+processId);
				variable.setType(type);
				variable.setValue(value);
				variable.save();
			}
		}
		else{
			throw new RuntimeException("类型"+type+"为不支持的类型！");
		}
	}
	
	
	public static void removeProcessVariable(String processId,String key){
		KoalaJbpmVariable variable = KoalaJbpmVariable.getRepository().getSingleResult("from KoalaJbpmVariable k where k.scope = '"+KOALA_PROCESS+processId+"' and k.key = ? ",new Object[]{key}, KoalaJbpmVariable.class);
	    if(variable!=null){
	    	variable.remove();
	    }
	}
	
	
	
	public static void setGlobalVariable(String key,String value,String type){
		String scopeValue = type.toUpperCase();
		if("STRING".equals(scopeValue) || "BOOLEAN".equals(scopeValue) || "INT".equals(scopeValue) || "LONG".equals(scopeValue) || "DOUBLE".equals(scopeValue)){
			List<KoalaJbpmVariable> variables = KoalaJbpmVariable.getRepository().find("from KoalaJbpmVariable k where k.scope = '"+KOALA_GLOBAL+"' and k.key = ? ",new Object[]{key}, KoalaJbpmVariable.class);
			if(variables!=null && variables.size()>0){
				for(KoalaJbpmVariable variable:variables){
					variable.setValue(value);
					variable.save();
				}
			}else{
				KoalaJbpmVariable variable = new KoalaJbpmVariable();
				variable.setKey(key);
				variable.setScope(KOALA_GLOBAL);
				variable.setType(type);
				variable.setValue(value);
				variable.save();
			}
		}
		else{
			throw new RuntimeException("类型"+type+"为不支持的类型！");
		}
	}
	
	public static void removeGlobalVariable(String key){
		KoalaJbpmVariable variable = KoalaJbpmVariable.getRepository().getSingleResult("from KoalaJbpmVariable k where k.scope = '"+KOALA_GLOBAL+"' and k.key = ? ",new Object[]{key}, KoalaJbpmVariable.class);
	    if(variable!=null){
	    	variable.remove();
	    }
	}
	
	public static boolean isVariableExists(String scope,String key){
		List<KoalaJbpmVariable> variables = KoalaJbpmVariable.getRepository().find("from KoalaJbpmVariable k where k.scope = ? and k.key = ? ",new Object[]{scope,key}, KoalaJbpmVariable.class);
		if(variables.size()>0)return true;
		return false;
	}
	
	public static void setPackageVariable(String packageName,String key,String value,String type){
		String scopeValue = type.toUpperCase();
		if("STRING".equals(scopeValue) || "BOOLEAN".equals(scopeValue) || "INT".equals(scopeValue) || "LONG".equals(scopeValue) || "DOUBLE".equals(scopeValue)){
			List<KoalaJbpmVariable> variables = KoalaJbpmVariable.getRepository().find("from KoalaJbpmVariable k where k.scope = '"+KOALA_PACKAGE+packageName+"' and k.key = ? ",new Object[]{key}, KoalaJbpmVariable.class);
			if(variables!=null && variables.size()>0){
				for(KoalaJbpmVariable variable:variables){
					variable.setValue(value);
					variable.save();
				}
			}else{
				KoalaJbpmVariable variable = new KoalaJbpmVariable();
				variable.setKey(key);
				variable.setScope(KOALA_PACKAGE+packageName);
				variable.setType(type);
				variable.setValue(value);
				variable.save();
			}
		}
		else{
			throw new RuntimeException("类型"+type+"为不支持的类型！");
		}
	}
	
	public Object getObjValue(){
		if("STRING".equals(this.type.toUpperCase())){
			return value;
		}
		if("INT".equals(type.toUpperCase())){
			return Integer.parseInt(value);
		}
		if("LONG".equals(type.toUpperCase())){
			return Long.parseLong(value);
		}
		if("DOUBLE".equals(type.toUpperCase())){
			return Double.parseDouble(value);
		}
		if("BOOLEAN".equals(type.toUpperCase())){
			return Boolean.parseBoolean(value);
		}
		return null;
	}
	
	public static void removePackageVariable(String packageName,String key){
		KoalaJbpmVariable variable = KoalaJbpmVariable.getRepository().getSingleResult("from KoalaJbpmVariable k where k.scope = '"+KOALA_PACKAGE+packageName+"' and k.key = ? ",new Object[]{key}, KoalaJbpmVariable.class);
	    if(variable!=null){
	    	variable.remove();
	    }
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		result = prime * result + ((scope == null) ? 0 : scope.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KoalaJbpmVariable other = (KoalaJbpmVariable) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		if (scope == null) {
			if (other.scope != null)
				return false;
		} else if (!scope.equals(other.scope))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KoalaJbpmVariable [key=" + key + ", value=" + value + ", type="
				+ type + ", scope=" + scope + "]";
	}

}
