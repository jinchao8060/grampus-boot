package com.oceancloud.grampus.admin.modules.system.service.impl;

import com.oceancloud.grampus.admin.modules.system.dto.SysUserDTO;
import com.oceancloud.grampus.admin.modules.system.security.enums.SuperAdminEnum;
import com.oceancloud.grampus.admin.modules.system.security.redis.SystemUserDetailsRedis;
import com.oceancloud.grampus.admin.modules.system.security.utils.SecurityUtils;
import com.oceancloud.grampus.admin.modules.system.service.SysUserRoleService;
import com.oceancloud.grampus.framework.core.utils.BeanUtil;
import com.oceancloud.grampus.framework.core.utils.CollectionUtil;
import com.oceancloud.grampus.framework.core.utils.StringUtil;
import com.oceancloud.grampus.framework.mybatis.enums.DelFlagEnum;
import com.oceancloud.grampus.admin.modules.system.dao.SysUserDao;
import com.oceancloud.grampus.admin.modules.system.entity.SysUser;
import com.oceancloud.grampus.admin.modules.system.service.SysUserService;
import com.oceancloud.grampus.framework.mybatis.service.impl.EnhancedBaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 用户表 服务实现类
 *
 * @author Beck
 * @since 2020-12-09 19:50:59
 */
@AllArgsConstructor
@Service("sysUserService")
public class SysUserServiceImpl extends EnhancedBaseServiceImpl<SysUserDao, SysUser, SysUserDTO> implements SysUserService {

	private final PasswordEncoder passwordEncoder;
	private final SysUserRoleService sysUserRoleService;
	private final SystemUserDetailsRedis systemUserDetailsRedis;

	@Override
	public SysUser getSysUserByUserNo(String userNo) {
		SysUser entity = new SysUser();
		entity.setUserNo(userNo);
		entity.setDelFlag(DelFlagEnum.NORMAL.getValue());
		return baseMapper.selectOne(entity);
	}

	@Override
	public void updatePassword(Long userId, String newPassword) {
		SysUser sysUser = selectById(userId);
		// 超管才能修改超管
		if (SuperAdminEnum.TRUE.getValue().equals(sysUser.getSuperAdmin())
				&& !SuperAdminEnum.TRUE.getValue().equals(SecurityUtils.getUserDetails().getSuperAdmin())) {
			return;
		}
		SysUser entity = new SysUser();
		entity.setId(userId);
		entity.setPassword(StringUtil.isNotBlank(newPassword) ? passwordEncoder.encode(newPassword) : null);
		baseMapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void save(SysUserDTO dto) {
		SysUser entity = BeanUtil.copy(dto, SysUser.class);
		String plainPwd = entity.getPassword();
		// 新建账号支持空密码
		if (StringUtil.isNotBlank(plainPwd)) {
			entity.setPassword(passwordEncoder.encode(plainPwd));
		}
		entity.setSuperAdmin(SuperAdminEnum.FALSE.getValue());
		baseMapper.insert(entity);
		sysUserRoleService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void modifyById(SysUserDTO dto) {
		SysUser entity = BeanUtil.copy(dto, SysUser.class);
		SysUser sysUser = selectById(entity.getId());
		// 超管才能修改超管
		if (SuperAdminEnum.TRUE.getValue().equals(sysUser.getSuperAdmin())
				&& !SuperAdminEnum.TRUE.getValue().equals(SecurityUtils.getUserDetails().getSuperAdmin())) {
			return;
		}
		String userNo = sysUser.getUserNo();
		String plainPwd = entity.getPassword();
		// 输入空密码则不进行更改
		entity.setPassword(StringUtil.isNotBlank(plainPwd) ? passwordEncoder.encode(plainPwd) : null);
		entity.setSuperAdmin(null);
		updateById(entity);
		if (CollectionUtil.isNotEmpty(dto.getRoleIdList())) {
			sysUserRoleService.saveOrUpdate(entity.getId(), dto.getRoleIdList());
		}
		systemUserDetailsRedis.removeSystemUserDetails(userNo);
	}
}