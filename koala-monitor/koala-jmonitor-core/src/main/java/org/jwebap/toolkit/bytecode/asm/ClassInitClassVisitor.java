package org.jwebap.toolkit.bytecode.asm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.asm.ClassAdapter;
import org.jwebap.asm.ClassVisitor;
import org.jwebap.asm.MethodVisitor;
import org.jwebap.asm.Opcodes;

/**
 * 它会在class的初始化方法里面生成如下代码:
 * <p>
 * 	static <clinit>(){
 * 		<initializeName>();
 * 	}
 * <initializeName>:框架生成的初始化方法，用于初始化inject class产生的一些静态变量
 * </p>
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  Oct 14, 2007
 */
public class ClassInitClassVisitor extends ClassAdapter implements Constants, Opcodes {

    private static final Log log = LogFactory.getLog(ClassInitClassVisitor.class);
    private boolean             clinitVisited = false;
    private String              className;

    public ClassInitClassVisitor(final ClassVisitor cv) {
        super(cv);
    }


    public void visit(final int version, final int access, final String name, final String signature,
                      final String superName, final String[] interfaces) {

        clinitVisited = false;
        className     = name;

        super.visit(version, access, name, signature, superName, interfaces);
    }


    public MethodVisitor visitMethod(final int access, final String name, final String desc, final String signature,
                                     final String[] exceptions) {

        MethodVisitor visitor = super.visitMethod(access, name, desc, signature, exceptions);

        if (classInitName.equals(name))
        {
            clinitVisited = true;
            visitor       = new ClassInitMethodVisitor(className, visitor);
        }

        return visitor;
    }


    public void visitEnd() {

        if (!clinitVisited)
        {
            MethodVisitor clinit = visitMethod(Modifier.PRIVATE_STATIC, classInitName, classInitDesc, null, null);

            clinit.visitCode();
            clinit.visitInsn(RETURN);
            clinit.visitEnd();
        }

        super.visitEnd();
    }
}
