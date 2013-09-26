package org.jwebap.toolkit.bytecode.asm;

/**
 * 类型
 * 
 * 在字节码生成过程中用于表示一个类
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Mar 2, 2008
 */
public class Type {

	int version;

	int access;

	String name;

	String signature;

	String superName;

	String[] interfaces;

	byte[] byteCode=new byte[0];


	Type(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		this.version = version;

		this.access = access;

		this.name = name;

		this.signature = signature;

		this.superName = superName;

		this.interfaces = interfaces;
	}
	
	public byte[] getByteCode() {
		return byteCode;
	}

	public void setByteCode(byte[] byteCode) {
		this.byteCode = byteCode;
	}
	
	int getAccess() {
		return access;
	}

	void setAccess(int access) {
		this.access = access;
	}

	String[] getInterfaces() {
		return interfaces;
	}

	void setInterfaces(String[] interfaces) {
		this.interfaces = interfaces;
	}

	String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}

	String getSignature() {
		return signature;
	}

	void setSignature(String signature) {
		this.signature = signature;
	}

	String getSuperName() {
		return superName;
	}

	void setSuperName(String superName) {
		this.superName = superName;
	}

	int getVersion() {
		return version;
	}

	void setVersion(int version) {
		this.version = version;
	}
	
	public String toString(){
		return this.name;
	}

}
