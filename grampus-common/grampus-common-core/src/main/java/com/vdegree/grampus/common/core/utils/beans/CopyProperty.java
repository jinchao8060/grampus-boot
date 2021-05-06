package com.vdegree.grampus.common.core.utils.beans;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * CopyProperty
 * Project: zeta
 *
 * @author Beck
 * @since 2021-01-25
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CopyProperty {

	/**
	 * 属性名，用于指定别名，默认使用：field name
	 *
	 * @return 属性名
	 */
	String value() default "";

	/**
	 * 忽略：默认为 false
	 *
	 * @return 是否忽略
	 */
	boolean ignore() default false;
}
