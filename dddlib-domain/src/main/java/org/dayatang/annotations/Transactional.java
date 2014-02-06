package org.dayatang.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标注方法必须在事务中运行
 */
@Target(METHOD)
@Retention(RUNTIME)
@Documented
public @interface Transactional {

}
