package org.openkoala.koala.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openkoala.koala.annotation.parse.ParseListFunctionCreate;
import org.openkoala.koala.annotation.parse.ParseMappedFunctionCreate;

/**
 * 标志Annotation,由ParseMappedFunctionCreate
 * 
 * 标志于String值属性上，当此值在定义的值集合中时，执行解析
 * @author lingen.liu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ParseAnnotation(type=ParseMappedFunctionCreate.class)
public @interface MappedFunctionCreate {
	
}
