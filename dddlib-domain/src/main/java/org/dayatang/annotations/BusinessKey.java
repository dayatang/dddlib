package org.dayatang.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 标识业务主键。业务主键用于区分同一实体类型的不同实例。相同类型的两个实体，如果其业务主键相同，即可认为两者是等同的。
 * @author yyang
 *
 */
@Target({TYPE})
@Retention(RUNTIME)
@Documented
public @interface BusinessKey {
	public String[] value();
}
