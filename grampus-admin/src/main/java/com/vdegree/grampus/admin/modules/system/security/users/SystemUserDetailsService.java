package com.vdegree.grampus.admin.modules.system.security.users;

import com.vdegree.grampus.admin.modules.system.entity.SysUser;
import com.vdegree.grampus.admin.modules.system.security.enums.SuperAdminEnum;
import com.vdegree.grampus.admin.modules.system.enums.SysUserEnabledEnum;
import com.vdegree.grampus.admin.modules.system.security.exception.UserDisabledException;
import com.vdegree.grampus.admin.modules.system.security.exception.UserNotFoundException;
import com.vdegree.grampus.admin.modules.system.security.redis.SystemUserDetailsRedis;
import com.vdegree.grampus.admin.modules.system.security.roles.SystemRoleService;
import com.vdegree.grampus.admin.modules.system.service.SysUserService;
import com.vdegree.grampus.common.core.utils.BeanUtil;
import com.vdegree.grampus.common.core.utils.StringUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 系统用户详情
 *
 * @author Beck
 * @since 2020-12-15
 */
@AllArgsConstructor
@Service
public class SystemUserDetailsService implements UserDetailsService {

	private final SysUserService sysUserService;
	private final SystemRoleService systemRoleService;
	private final SystemUserDetailsRedis systemUserDetailsRedis;

	@Override
	public UserDetails loadUserByUsername(String userNo) throws UsernameNotFoundException {
		// 查询用户详情
		SystemUserDetails userDetails = systemUserDetailsRedis.getSystemUserDetails(userNo);
		if (userDetails != null && StringUtil.isNotBlank(userDetails.getUserNo())) {
			return userDetails;
		}
		SysUser user = sysUserService.getSysUserByUserNo(userNo);

		// 用户不存在
		if (user == null) {
			throw new UserNotFoundException();
		}

		// 用户被禁用
		if (SysUserEnabledEnum.DISABLED.getValue().equals(user.getEnabled())) {
			throw new UserDisabledException();
		}

		// 缓存系统用户详情
		userDetails = buildUserDetails(user);
		systemUserDetailsRedis.saveSystemUserDetails(userDetails);
		return userDetails;
	}

	public SystemUserDetails buildUserDetails(SysUser user) {
		SystemUserDetails systemUserDetails = BeanUtil.copy(user, SystemUserDetails.class);
		// TODO 无法copy enabled字段
		systemUserDetails.setEnabled(user.getEnabled());
		boolean isSuperAdmin = SuperAdminEnum.TRUE.getValue().equals(systemUserDetails.getSuperAdmin());
		if (Boolean.TRUE.equals(isSuperAdmin)) {
			systemUserDetails.setPermissions(systemRoleService.getAllPermissions());
		} else {
			systemUserDetails.setPermissions(systemRoleService.getPermissions(user.getId()));
		}
		return systemUserDetails;
	}
}
