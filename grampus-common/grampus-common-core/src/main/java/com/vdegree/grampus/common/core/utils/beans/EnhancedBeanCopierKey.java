package com.vdegree.grampus.common.core.utils.beans;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Title: EnhancedBeanCopierKey
 * Project: zeta
 *
 * @author Beck
 * @date 2021-01-25
 */
@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class EnhancedBeanCopierKey {
	private final Class<?> source;
	private final Class<?> target;
	private final boolean useConverter;
	private final boolean nonNull;
}