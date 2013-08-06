package org.openkoala.koala.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openkoala.koala.annotation.parse.ParseListFunctionCreate;

/**
 * 标志Annotation,由ParseBooleanFunctionCreate负责解析
 * 
 * 标志于Boolean值属性上，当此值为TRUE时，执行解析
 * @author lingen.liu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ParseAnnotation(type=ParseListFunctionCreate.class)
public @interface ListFunctionCreate {
	
}
