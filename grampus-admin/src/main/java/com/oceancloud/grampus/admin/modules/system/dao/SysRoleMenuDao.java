package com.oceancloud.grampus.admin.modules.system.dao;

import com.oceancloud.grampus.admin.modules.system.entity.SysRoleMenu;
import com.oceancloud.grampus.common.mybatis.annotation.MyBatisMapper;
import com.oceancloud.grampus.common.mybatis.mapper.BaseMapper;

/**
 * 角色菜单表 数据库访问层
 *
 * @author Beck
 * @since 2020-12-09 19:50:43
 */
@MyBatisMapper
public interface SysRoleMenuDao extends BaseMapper<SysRoleMenu> {

}