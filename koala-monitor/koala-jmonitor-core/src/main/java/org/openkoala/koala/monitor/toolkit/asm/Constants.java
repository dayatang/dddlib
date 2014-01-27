package org.openkoala.koala.monitor.toolkit.asm;



import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;




public interface Constants {

    public static String initializeName = "$clinit" + ASMInjectorStrategy.METHOD_POSTFIX;
    public static Method initialize     = Method.getMethod("void " + initializeName + "()");
    public static String classInitName  = "<clinit>";
    public static String classInitDesc  = "()V";
    public static Method classInit      = Method.getMethod("void " + classInitName + "()");





    public interface HandlerFactory {

        public static Class  CLASS            = StaticHandleFactory.class;
        public static Type   TYPE             = Type.getType(CLASS);
        public static Method getMethodHandler =
            Method.getMethod(MethodInjectHandler.class.getName()
                             + " getMethodHandler(String, String, String)");
    }

    public interface MethodHandler {

        public static Class  CLASS          = MethodInjectHandler.class;
        public static Type   TYPE           = Type.getType(CLASS);
        public static Method invoke  = Method.getMethod("Object invoke(Object,java.lang.reflect.Method,java.lang.reflect.Method,Object[])");
    
    }

}
