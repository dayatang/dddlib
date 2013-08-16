package org.jwebap.toolkit.bytecode.asm;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jwebap.asm.Label;
import org.jwebap.asm.MethodVisitor;
import org.jwebap.asm.Opcodes;
import org.jwebap.asm.Type;
import org.jwebap.asm.commons.GeneratorAdapter;
import org.jwebap.asm.commons.Method;

/**
 * 生成代理方法，把原方法私有化，生成新方法如下：
 * <p>
 * <access> <methodName>(){
 * 
 * if(handle==null){ return <proxyMethodName>(); }else{ return
 * handle.invoke(this,method,proxyMethod,args[]); } }
 * 
 * private <proxyMethodName>(){ //原方法代码 }
 * </p>
 * 
 * @author leadyu(yu-lead@163.com)
 * @since Jwebap 0.5
 * @date Oct 20, 2007
 */
public class ProxyMethodVisitor extends GeneratorAdapter implements Constants,
		Opcodes {

	private static final Log log = LogFactory.getLog(ProxyMethodVisitor.class);

	private boolean isStatic;

	private boolean isVoidReturn;

	private Type classType;

	private String handlerFieldName;

	private String methodFieldName;

	private String methodProxyFieldName;

	private String targetMethodName;

	private Method method;

	public ProxyMethodVisitor(int access, Method method, MethodVisitor mv,
			Type classType, String targetMethodName, String handlerFieldName,
			String methodFieldName, String methodProxyFieldName) {

		super(access, method, mv);

		this.method = method;
		this.isStatic = Modifier.isStatic(access);
		this.isVoidReturn = Type.VOID_TYPE.equals(method.getReturnType());
		this.classType = classType;
		this.targetMethodName = targetMethodName;
		this.handlerFieldName = handlerFieldName;
		this.methodFieldName = methodFieldName;
		this.methodProxyFieldName = methodProxyFieldName;
	}

	/**
	 * 由于生成的代理方法在，调用原方法时采用了反射调用handle的方式，所以需要对返回值进行处理
	 * 
	 * 当原方法返回值为简单类型时,handle通过反射调用返回的是Object，需要对应处理，
	 * 
	 * 参见MethodInjectHandler
	 * 
	 * @param type
	 * @see MethodInjectHandler
	 * @deprecated 改为通过变量isVoidReturn来处理返回类型
	 */
	private void unbox_or_zero(Type type) {
		if (isPrimitive(type)) {
			if (type != Type.VOID_TYPE) {
				Label nonNull = newLabel();
				Label end = newLabel();
				dup();
				//如果原方法返回的是简单类型,而handle返回的是Null，那么转换为0L返回
				ifNonNull(nonNull);
				pop();
				zero_or_null(type);//转换为0L
				goTo(end);
				mark(nonNull);
				unbox(type);
				mark(end);
			}
		} else {
			checkCast(type);
		}
	}

	//-------------------------------
	//null转换成0
	//-------------------------------
	private void zero_or_null(Type type) {
		if (isPrimitive(type)) {
			switch (type.getSort()) {
			case Type.DOUBLE:
				push(0d);
				break;
			case Type.LONG:
				push(0L);
				break;
			case Type.FLOAT:
				push(0f);
				break;
			case Type.VOID:
				mv.visitInsn(Opcodes.ACONST_NULL);
			default:
				push(0);
			}
		} else {
			mv.visitInsn(Opcodes.ACONST_NULL);
		}
	}

	private boolean isPrimitive(Type type) {
		switch (type.getSort()) {
		case Type.ARRAY:
		case Type.OBJECT:
			return false;
		default:
			return true;
		}
	}

	public void visitCode() {

		Label gotoLabel = newLabel();
		Type rt = method.getReturnType();
		int result = newLocal(rt);
		// -------------------------------------------------------------------------------

		getStatic(classType, handlerFieldName, MethodHandler.TYPE);
		dup();
		ifNull(gotoLabel);

		pushThis();
		getStatic(classType, methodFieldName, Type
				.getType(java.lang.reflect.Method.class));
		getStatic(classType, methodProxyFieldName, Type
				.getType(java.lang.reflect.Method.class));
		loadArgArray();
		invokeInterface(MethodHandler.TYPE, MethodHandler.invoke);

		if (!isVoidReturn) {
			// 由于是反射调用，所以对于handle返回的Object需要处理，判断简单类型情况，null，以及类型转换等问题
			unbox(rt);
			storeLocal(result);
			loadLocal(result);
		}

		returnValue();

		mark(gotoLabel);// 判断handle是否为null，如果为null调用原方法

		if (isStatic) {
			loadArgs(); // 加载参数
			invokeStatic(classType, new Method(targetMethodName, method
					.getDescriptor()));
		} else {
			loadThis(); // 加载调用实体
			loadArgs(); // 加载参数
			invokeVirtual(classType, new Method(targetMethodName, method
					.getDescriptor()));
		}

		if (!isVoidReturn) {
			storeLocal(result);
			loadLocal(result);

		}
		returnValue();

		// -------------------------------------------------------------------------------
		endMethod();
	}

	private void pushThis() {

		if (isStatic) {
			push("test");
		} else {
			loadThis();
		}
	}
}
