package org.jwebap.toolkit.bytecode.asm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.asm.ClassAdapter;
import org.jwebap.asm.ClassVisitor;
import org.jwebap.asm.FieldVisitor;
import org.jwebap.asm.MethodVisitor;
import org.jwebap.asm.Opcodes;
import org.jwebap.asm.Type;
import org.jwebap.asm.commons.GeneratorAdapter;
import org.jwebap.asm.commons.Method;

/**
 * 类注入访问者，用于对类进行字节码注入
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date  Mar 22, 2008
 */
public class InjectClassVisitor extends ClassAdapter implements Constants, Opcodes {

    private static final Log log = LogFactory.getLog(InjectClassVisitor.class);
    private int                 handlerCount;
    private Type                classType;
    private GeneratorAdapter    initializer;

    public InjectClassVisitor(ClassVisitor visitor) {
        super(visitor);
    }


    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        handlerCount = 0;
        classType    = Type.getType("L" + name.replace('.', '/') + ";");

        super.visit(version, access, name, signature, superName, interfaces);

        initializer = addInitializer();
    }


    private GeneratorAdapter addInitializer() {

        int              access            = Modifier.PRIVATE_STATIC;
        String           descriptor        = "()V";
        MethodVisitor    initMethodVisitor = super.visitMethod(access, initializeName, descriptor, null, null);
        Method initializeMethod=new Method(initializeName,descriptor);
        GeneratorAdapter initializer       = new GeneratorAdapter(access,initializeMethod,initMethodVisitor);

        initMethodVisitor.visitCode();

        return initializer;
    }


    public void visitEnd() {

        initializer.returnValue();
        initializer.endMethod();
        super.visitEnd();
    }


    private void addMethodHandlerField(String fieldName, String methodName, String descriptor) {

        FieldVisitor handler = super.visitField(Modifier.PRIVATE_STATIC_FINAL, fieldName,
                                                MethodHandler.TYPE.getDescriptor(), null, null);

        handler.visitEnd();
        initializer.push(classType.getClassName());
        initializer.push(methodName);
        initializer.push(descriptor);
        initializer.invokeStatic(HandlerFactory.TYPE, HandlerFactory.getMethodHandler);
        initializer.putStatic(classType, fieldName, MethodHandler.TYPE);
    }

    private void addMethodField(String fieldName, String methodName, String descriptor) {

        FieldVisitor handler = super.visitField(Modifier.PRIVATE_STATIC_FINAL, fieldName,
                                                Type.getDescriptor(java.lang.reflect.Method.class), null, null);

        handler.visitEnd();
        initializer.push(classType.getClassName());
        initializer.push(methodName);
        initializer.push(descriptor);
        initializer.invokeStatic(Type.getType(MethodUtil.class), Method.getMethod(java.lang.reflect.Method.class.getName()+ " findMethod(String, String, String)"));
        initializer.putStatic(classType, fieldName,Type.getType(java.lang.reflect.Method.class));
    }
    
    private void addMethodProxyField(String fieldName, String methodName, String descriptor) {

        FieldVisitor handler = super.visitField(Modifier.PRIVATE_STATIC_FINAL, fieldName,
                                                Type.getDescriptor(java.lang.reflect.Method.class), null, null);

        handler.visitEnd();
        initializer.push(classType.getClassName());
        initializer.push(methodName);
        initializer.push(descriptor);
        initializer.invokeStatic(Type.getType(MethodUtil.class), Method.getMethod(java.lang.reflect.Method.class.getName()+ " findMethod(String, String, String)"));
        initializer.putStatic(classType, fieldName,Type.getType(java.lang.reflect.Method.class));
    }

    public void pushThis(GeneratorAdapter adapter, boolean isStatic) {

        if (isStatic)
        {
            adapter.push("test");
        }
        else
        {
            adapter.loadThis();
        }
    }


    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature,
                                     String[] exceptions) {

        if (name.equals("<clinit>") || name.equals("<init>") || Modifier.isAbstract(access)
                || Modifier.isNative(access))
        {
            return super.visitMethod(access, name, descriptor, signature, exceptions);
        }

        int    index            = (handlerCount++);
        String handlerFieldName = ASMInjectorStrategy.HANDLER_PREFIX + index;
        String methodFieldName = ASMInjectorStrategy.METHOD_PREFIX + index;
        String methodProxyFieldName = ASMInjectorStrategy.METHOD_PROXY_PREFIX + index;
        String targetMethodName = name + ASMInjectorStrategy.METHOD_POSTFIX;

        addMethodHandlerField(handlerFieldName, name, descriptor);
        addMethodField(methodFieldName, name, descriptor);
        addMethodProxyField(methodProxyFieldName, targetMethodName, descriptor);
        //Proxy Method
        {
            Method             method  = new Method(name, descriptor);
            MethodVisitor      mv      = super.visitMethod(access, name, descriptor, signature, exceptions);
            ProxyMethodVisitor visitor = new ProxyMethodVisitor(access, method, mv, classType, targetMethodName,
                                             handlerFieldName,methodFieldName,methodProxyFieldName);

            visitor.visitCode();
            visitor.visitEnd();
        }

        //Target Method
        return super.visitMethod(Modifier.makePrivate(access), targetMethodName, descriptor, signature, exceptions);
    }
}
