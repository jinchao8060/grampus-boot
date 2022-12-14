package com.oceancloud.grampus.admin.modules.system.service.impl;

import com.google.common.collect.Sets;
import com.oceancloud.grampus.admin.modules.system.dto.SysDictDTO;
import com.oceancloud.grampus.admin.modules.system.dao.SysDictDao;
import com.oceancloud.grampus.admin.modules.system.dto.SysDictItemDTO;
import com.oceancloud.grampus.admin.modules.system.entity.SysDict;
import com.oceancloud.grampus.admin.modules.system.service.SysDictItemService;
import com.oceancloud.grampus.admin.modules.system.service.SysDictService;
import com.oceancloud.grampus.framework.core.utils.CollectionUtil;
import com.oceancloud.grampus.framework.mybatis.service.impl.EnhancedBaseServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字典表 服务实现类
 *
 * @author Beck
 * @since 2020-12-09
 */
@AllArgsConstructor
@Service("sysDictService")
public class SysDictServiceImpl extends EnhancedBaseServiceImpl<SysDictDao, SysDict, SysDictDTO> implements SysDictService {

	private final SysDictItemService sysDictItemService;

	@Override
	public List<SysDict> getSysDictList() {
		return baseMapper.getSysDictList();
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeBatchByIds(Collection<?> idList) {
		Set<Long> itemIdSet = Sets.newLinkedHashSet();
		for (Object id : idList) {
			SysDictItemDTO params = new SysDictItemDTO();
			params.setDictId((Long) id);
			List<SysDictItemDTO> itemList = sysDictItemService.queryList(params);
			itemIdSet.addAll(itemList.stream().map(SysDictItemDTO::getId).collect(Collectors.toSet()));
		}
		if (CollectionUtil.isNotEmpty(itemIdSet)) {
			sysDictItemService.removeBatchByIds(itemIdSet);
		}
		this.baseMapper.deleteBatchIds(idList);
		// TODO 优化删除结果
		return true;
	}
}