package org.jwebap.toolkit.bytecode.asm;



import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.asm.AnnotationVisitor;
import org.jwebap.asm.Attribute;
import org.jwebap.asm.ClassVisitor;
import org.jwebap.asm.FieldVisitor;
import org.jwebap.asm.MethodVisitor;


public class MethodCriteriaClassVisitor implements ClassVisitor {

    private static final Log log = LogFactory.getLog(MethodCriteriaClassVisitor.class);
    private final ClassVisitor  injector;
    private String              className;
    private ClassVisitor        defaultClassVisitor;

    public MethodCriteriaClassVisitor(ClassVisitor injector) {
        this.injector = injector;
    }





   
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        className = name.replace('/', '.');

        
        defaultClassVisitor = injector;

        log.debug("not filtering class " + className);
        

        defaultClassVisitor.visit(version, access, name, signature, superName, interfaces);
    }


    
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

       
        return defaultClassVisitor.visitMethod(access, name, desc, signature, exceptions);
        
    }


    public void visitSource(String source, String debug) {
        defaultClassVisitor.visitSource(source, debug);
    }


    public void visitOuterClass(String owner, String name, String desc) {
        defaultClassVisitor.visitOuterClass(owner, name, desc);
    }


    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return defaultClassVisitor.visitAnnotation(desc, visible);
    }


    public void visitAttribute(Attribute attr) {
        defaultClassVisitor.visitAttribute(attr);
    }


    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        defaultClassVisitor.visitInnerClass(name, outerName, innerName, access);
    }


    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        return defaultClassVisitor.visitField(access, name, desc, signature, value);
    }


    public void visitEnd() {

        defaultClassVisitor.visitEnd();

        defaultClassVisitor = null;
        className           = null;
    }
}
