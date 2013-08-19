package org.jwebap.toolkit.bytecode.asm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.asm.AnnotationVisitor;
import org.jwebap.asm.Attribute;
import org.jwebap.asm.ClassVisitor;
import org.jwebap.asm.FieldVisitor;
import org.jwebap.asm.MethodVisitor;

/**
 * 判断是不是接口，是接口的话什么不作修改
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Oct 14, 2007
 */
public class ASMClassVisitor implements ClassVisitor {

	private static final Log log = LogFactory.getLog(ASMClassVisitor.class);

	private ClassVisitor interfaceClassVisitor;

	private ClassVisitor concreteClassVisitor;

	private ClassVisitor currentVisitor;

	private Type clazz = null;

	public ASMClassVisitor(ClassVisitor interfaceClassVisitor,
			ClassVisitor concreteClassVisitor) {
		this.interfaceClassVisitor = interfaceClassVisitor;
		this.concreteClassVisitor = concreteClassVisitor;
	}

	public Type getType() {
		return clazz;
	}

	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {

		clazz = new Type(version, access, name, signature, superName,
				interfaces);

		if (Modifier.isInterface(access)) {
			currentVisitor = interfaceClassVisitor;
		} else {
			currentVisitor = concreteClassVisitor;

		}

		currentVisitor.visit(version, access, name, signature, superName,
				interfaces);
	}

	public void visitSource(String source, String debug) {
		currentVisitor.visitSource(source, debug);
	}

	public void visitOuterClass(String owner, String name, String desc) {
		currentVisitor.visitOuterClass(owner, name, desc);
	}

	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return currentVisitor.visitAnnotation(desc, visible);
	}

	public void visitAttribute(Attribute attr) {
		currentVisitor.visitAttribute(attr);
	}

	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		currentVisitor.visitInnerClass(name, outerName, innerName, access);
	}

	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		return currentVisitor.visitField(access, name, desc, signature, value);
	}

	public MethodVisitor visitMethod(int access, String name, String desc,
			String signature, String[] exceptions) {
		return currentVisitor.visitMethod(access, name, desc, signature,
				exceptions);
	}

	public void visitEnd() {

		currentVisitor.visitEnd();

		currentVisitor = null;
	}
}
