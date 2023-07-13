package com.oceancloud.grampus.admin.modules.system.service.impl;

import com.oceancloud.grampus.admin.modules.system.dao.SysDeptDao;
import com.oceancloud.grampus.admin.modules.system.dto.SysDeptDTO;
import com.oceancloud.grampus.admin.modules.system.entity.SysDept;
import com.oceancloud.grampus.admin.modules.system.service.SysDeptService;
import com.oceancloud.grampus.framework.mybatis.service.impl.EnhancedBaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 部门表 服务实现类
 *
 * @author Beck
 * @since 2020-12-03
 */
@Service("sysDeptService")
public class SysDeptServiceImpl extends EnhancedBaseServiceImpl<SysDeptDao, SysDept, SysDeptDTO> implements SysDeptService {

}