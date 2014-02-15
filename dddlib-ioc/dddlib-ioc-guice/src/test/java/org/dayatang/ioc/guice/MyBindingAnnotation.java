package org.dayatang.ioc.guice;

import com.google.inject.BindingAnnotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by yyang on 14-2-8.
 */
@BindingAnnotation
@Retention(RetentionPolicy.RUNTIME)
public @interface MyBindingAnnotation {
}
