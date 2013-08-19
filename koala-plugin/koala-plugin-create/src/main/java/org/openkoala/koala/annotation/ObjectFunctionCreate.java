package org.openkoala.koala.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openkoala.koala.annotation.parse.ParseObjectFunctionCreate;

/**
 * 标志Annotation,由ParseObjectFunctionCreate
 * 
 * 标志于类上，当此类有此注解，进行解析
 * @author lingen.liu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@ParseAnnotation(type=ParseObjectFunctionCreate.class)
public @interface ObjectFunctionCreate {

}
