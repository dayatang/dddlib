package org.openkoala.koala.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 定义Annotation由谁来进行解析
 * 
 * 每个标志解析的Annotaion都要定义一个解析类，进行解析生成
 * @author lingen.liu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ParseAnnotation {
	Class type();
}
