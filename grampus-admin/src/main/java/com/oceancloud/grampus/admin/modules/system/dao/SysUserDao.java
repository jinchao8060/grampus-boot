package com.oceancloud.grampus.admin.modules.system.dao;

import com.oceancloud.grampus.admin.modules.system.entity.SysUser;
import com.oceancloud.grampus.common.mybatis.annotation.MyBatisMapper;
import com.oceancloud.grampus.common.mybatis.mapper.BaseMapper;

/**
 * 用户表 数据库访问层
 *
 * @author Beck
 * @since 2020-12-09 19:50:58
 */
@MyBatisMapper
public interface SysUserDao extends BaseMapper<SysUser> {

}