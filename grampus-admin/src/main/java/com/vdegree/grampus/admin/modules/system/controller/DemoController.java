package com.vdegree.grampus.admin.modules.system.controller;

import com.google.common.collect.Maps;
import com.vdegree.grampus.admin.modules.system.entity.SysDept;
import com.vdegree.grampus.admin.modules.system.service.SysDeptService;
import com.vdegree.grampus.common.mybatis.enums.DelFlagEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * Title: DemoController
 * Company: v-degree
 *
 * @author Beck
 * @date 2020-11-26
 */
@Slf4j
@RestController
@RequestMapping("/demo")
public class DemoController {

	@Autowired
	private SysDeptService sysDeptService;

	@GetMapping("/save")
	public ResponseEntity<Map<String, Object>> test2() {
		SysDept sysDept = new SysDept();
		sysDept.setDeptName(UUID.randomUUID().toString());
		sysDept.setCreateBy(1L);
		sysDept.setCreateDate(new Date());
		sysDept.setUpdateBy(1L);
		sysDept.setUpdateDate(new Date());
		sysDept.setDelFlag(DelFlagEnum.NORMAL.getValue());
		sysDeptService.insert(sysDept);
		Map<String, Object> result = Maps.newHashMap();
		result.put("sysDept", sysDept);
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/list")
	public ResponseEntity<Map<String, Object>> list() {
		Map<String, Object> result = Maps.newHashMap();
		result.put("list", sysDeptService.selectAll());
		return ResponseEntity.ok().body(result);
	}

	@GetMapping("/delete/{id}")
	public ResponseEntity<Map<String, Object>> delete(@PathVariable("id") Long id) {
		sysDeptService.deleteById(id);
		Map<String, Object> result = Maps.newHashMap();
		return ResponseEntity.ok().body(result);
	}
}
