package com.vdegree.grampus.admin.modules.system.service;

import com.vdegree.grampus.admin.modules.system.dto.SysDictDTO;
import com.vdegree.grampus.admin.modules.system.entity.SysDict;
import com.vdegree.grampus.common.mybatis.service.EnhancedBaseService;

import java.util.List;

/**
 * 字典表 服务接口
 *
 * @author Beck
 * @since 2020-12-09 19:47:47
 */
public interface SysDictService extends EnhancedBaseService<SysDict, SysDictDTO> {

	/**
	 * 获取系统字典列表
	 */
	List<SysDict> getSysDictList();
}