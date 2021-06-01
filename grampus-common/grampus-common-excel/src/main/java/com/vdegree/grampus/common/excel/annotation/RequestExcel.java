package com.vdegree.grampus.common.excel.annotation;

import com.vdegree.grampus.common.excel.read.DefaultAnalysisEventListener;
import com.vdegree.grampus.common.excel.read.ListAnalysisEventListener;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Excel 导入
 *
 * @author Beck
 * @since 2021-5-14
 */
@Documented
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

	/**
	 * 前端上传字段名称 file
	 */
	String fileName() default "file";

	/**
	 * 读取的监听器类
	 */
	Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

	/**
	 * 是否跳过空行
	 */
	boolean ignoreEmptyRow() default false;

}
