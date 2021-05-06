package com.vdegree.grampus.admin.modules.system.security.enums;

import lombok.Getter;

/**
 * 超级管理员枚举
 *
 * @author Beck
 * @date 2021-04-09
 */
public enum SuperAdminEnum {
	/**
	 * 超级管理员
	 */
	TRUE(1),
	/**
	 * 非超级管理员
	 */
	FALSE(0);

	@Getter
	private final Integer value;

	SuperAdminEnum(Integer value) {
		this.value = value;
	}
}